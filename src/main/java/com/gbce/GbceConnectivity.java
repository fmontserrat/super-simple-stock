package com.gbce;

import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Trade;
import com.simplebank.supersimplestocks.fix.Order;
import java.util.List;

/**
 * Connectivity with the Global Beverage Corporation Exchange
 * 
 * For simplicity this is contained in the same module but in a bigger project
 * would be an external library offered by the client or most realistically an
 * agreed FIX/custom going over TCP
 * 
 * Also in a more realistic scenario MD would be consumed in an aggregated manner
 * from an MD provider and not directly from each exchange.
 * 
 * @author Francesc Montserrat
 *
 */
public interface GbceConnectivity {

  String GBCE = "GBCE";
  
  public Trade sendToMarket(Order order);

  public List<String> getTradedStocks();

  public double lastDividend(String ticker);

  public double lastPrice(String ticker);

  public double fixedDividend(String ticker);

  public double parValue(String ticker);

  public SecurityType obtainSecurityType(String ticker);

  public double tickerPrice(String ticker);
}
