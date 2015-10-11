package com.gbce;

import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Trade;
import com.simplebank.supersimplestocks.fix.Order;
import java.util.List;

/**
 * Connectivity with the Global Beverage Corporation Exchange
 * 
 * For simplicity this is contained in the same module but in a bigger project would be an external
 * library offered by the client or most realistically an agreed FIX/custom going over TCP
 * 
 * Also in a more realistic scenario MD would be consumed in an aggregated manner from an MD
 * provider and not directly from each exchange.
 * 
 * @author Francesc Montserrat
 * 
 */
public interface GbceConnectivity {

  String GBCE = "GBCE";

  /**
   * Cross an order (always!)
   * 
   * @param order The order to be sent to market
   * @return The related execution
   */
  public Trade sendToMarket(Order order);

  /**
   * Obtain the list of stocks traded in this exchange
   * 
   * @return List of stock tickers
   */
  public List<String> getTradedStocks();

  /**
   * http://www.investopedia.com/terms/d/dividend.asp
   * 
   * @param ticker The identifier of the stock
   * @return The value of the last dividend
   */
  public double lastDividend(String ticker);

  /**
   * http://www.investopedia.com/terms/m/market-price.asp
   * 
   * @param ticker The identifier of the stock
   * @return The last price at which a stock was traded
   */
  public double lastPrice(String ticker);

  /**
   * http://www.investopedia.com/terms/d/dividendrate.asp
   * 
   * @param ticker The identifier of the stock
   * @return The % value of the fixed dividend
   */
  public double fixedDividend(String ticker);

  /**
   * http://www.investopedia.com/terms/p/parvalue.asp
   * 
   * @param ticker The identifier of the stock
   * @return The par value for a give stock
   */
  public double parValue(String ticker);

  /**
   * http://www.investopedia.com/exam-guide/series-7/equities/types-stock.asp
   * 
   * @param ticker The identifier of the stock
   * @return The stock type of a given stock
   */
  public SecurityType obtainSecurityType(String ticker);

  /**
   * Ticker price calculated with simplified formula
   * 
   * @param ticker The identifier of the stock
   * @return The calculated ticker price
   */
  public double tickerPrice(String ticker);
}
