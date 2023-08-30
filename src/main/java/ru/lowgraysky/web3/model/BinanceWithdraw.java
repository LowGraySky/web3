package ru.lowgraysky.web3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BinanceWithdraw {
  private String coin;
  private String network;
  private String address;
  private double amount;
}
