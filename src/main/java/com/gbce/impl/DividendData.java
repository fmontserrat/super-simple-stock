package com.gbce.impl;

import com.simplebank.supersimplestocks.fix.SecurityType;

public class DividendData {

	private final String ticker;
	private final int parValue;
	private final double fixedDividend, lastDividend;
	private final SecurityType securityType;

	DividendData(String ticker, SecurityType securityType, double lastDividend, double fixedDividend, int parValue) {
		this.ticker = ticker;
		this.parValue = parValue;
		this.fixedDividend = fixedDividend;
		this.lastDividend = lastDividend;
		this.securityType = securityType;
	}

	public String getTicker() {
		return ticker;
	}

	public int getParValue() {
		return parValue;
	}

	public double getFixedDividend() {
		return fixedDividend;
	}

	public double getLastDividend() {
		return lastDividend;
	}

	public SecurityType getSecurityType() {
		return securityType;
	}

	@Override
	public String toString() {
		return "DividendData [ticker=" + ticker + ", parValue=" + parValue + ", fixedDividend=" + fixedDividend
				+ ", lastDividend=" + lastDividend + ", securityType=" + securityType + "]";
	}
}
