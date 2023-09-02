package ru.lowgraysky.web3.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.lowgraysky.web3.binance.config.BinanceCoreProperties;
import ru.lowgraysky.web3.binance.config.BinancePathProperties;
import ru.lowgraysky.web3.telegram.config.TelegramBot;
import ru.lowgraysky.web3.telegram.config.TelegramProperties;

@Slf4j
@Configuration
@ConfigurationPropertiesScan
@EnableConfigurationProperties({BinanceCoreProperties.class, BinancePathProperties.class, TelegramProperties.class})
public class ApplicationConfiguration {

  @Bean
  public TelegramBot telegramBot(TelegramProperties telegramProperties) {
    TelegramBot telegramBot = new TelegramBot(telegramProperties);
    try {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      botsApi.registerBot(telegramBot);
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
    return telegramBot;
  }
}
