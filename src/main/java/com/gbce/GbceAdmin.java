package com.gbce;

import com.gbce.impl.DividendData;

/**
 * Interface to manage the market. Mainly to add and modify traded stocks.
 * 
 * It is not safe to make the exchange implementation include this methods
 * directly but this since everything is in the same module anyway, at this
 * point security is not an issue
 * 
 * @author Francesc Montserrat
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
