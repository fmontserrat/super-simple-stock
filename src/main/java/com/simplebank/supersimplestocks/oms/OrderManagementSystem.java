package com.simplebank.supersimplestocks.oms;

import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.Side;
import com.simplebank.supersimplestocks.fix.Trade;

/**
 * SimpleBank's top tier OMS.
 * 
 * SimpleBank guarantees unlimited liquidity and therefore SB OMS always executes your orders!
 * 
 * SB is so simple that will get you exactly the price you want. NBBO? There's no such thing on
 * Super Simple trading!
 * 
 * 
 * @author Francesc Montserrat
 * 
 */
public interface OrderManagementSystem {

  /**
   * Create a simple new order:
   * 
   * example: BUY 150 TEA @ 100.00
   * 
   * @return The new order
   */
  public Order enterNewOrderSingle(Side side, String ticker, int qty, double price);

  /**
   * Send to market. Full fill is guaranteed.
   * 
   * Exact price is also guaranteed, it won't be improved.
   * 
   * @param A new order (not filled)
   * 
   * @return The executed trade
   */
  public Trade execute(Order order);
  
  /**
   * http://www.investopedia.com/terms/d/dividendyield.asp
   * 
   * @param ticker The stock which dividend yield is requested
   * @return Dividend yield
   */
  public double calculateDividendYield(String ticker);
  
  /**
   * http://www.investopedia.com/terms/p/price-earningsratio.asp
   * 
   * @param ticker The stock which P/E ratio is requested
   * @return P/E Ratio
   */
  public double calculatePERatio(String ticker);
}
