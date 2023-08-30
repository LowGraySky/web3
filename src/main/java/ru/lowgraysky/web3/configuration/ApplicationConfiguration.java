package ru.lowgraysky.web3.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lowgraysky.web3.binance.config.BinanceCoreProperties;
import ru.lowgraysky.web3.binance.config.BinancePathProperties;

@Configuration
@EnableConfigurationProperties({BinanceCoreProperties.class, BinancePathProperties.class})
public class ApplicationConfiguration {

  @Bean
  public RouterConfiguration routerConfiguration() {
    return new RouterConfiguration();
  }
}
