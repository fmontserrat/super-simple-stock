package com.simplebank.supersimplestocks.oms.impl;

import com.gbce.GbceConnectivity;
import com.google.inject.Inject;
import com.simplebank.supersimplestocks.fix.OrdStatus;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Side;
import com.simplebank.supersimplestocks.fix.Trade;
import com.simplebank.supersimplestocks.md.MarketDataService;
import com.simplebank.supersimplestocks.oms.OrderManagementSystem;
import com.simplebank.supersimplestocks.sd.StaticDataService;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Default first generation implementation of the OMS
 * 
 * @author Francesc Montserrat
 * 
 */
public class SimpleOrderManagementSystem implements OrderManagementSystem {

  private final MarketDataService marketDataService;

  private final StaticDataService staticDataService;

  private final GbceConnectivity gbceConnectivity;

  @Inject
  public SimpleOrderManagementSystem(final MarketDataService marketDataService,
      final StaticDataService staticDataService, final GbceConnectivity gbceConnectivity) {
    this.marketDataService = marketDataService;
    this.staticDataService = staticDataService;
    this.gbceConnectivity = gbceConnectivity;
  }

  @Override
  public Order enterNewOrderSingle(Side side, String ticker, int qty, double price) {
    checkArgument(ticker != null & ticker.length() == 3);
    // lot size is always 1
    checkArgument(qty > 0);
    // let fat finger check catch invalid prices
    checkArgument(price > 0);

    fatFingerCheck(ticker, qty);
    return new Order(side, ticker, qty, price);
  }

  @Override
  public Trade execute(Order order) {

    if (OrdStatus.FILLED.equals(order.getOrdStatus())) {
      throw new UnsupportedOperationException("The order is already filled");
    }

    Trade trade = gbceConnectivity.sendToMarket(order);
    order.setOrdStatus(OrdStatus.FILLED);
    return trade;
  }

  @Override
  public double calculateDividendYield(String ticker) {

    String mic = primaryExchangeFor(ticker);
    SecurityType securityType = staticDataService.securityType(ticker);
    double dividendYield;

    switch (securityType) {
      case CS:
        dividendYield = calculateDividendYieldCommonStock(ticker, mic);
        break;
      case PS:
        dividendYield = calculateDividendYieldPreferredStock(ticker, mic);
        break;
      default:
        throw new UnsupportedOperationException(String.format(
            "The SecurityType %s is not supported.", securityType));
    }

    return dividendYield;
  }

  @Override
  public double calculatePERatio(String ticker) {
    String mic = primaryExchangeFor(ticker);
    return marketDataService.tickerPrice(ticker, mic) / marketDataService.lastDividend(ticker, mic);
  }

  private double calculateDividendYieldCommonStock(String ticker, String mic) {
    return marketDataService.lastDividend(ticker, mic) / marketDataService.tickerPrice(ticker, mic);
  }

  private double calculateDividendYieldPreferredStock(String ticker, String mic) {
    return marketDataService.fixedDividend(ticker, mic) * marketDataService.parValue(ticker, mic)
        / marketDataService.tickerPrice(ticker, mic);
  }

  private void fatFingerCheck(String ticker, int qty) {
    // TODO implement. FF not yet there.
  }

  private String primaryExchangeFor(String ticker) {
    return marketDataService.primaryExchangeOf(ticker);
  }
}
