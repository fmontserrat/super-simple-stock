package com.simplebank.supersimplestocks;

import org.testng.annotations.DataProvider;

import static com.simplebank.supersimplestocks.fix.SecurityType.CS;
import static com.simplebank.supersimplestocks.fix.SecurityType.PS;

public class TickDataProviders {

  public static final String DIVIDEND_DATA_PROVIDER = "DividendDataProvider";

  @DataProvider(name = DIVIDEND_DATA_PROVIDER)
  public static Object[][] dividendDataProvider() {
    /*
     * String ticker, SecurityType securityType, double lastDividend, double fixedDividend, double
     * parValue, double tickerPrice, double expectedDividendYield, double expectedPeRatio
     */
    return new Object[][] { 
        {"TEA", CS, 0, 0, 100, 50, 0, Double.POSITIVE_INFINITY},
        {"POP", CS, 8, 0, 100, 50, 0.16, 6.25},
        {"ALE", CS, 23, 0, 60, 100, 0.23, 4.3478260869565215},
        {"GIN", PS, 8, 0.02, 100, 130, 0.015384615384615385, 16.25},
        {"JOE", CS, 13, 0, 250, 110, 0.11818181818181818, 8.461538461538462}};
  }
}
