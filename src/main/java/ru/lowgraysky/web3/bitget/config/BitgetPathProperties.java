package ru.lowgraysky.web3.bitget.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@AllArgsConstructor
@ConfigurationProperties(prefix = "bitget.path")
public class BitgetPathProperties {
    private String order;
}
