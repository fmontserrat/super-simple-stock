package com.simplebank.supersimplestocks.md.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gbce.GbceConnectivity;
import com.google.inject.Inject;
import com.simplebank.supersimplestocks.md.MarketDataService;

public class MarketDataServiceImpl implements MarketDataService {

  private final GbceConnectivity exchangeConnectivity;

  private final Map<String, String> primaryExchange = new HashMap<String, String>();

  @Inject
  public MarketDataServiceImpl(final GbceConnectivity exchangeConnectivity){
    this.exchangeConnectivity = exchangeConnectivity;
  }
  @Inject
  public void init() {
    initPrimaryExchange(exchangeConnectivity.getTradedStocks(), GbceConnectivity.GBCE);
  }

  // All this values are delegated every time because in this example the info is local.
  // On a real exchange though, there would be a subscription to MD and the OMS would cache
  // this data
  @Override
  public double lastDividend(String ticker, String mic) {
    return exchangeConnectivity.lastDividend(ticker);
  }

  @Override
  public double lastPrice(String ticker, String mic) {
    return exchangeConnectivity.lastPrice(ticker);
  }

  @Override
  public double fixedDividend(String ticker, String mic) {
    return exchangeConnectivity.fixedDividend(ticker);
  }

  @Override
  public double parValue(String ticker, String mic) {
    return exchangeConnectivity.parValue(ticker);
  }

  @Override
  public double tickerPrice(String ticker, String mic) {
     return exchangeConnectivity.tickerPrice(ticker);
  }

  @Override
  public String primaryExchangeOf(String ticker) {
    String exchangeMic = primaryExchange.get(ticker);
    if (exchangeMic == null) {
      throw new IllegalArgumentException(String.format(
          "The stock %d is not traded in any know Exchange", ticker));
    }
    return exchangeMic;
  }

  private void initPrimaryExchange(List<String> tradedStocksInGbce, String exchangeMic) {
    for (String stock : tradedStocksInGbce) {
      primaryExchange.put(stock, exchangeMic);
    }
  }

}
