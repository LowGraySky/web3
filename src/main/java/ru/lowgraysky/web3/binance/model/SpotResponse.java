package ru.lowgraysky.web3.binance.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpotResponse {
  private String symbol;
  private int orderListId;
  private String clientOrderId;
  private long transactTime;
}
