package ru.lowgraysky.web3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lowgraysky.web3.binance.config.BinanceCoreProperties;
import ru.lowgraysky.web3.binance.config.BinancePathProperties;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public BinancePathProperties binancePathProperties() {
    return new BinancePathProperties();
  }

  @Bean
  public BinanceCoreProperties binanceCoreProperties() {
    return new BinanceCoreProperties();
  }
}
