package ru.lowgraysky.web3.telegram.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import reactor.core.publisher.Mono;
import ru.lowgraysky.web3.binance.service.BinanceService;
import ru.lowgraysky.web3.telegram.config.TelegramProperties;

import java.util.ArrayList;
import java.util.List;

import static ru.lowgraysky.web3.telegram.model.ExchangeName.BINANCE;
import static ru.lowgraysky.web3.telegram.model.ExchangeName.BITGET;

@Slf4j
@Getter
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

  private final BinanceService binanceService;
  private final TelegramProperties telegramProperties;
  private final List<String> keyboardButtonsText = List.of(BINANCE, BITGET);

  @Override
  public String getBotToken() {
    return telegramProperties.getToken();
  }

  @Override
  public String getBotUsername() {
    return telegramProperties.getUsername();
  }

  @Override
  public void onRegister() { Mono
          .just("Telegram bot started successfully registered")
          .doOnNext(log::info)
          .flatMap(msg -> sendMessage(msg, exchangesKeyboard()))
          .doOnError(e -> log.error(e.getMessage()))
          .subscribe();
  }

  @Override
  public void onUpdateReceived(Update update) {
    log.info("Telegram message: " + update.toString());
    String command = update.getMessage().getText();
    exchangesKeyboard();
  }

  private ReplyKeyboardMarkup exchangesKeyboard() {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboardRows = new ArrayList<>(1);
    KeyboardRow row = new KeyboardRow();
    for (String buttonText: keyboardButtonsText) {
      KeyboardButton button = new KeyboardButton();
      button.setText(buttonText);
      row.add(button);
    }
    keyboardRows.add(row);
    keyboardMarkup.setKeyboard(keyboardRows);
    keyboardMarkup.setResizeKeyboard(true);
    return keyboardMarkup;
  }



  private Mono<Message> sendMessage(String message, ReplyKeyboardMarkup replyKeyboardMarkup) {
    SendMessage msg = createMessage(message, replyKeyboardMarkup);
    log.info("Sending message to telegram: " + message);
    try {
      return Mono.fromFuture(this.executeAsync(msg));
    } catch (TelegramApiException e ) {
       return Mono.error(e);
    }
  }

  private SendMessage createMessage(String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
    SendMessage message = new SendMessage();
    message.setText(text);
    message.setChatId(telegramProperties.getChatId());
    message.setReplyMarkup(replyKeyboardMarkup);
    return message;
  }
}