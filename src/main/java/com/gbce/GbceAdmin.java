package com.gbce;

import com.gbce.impl.DividendData;

/**
 * Interface to manage the market. Mainly to add and modify traded stocks.
 * 
 * @author root
 *
 */
public interface GbceAdmin extends GbceConnectivity {

	  /**
	   * Stock will be added to the market and can be traded from now on
	   * 
	   * dividendData The static dividend data for the stock
	   */
	  public void addTickerToMarket(DividendData dividendData);
}
