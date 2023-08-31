package ru.lowgraysky.web3.telegram.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

  private final TelegramProperties telegramProperties;

  @Override
  public void onUpdateReceived(Update update) {
    log.info("Update: " + update.toString());
  }

  @Override
  public String getBotUsername() {
    return telegramProperties.getUsername();
  }

  @Override
  public void onRegister() { Mono
          .just("Telegram bot started...")
          .doOnNext(log::info)
          .flatMap(this::sendMessage)
          .doOnError(e -> log.error(e.getMessage()))
          .subscribe();
  }

  private Mono<Message> sendMessage(String message) {
    SendMessage msg = createMessage(message);
    log.info("Send message: " + message);
    try {
      return Mono.fromFuture(this.executeAsync(msg));
    } catch (TelegramApiException e ) {
       return Mono.error(e);
    }
  }

  private SendMessage createMessage(String text) {
    SendMessage message = new SendMessage();
    message.setText(text);
    return message;
  }
}