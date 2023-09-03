package ru.lowgraysky.web3;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import ru.lowgraysky.web3.binance.config.BinanceCoreProperties;
import ru.lowgraysky.web3.binance.config.BinancePathProperties;
import ru.lowgraysky.web3.binance.model.enums.Side;
import ru.lowgraysky.web3.binance.service.BinanceService;

public class BinanceServiceTest {

  private final BinanceCoreProperties coreProperties;
  private final BinancePathProperties pathProperties;
  private final BinanceService binanceService;

  public BinanceServiceTest(BinancePathProperties binancePathProperties, BinanceCoreProperties binanceCoreProperties) {
    this.coreProperties = binanceCoreProperties;
    this.pathProperties = binancePathProperties;
    binanceService = new BinanceService(pathProperties, coreProperties);
  }

  @Test
  public void time() {
    StepVerifier.create(binanceService.serverTime())
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete();
  }

  @Test
  public void testOrder() {
    StepVerifier.create(binanceService.limit("BTCUSDT", Side.BUY, 1, 17000))
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete();
  }
}
