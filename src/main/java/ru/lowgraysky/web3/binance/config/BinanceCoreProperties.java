package ru.lowgraysky.web3.binance.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@AllArgsConstructor
@ConfigurationProperties(prefix = "binance.core")
public class BinanceCoreProperties {
  private String baseUrl;
  private String apikey;
  private String secretKey;
}
