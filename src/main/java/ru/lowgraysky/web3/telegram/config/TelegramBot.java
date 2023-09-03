package ru.lowgraysky.web3.telegram.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import reactor.core.publisher.Mono;
import ru.lowgraysky.web3.binance.service.BinanceService;

@Slf4j
@Getter
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

  private final BinanceService binanceService;
  private final TelegramProperties telegramProperties;

  @Override
  public String getBotToken() {
    return telegramProperties.getToken();
  }

  @Override
  public void onUpdateReceived(Update update) {
    log.info("Telegram message: " + update.toString());
    String command = update.getMessage().getText();
    switch (command) {
      case "/time": {
        binanceService.serverTime()
                        .flatMap(time -> sendMessage(Long.toString(time.getServerTime())))
                                .subscribe();
        break;
      }
      default: {
        //telegramCommandResolver.resolve(command);
        break;
      }
    }
  }

  @Override
  public String getBotUsername() {
    return telegramProperties.getUsername();
  }

  @Override
  public void onRegister() { Mono
          .just("Telegram bot started successfully registered")
          .doOnNext(log::info)
          .doOnError(e -> log.error(e.getMessage()))
          .subscribe();
  }

  private Mono<Message> sendMessage(String message) {
    SendMessage msg = createMessage(message);
    log.info("Sending message to telegram: " + message);
    try {
      return Mono.fromFuture(this.executeAsync(msg));
    } catch (TelegramApiException e ) {
       return Mono.error(e);
    }
  }

  private SendMessage createMessage(String text) {
    SendMessage message = new SendMessage();
    message.setText(text);
    message.setChatId(telegramProperties.getChatId());
    return message;
  }
}