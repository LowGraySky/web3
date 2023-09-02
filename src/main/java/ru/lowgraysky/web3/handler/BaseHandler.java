package ru.lowgraysky.web3.handler;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface BaseHandler {

  Function<Throwable, Mono<? extends ServerResponse>> ERROR_FUNCTION =
          error -> ServerResponse.badRequest().body(error, Exception.class);
}
