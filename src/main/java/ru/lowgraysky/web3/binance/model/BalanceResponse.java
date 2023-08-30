package ru.lowgraysky.web3.binance.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class BalanceResponse {
  private String code;
  private String msg;
  private Collection<Vos> snapshotVos;

  @Data
  @NoArgsConstructor
  public static class Vos {
    private VosData data;
    private String type;
    private long updateTime;
  }

  @Data
  @NoArgsConstructor
  public static class VosData {
    private Collection<BalanceData> balances;
    private double totalAssetOfBtc;
  }

  @Data
  public static class BalanceData {
    private String asset;
    private double free;
    private double locked;
  }
}
