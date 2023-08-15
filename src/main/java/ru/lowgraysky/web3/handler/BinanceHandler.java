package ru.lowgraysky.web3.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.lowgraysky.web3.service.BinanceService;

@Component
@RequiredArgsConstructor
public class BinanceHandler {

  private final BinanceService binanceService;

  public Mono<ServerResponse> gasPrice(ServerRequest request) {
    return ServerResponse.ok()
            .body();
  }
}
