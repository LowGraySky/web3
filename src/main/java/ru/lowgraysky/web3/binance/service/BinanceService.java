package ru.lowgraysky.web3.binance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import ru.lowgraysky.web3.binance.model.*;
import ru.lowgraysky.web3.binance.model.enums.OrderType;
import ru.lowgraysky.web3.binance.model.enums.ResponseType;
import ru.lowgraysky.web3.binance.model.enums.Side;
import ru.lowgraysky.web3.binance.model.enums.TimeInForce;
import ru.lowgraysky.web3.binance.config.BinanceCoreProperties;
import ru.lowgraysky.web3.binance.config.BinancePathProperties;
import ru.lowgraysky.web3.service.RemoteRequestService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BinanceService extends RemoteRequestService {

  private final BinancePathProperties binancePathProperties;
  private final BinanceCoreProperties binanceCoreProperties;

  public BinanceService(BinancePathProperties pathProperties, BinanceCoreProperties coreProperties) {
    super(coreProperties.getBaseUrl());
    this.binanceCoreProperties = coreProperties;
    this.binancePathProperties = pathProperties;
  }

  public Mono<BalanceResponse> balance() {
    throw new UnsupportedOperationException("Balance operation not supported!");
//    Map<String, Object> params = Map.of(
//            "type", "SPOT",
//            "timestamp", epochNow()
//            );
//    return webClient(BALANCE_PATH, params)
//            .get()
//            .exchangeToMono(responseFunction(BalanceResponse.class))
//            .map(BalanceResponse::getSnapshotVos)
//            .map();
  }

  public Mono<ServerTimeResponse> serverTime() {
    return webClient(binancePathProperties.getTime(), Collections.emptyMap())
            .get()
            .exchangeToMono(responseFunction(ServerTimeResponse.class));
  }

  public Mono<SpotResponse> limit(String symbol, Side side, double quantity, double price) {
    return serverTime()
            .map(response -> {Map<String, Object> paramsMap =
                    buildSpotParametersMap(symbol, side, quantity, price, response.getServerTime(), OrderType.LIMIT);
              paramsMap.put("timeInForce", TimeInForce.FOK);
              return paramsMap;
            })
            .flatMap(params -> webClientWithAuth(binancePathProperties.getOrder(), params)
                    .get()
                    .exchangeToMono(responseFunction(SpotResponse.class))
            );
  }

  public Mono<SpotResponse> market(String symbol, Side side, double quantity, double price) {
    return serverTime()
            .map(response ->
                    buildSpotParametersMap(symbol, side, quantity, price, response.getServerTime(), OrderType.MARKET))
            .flatMap(params ->
                    webClientWithAuth(binancePathProperties.getOrder(), params)
                            .get()
                            .exchangeToMono(responseFunction(SpotResponse.class))
            );
  }

  public Mono<WithdrawResponse> withdraw(String coin, String network, String address, double amount) {
    return serverTime()
            .map(response -> {
              Map<String, Object> params = getMapWithSignature();
              params.put("coin", coin);
              params.put("network", network);
              params.put("address", address);
              params.put("amount", amount);
              params.put("walletType", 0);
              params.put("timestamp", response.getServerTime());
              return params;
            })
            .flatMap(params ->
                    webClientWithAuth(binancePathProperties.getWithdraw(), params)
                            .post()
                            .exchangeToMono(responseFunction(WithdrawResponse.class))
            );
  }

  private Map<String, Object> buildSpotParametersMap(
          String symbol,
          Side side,
          double quantity,
          double price,
          long timestamp,
          OrderType orderType
  ) {
    Map<String, Object> paramsMap = getMapWithSignature();
    paramsMap.put("symbol", symbol);
    paramsMap.put("side", side);
    paramsMap.put("type", orderType);
    paramsMap.put("timestamp", timestamp);
    paramsMap.put("quantity", quantity);
    paramsMap.put("price", price);
    paramsMap.put("newOrderRespType", ResponseType.ACK);
    return paramsMap;
  }

  private Map<String, Object> getMapWithSignature() {
    Map<String, Object> map = new HashMap<>();
    map.put("signature", binanceCoreProperties.getSecretKey());
    return map;
  }

  private WebClient webClientWithAuth(String path, Map<String, Object> params) {
    return WebClient.builder()
            .uriBuilderFactory(new DefaultUriBuilderFactory(uriComponentsBuilder(path, params)))
            .defaultHeader("X-MBX-APIKEY", binanceCoreProperties.getApiKey())
            .build();
  }
}
