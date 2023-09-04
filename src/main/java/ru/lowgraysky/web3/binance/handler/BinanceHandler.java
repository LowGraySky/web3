package ru.lowgraysky.web3.binance.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.lowgraysky.web3.binance.model.ServerTimeResponse;
import ru.lowgraysky.web3.binance.model.SpotResponse;
import ru.lowgraysky.web3.binance.model.WithdrawResponse;
import ru.lowgraysky.web3.binance.service.BinanceService;
import ru.lowgraysky.web3.handler.BaseHandler;
import ru.lowgraysky.web3.model.BinanceSpotOrder;
import ru.lowgraysky.web3.model.BinanceWithdraw;

@Component
@RequiredArgsConstructor
public class BinanceHandler implements BaseHandler {

  private final BinanceService binanceService;

  public Mono<ServerResponse> limit(ServerRequest request) {
    return request.bodyToMono(BinanceSpotOrder.class)
            .flatMap(order -> binanceService.limit(
                    order.getSymbol(),
                    order.getSide(),
                    order.getQuantity(),
                    order.getPrice())
            )
            .flatMap(response -> ServerResponse.ok().body(response, SpotResponse.class)
            )
            .onErrorResume(ERROR_FUNCTION);
  }

  public Mono<ServerResponse> market(ServerRequest request) {
    return request.bodyToMono(BinanceSpotOrder.class)
            .flatMap(order -> binanceService.market(
                    order.getSymbol(),
                    order.getSide(),
                    order.getQuantity(),
                    order.getPrice())
            )
            .flatMap(response -> ServerResponse.ok().body(response, SpotResponse.class)
            )
            .onErrorResume(ERROR_FUNCTION);
  }

  public Mono<ServerResponse> time(ServerRequest request) {
    return ServerResponse.ok()
            .body(binanceService.serverTime(), ServerTimeResponse.class)
            .onErrorResume(ERROR_FUNCTION);
  }

  public Mono<ServerResponse> withdraw(ServerRequest request) {
    return request.bodyToMono(BinanceWithdraw.class )
            .flatMap(body -> binanceService.withdraw(
                    body.getCoin(),
                    body.getNetwork(),
                    body.getAddress(),
                    body.getAmount()
            ))
            .flatMap(response -> ServerResponse.ok().body(response, WithdrawResponse.class))
            .onErrorResume(ERROR_FUNCTION);
  }

  public Mono<ServerResponse> balance(ServerRequest request) {
    return ServerResponse.ok()
            .body(binanceService.balance(), String.class)
            .onErrorResume(ERROR_FUNCTION);
  }
}
