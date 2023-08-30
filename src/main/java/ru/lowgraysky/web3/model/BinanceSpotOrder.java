package ru.lowgraysky.web3.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.lowgraysky.web3.binance.model.enums.Side;

@Data
@NoArgsConstructor
public class BinanceSpotOrder {
  private String symbol;
  private Side side;
  private double quantity;
  private double price;
}
