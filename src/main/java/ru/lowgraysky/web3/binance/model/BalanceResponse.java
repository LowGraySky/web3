package ru.lowgraysky.web3.binance.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BalanceResponse {
  private List<Balance> balances;

  @Data
  public static class Balance {
    private String asset;
    private BigDecimal free;
  }
}
