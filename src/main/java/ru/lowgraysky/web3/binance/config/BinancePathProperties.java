package ru.lowgraysky.web3.binance.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@AllArgsConstructor
@ConfigurationProperties(prefix = "binance.path")
public class BinancePathProperties {
  private String time;
  private String withdraw;
  private String order;
}
