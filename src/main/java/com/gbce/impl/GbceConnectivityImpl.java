package com.gbce.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.gbce.GbceConnectivity;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Trade;

public class GbceConnectivityImpl implements GbceConnectivity {

  private final Map<String, Queue<Trade>> tradeHistory = new HashMap<>();

  @Override
  public Trade sendToMarket(Order order) {
    Trade trade = new Trade(order.getSide(), order.getTicker(), order.getQty(), order.getPrice());
    recordTrade(trade);
    return trade;
  }

  public void init() {
    for (String stock : getTradedStocks()) {
      tradeHistory.put(stock, new LinkedList<Trade>());
    }
  }

  @Override
  public List<String> getTradedStocks() {
    return Arrays.asList(new String[] {"TEA", "POP", "ALE", "GIN", "JOE"});
  }

  @Override
  public double lastDividend(String ticker) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double lastPrice(String ticker) {
    verifyIfTraded(ticker);
    return tradeHistory.get(ticker).peek().getPrice();
  }

  private void verifyIfTraded(String ticker) {
    if (!getTradedStocks().contains(ticker)) {
      throw new IllegalArgumentException(String.format("The stock %s is not traded in %ss", ticker,
          GBCE));
    }
  }

  @Override
  public double fixedDividend(String ticker) {
    throw new UnsupportedOperationException("Not yet implememted");
  }

  @Override
  public double parValue(String ticker) {
    throw new UnsupportedOperationException("Not yet implememted");
  }

  @Override
  public SecurityType obtainSecurityType(String ticker) {
    throw new UnsupportedOperationException("Not yet implememted");
  }

  @Override
  public double tickerPrice(String ticker) {
    throw new UnsupportedOperationException("Not yet implememted");
  }

  private void recordTrade(Trade trade) {
    tradeHistory.get(trade.getTicker()).add(trade);
  }
}
