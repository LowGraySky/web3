package ru.lowgraysky.web3.binance.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "binance.core")
public class BinanceCoreProperties {
  private String baseurl;
  private String apikey;
  private String secretkey;
}
