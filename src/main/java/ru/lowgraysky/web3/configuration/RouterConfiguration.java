package ru.lowgraysky.web3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.lowgraysky.web3.binance.handler.BinanceHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class RouterConfiguration {

  @Bean
  public RouterFunction<ServerResponse> routes(BinanceHandler handler) {
    return route()
            .path("binance", router -> router
                    .GET("/time", handler::time)
                    .POST("/limit", handler::limit)
                    .POST("/market", handler::market)
                    .POST("/withdraw", handler::withdraw))
            .build();
  }
}
