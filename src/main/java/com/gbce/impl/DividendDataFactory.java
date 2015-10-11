package com.gbce.impl;

import com.simplebank.supersimplestocks.fix.SecurityType;

/**
 * A simple factory for SOD dividend data for the GBCE Exchange
 * 
 * @author Francesc Montserrat
 *
 */
public class DividendDataFactory {

	public static DividendData createsNewCommonStock(String ticker, Double lastDividend, int parValue) {
		return new DividendData(ticker, SecurityType.CS, lastDividend, 0, parValue);
	}

	public static DividendData createsNewPreferredStock(String ticker, Double lastDividend, Double fixedDividend,
			int parValue) {
		return new DividendData(ticker, SecurityType.PS, lastDividend, fixedDividend, parValue);
	}
}
