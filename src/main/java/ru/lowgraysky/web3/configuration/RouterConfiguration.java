package ru.lowgraysky.web3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.lowgraysky.web3.handler.BinanceHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

  @Bean
  public RouterFunction<ServerResponse> routes(BinanceHandler handler) {
    return route(GET("/binance"), handler::gasPrice);
  }
}
