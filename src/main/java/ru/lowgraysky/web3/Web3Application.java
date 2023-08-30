package ru.lowgraysky.web3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.lowgraysky.web3.binance.config.BinanceCoreProperties;
import ru.lowgraysky.web3.binance.config.BinancePathProperties;

@SpringBootApplication
@EnableConfigurationProperties({BinanceCoreProperties.class, BinancePathProperties.class})
public class Web3Application {

  public static void main(String[] args) {
    SpringApplication.run(Web3Application.class, args);
  }
}
