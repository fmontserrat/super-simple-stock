package com.simplebank.supersimplestocks.oms.impl;

import java.util.Date;

import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gbce.GbceConnectivity;
import com.simplebank.supersimplestocks.TickDataProviders;
import com.simplebank.supersimplestocks.fix.OrdStatus;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Side;
import com.simplebank.supersimplestocks.fix.Trade;
import com.simplebank.supersimplestocks.md.MarketDataService;
import com.simplebank.supersimplestocks.oms.OrderManagementSystem;
import com.simplebank.supersimplestocks.sd.StaticDataService;

import static com.simplebank.supersimplestocks.TickDataProviders.DIVIDEND_DATA_PROVIDER;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SimpleOrderManagementSystemTest {

  @Mock
  private MarketDataService marketDataService;

  @Mock
  private StaticDataService staticDataService;

  @Mock
  private GbceConnectivity gbceConnectivity;

  private OrderManagementSystem simpleOrderManagementSystem;

  @BeforeClass
  public void setup() {
    initMocks(this);
    simpleOrderManagementSystem =
        new SimpleOrderManagementSystem(marketDataService, staticDataService, gbceConnectivity);
    when(marketDataService.primaryExchangeOf(anyString())).thenReturn(GbceConnectivity.GBCE);
  }

  @Test(dataProvider = DIVIDEND_DATA_PROVIDER, dataProviderClass = TickDataProviders.class)
  public void calculateDividendYield(String ticker, SecurityType securityType, double lastDividend,
      double fixedDividend, double parValue, double tickerPrice, double expectedDividendYield,
      double expectedPeRatio) {

    when(marketDataService.lastDividend(ticker, GbceConnectivity.GBCE)).thenReturn(lastDividend);
    when(marketDataService.fixedDividend(ticker, GbceConnectivity.GBCE)).thenReturn(fixedDividend);
    when(marketDataService.parValue(ticker, GbceConnectivity.GBCE)).thenReturn(parValue);
    when(marketDataService.tickerPrice(ticker, GbceConnectivity.GBCE)).thenReturn(tickerPrice);
    when(staticDataService.securityType(ticker)).thenReturn(securityType);

    double dividendYield = simpleOrderManagementSystem.calculateDividendYield(ticker);

    assertEquals(dividendYield, expectedDividendYield);
  }

  @Test
  public void calculeDividendYieldWithoutTickerPriceMeansInfinity() {
    // In a real exchange we have an opening price and this never happens
    // but here we just verify that the system doesn't crash
    // the first trade will take care to give a real ticker price
    String ticker = "TEA";

    when(marketDataService.lastDividend(ticker, GbceConnectivity.GBCE)).thenReturn(10.0);
    when(marketDataService.tickerPrice(ticker, GbceConnectivity.GBCE)).thenReturn(0.0);
    when(staticDataService.securityType(ticker)).thenReturn(SecurityType.CS);

    double dividendYield = simpleOrderManagementSystem.calculateDividendYield(ticker);

    assertEquals(dividendYield, Double.POSITIVE_INFINITY);
  }

  @Test(dataProvider = DIVIDEND_DATA_PROVIDER, dataProviderClass = TickDataProviders.class)
  public void calculatePeRatio(String ticker, SecurityType securityType, double lastDividend,
      double fixedDividend, double parValue, double tickerPrice, double expectedDividendYield,
      double expectedPeRatio) {

    when(marketDataService.lastDividend(ticker, GbceConnectivity.GBCE)).thenReturn(lastDividend);
    when(marketDataService.tickerPrice(ticker, GbceConnectivity.GBCE)).thenReturn(tickerPrice);
    when(staticDataService.securityType(ticker)).thenReturn(securityType);

    double peRatio = simpleOrderManagementSystem.calculatePERatio(ticker);

    assertEquals(peRatio, expectedPeRatio);
  }

  @Test
  public void enterNewOrderValid() {
    Order order = simpleOrderManagementSystem.enterNewOrderSingle(Side.BUY, "TEA", 10, 100.0);
    assertEquals(order.getOrdStatus(), OrdStatus.NEW);
    assertEquals(order.getSide(), Side.BUY);
    assertEquals(order.getPrice(), 100.0);
    assertEquals(order.getQty(), 10);
    assertEquals(order.getTicker(), "TEA");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void enterNewOrderInvalidPrice() {
    simpleOrderManagementSystem.enterNewOrderSingle(Side.BUY, "TEA", 10, -100.0);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void enterNewOrderInvalidQty() {
    simpleOrderManagementSystem.enterNewOrderSingle(Side.BUY, "TEA", 0, 100.0);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void enterNewOrderInvalidTicker() {
    simpleOrderManagementSystem.enterNewOrderSingle(Side.BUY, "TOOLONG", 10, 100.0);
  }

  @Test
  public void executeTrade() {
    Order order = simpleOrderManagementSystem.enterNewOrderSingle(Side.BUY, "GIN", 10, 112.0);
    when(gbceConnectivity.sendToMarket(order)).thenReturn(new Trade(Side.BUY, "GIN", 10, 112.0));

    Date aboutToTrade = new Date();
    Trade trade = simpleOrderManagementSystem.execute(order);
    Date afterTrading = new Date();

    assertTrue(trade.getTradeDate().equals(aboutToTrade)
        || trade.getTradeDate().after(aboutToTrade));

    assertTrue(trade.getTradeDate().equals(afterTrading)
        || trade.getTradeDate().before(afterTrading));
    
    assertEquals(trade.getTicker(), "GIN");
    assertEquals(trade.getPrice(), 112.0);
    assertEquals(trade.getQty(), 10);
    assertEquals(trade.getSide(), Side.BUY);

    assertEquals(order.getOrdStatus(), OrdStatus.FILLED);
  }
}
