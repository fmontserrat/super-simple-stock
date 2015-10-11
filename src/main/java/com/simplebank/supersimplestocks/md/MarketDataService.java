package com.simplebank.supersimplestocks.md;

/**
 * Market data operations from the exchanges and MD providers
 * 
 * @author Francesc Montserrat
 *
 */
public interface MarketDataService {
  
  public double lastDividend(String ticker, String mic);
  
  public double lastPrice(String ticker, String mic);

  public double fixedDividend(String ticker, String mic);

  public double parValue(String ticker, String mic);
  
  public double tickerPrice(String ticker, String mic);

  public String primaryExchangeOf(String ticker);
}
