package ru.lowgraysky.web3.binance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import ru.lowgraysky.web3.binance.config.BinanceCoreProperties;
import ru.lowgraysky.web3.binance.config.BinancePathProperties;
import ru.lowgraysky.web3.binance.model.BalanceResponse;
import ru.lowgraysky.web3.binance.model.ServerTimeResponse;
import ru.lowgraysky.web3.binance.model.SpotResponse;
import ru.lowgraysky.web3.binance.model.WithdrawResponse;
import ru.lowgraysky.web3.binance.model.enums.OrderType;
import ru.lowgraysky.web3.binance.model.enums.ResponseType;
import ru.lowgraysky.web3.binance.model.enums.Side;
import ru.lowgraysky.web3.binance.model.enums.TimeInForce;
import ru.lowgraysky.web3.service.RemoteRequestService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BinanceService extends RemoteRequestService {

  private static final String WALLET_TYPE = "0";
  private final BinancePathProperties binancePathProperties;
  private final BinanceCoreProperties binanceCoreProperties;

  public BinanceService(BinancePathProperties pathProperties, BinanceCoreProperties coreProperties) {
    super(coreProperties.getBaseUrl());
    this.binanceCoreProperties = coreProperties;
    this.binancePathProperties = pathProperties;
  }

  public Mono<BalanceResponse> balance() {
    Map<String, List<String>> par = new HashMap<>();
    par.put("timestamp", List.of(Long.toString(nowInMillis())));
    return Mono.just(par)
            .map(MultiValueMapAdapter::new)
            .doOnNext(params -> logRequest(log, "GET", binancePathProperties.getBalance(), params))
            .flatMap(params -> webClientWithAuth(binancePathProperties.getBalance(), params)
                    .get()
                    .exchangeToMono(responseFunction(BalanceResponse.class))
            )
            .doOnNext(response -> logResponse(log, response))
            .map(res -> {
              List<BalanceResponse.Balance> balances = res.getBalances()
                      .stream()
                      .filter(b -> b.getFree().compareTo(BigDecimal.ZERO) > 0)
                      .collect(Collectors.toList());
              res.setBalances(balances);
              return res;
            });
  }

  public Mono<ServerTimeResponse> serverTime() {
    logRequest(log, "GET", binancePathProperties.getTime(), Collections.emptyMap());
    return webClient(binancePathProperties.getTime(), new MultiValueMapAdapter<>(Collections.emptyMap()))
            .get()
            .exchangeToMono(responseFunction(ServerTimeResponse.class))
            .doOnNext(response -> logResponse(log, response));
  }

  public Mono<SpotResponse> limit(String symbol, Side side, double quantity, double price) {
    Map<String, List<String>> paramsMap =
            buildSpotParametersMap(symbol, side, quantity, price, nowInMillis(), OrderType.LIMIT);
    paramsMap.put("timeInForce", List.of(TimeInForce.FOK.name()));
    return Mono.just(paramsMap)
            .map(MultiValueMapAdapter::new)
            .doOnNext(params -> logRequest(log, "POST", binancePathProperties.getOrder(), params))
            .flatMap(params -> webClientWithAuth(binancePathProperties.getOrder(), params)
                    .get()
                    .exchangeToMono(responseFunction(SpotResponse.class))
            )
            .doOnNext(response -> logResponse(log, response));
  }

  public Mono<SpotResponse> market(String symbol, Side side, double quantity, double price) {
    return Mono.just(buildSpotParametersMap(symbol, side, quantity, price, nowInMillis(), OrderType.MARKET))
            .map(MultiValueMapAdapter::new)
            .doOnNext(params -> logRequest(log, "POST", binancePathProperties.getOrder(), params))
            .flatMap(params ->
                    webClientWithAuth(binancePathProperties.getOrder(), params)
                            .get()
                            .exchangeToMono(responseFunction(SpotResponse.class))
            )
            .doOnNext(response -> logResponse(log, response));
  }

  public Mono<WithdrawResponse> withdraw(String coin, String network, String address, double amount) {
    Map<String, List<String>> par = new HashMap<>();
    par.put("coin", List.of(coin));
    par.put("network", List.of(network));
    par.put("address", List.of(address));
    par.put("amount", List.of(Double.toString(amount)));
    par.put("walletType", List.of(WALLET_TYPE));
    par.put("timestamp", List.of(Long.toString(nowInMillis())));
    return Mono.just(new MultiValueMapAdapter<>(par))
            .doOnNext(params -> logRequest(log, "POST", binancePathProperties.getWithdraw(), params))
            .flatMap(params ->
                    webClientWithAuth(binancePathProperties.getWithdraw(), params)
                            .post()
                            .exchangeToMono(responseFunction(WithdrawResponse.class))
            )
            .doOnNext(response -> logResponse(log, response));
  }

  private Map<String, List<String>> buildSpotParametersMap(
          String symbol,
          Side side,
          double quantity,
          double price,
          long timestamp,
          OrderType orderType
  ) {
    Map<String, List<String>> paramsMap = new HashMap<>();
    paramsMap.put("symbol", List.of(symbol));
    paramsMap.put("side", List.of(side.name()));
    paramsMap.put("type", List.of(orderType.name()));
    paramsMap.put("timestamp", List.of(Long.toString(timestamp)));
    paramsMap.put("quantity", List.of(Double.toString(quantity)));
    paramsMap.put("price", List.of(Double.toString(price)));
    paramsMap.put("newOrderRespType", List.of(ResponseType.ACK.name()));
    return paramsMap;
  }

  private WebClient webClientWithAuth(String path, MultiValueMap<String, String> params) {
    int size = 16 * 1024 * 1024;
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
            .build();
    MultiValueMap<String, String> parameters = addSignatureToParameters(params, binanceCoreProperties.getSecretKey());
    return WebClient.builder()
            .exchangeStrategies(strategies)
            .uriBuilderFactory(new DefaultUriBuilderFactory(uriComponentsBuilder(path, parameters)))
            .defaultHeader("X-MBX-APIKEY", binanceCoreProperties.getApiKey())
            .build();
  }
}
