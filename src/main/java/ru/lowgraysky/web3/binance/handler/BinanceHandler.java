package ru.lowgraysky.web3.binance.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.lowgraysky.web3.binance.model.ServerTimeResponse;
import ru.lowgraysky.web3.binance.model.SpotResponse;
import ru.lowgraysky.web3.binance.service.BinanceService;
import ru.lowgraysky.web3.model.BinanceSpotOrder;

@Component
@RequiredArgsConstructor
public class BinanceHandler {

  private final BinanceService binanceService;

  public Mono<ServerResponse> limit(ServerRequest request) {
    return request.bodyToMono(BinanceSpotOrder.class)
            .flatMap(order -> binanceService.limit(
                    order.getSymbol(),
                    order.getSide(),
                    order.getQuantity(),
                    order.getPrice())
            )
            .flatMap(response -> ServerResponse.ok()
                    .body(response, SpotResponse.class)
            );
  }

  public Mono<ServerResponse> market(ServerRequest request) {
    return request.bodyToMono(BinanceSpotOrder.class)
            .flatMap(order -> binanceService.market(
                    order.getSymbol(),
                    order.getSide(),
                    order.getQuantity(),
                    order.getPrice())
            )
            .flatMap(response -> ServerResponse.ok()
                    .body(response, SpotResponse.class)
            );
  }

  public Mono<ServerResponse> time(ServerRequest request) {
    return ServerResponse.ok()
            .body(binanceService.serverTime(), ServerTimeResponse.class);
  }
}
