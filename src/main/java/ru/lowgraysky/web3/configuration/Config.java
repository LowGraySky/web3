package ru.lowgraysky.web3.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

@Configuration
public class Config {

  @Value("binance.apikey")
  private String BINANCE_API_KEY;

}
