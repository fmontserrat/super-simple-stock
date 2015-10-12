package com.simplebank.supersimplestocks.md.impl;

import com.gbce.GbceConnectivity;
import com.google.inject.Inject;
import com.simplebank.supersimplestocks.md.MarketDataService;

public class MarketDataServiceImpl implements MarketDataService {

	private final GbceConnectivity exchangeConnectivity;

	@Inject
	public MarketDataServiceImpl(final GbceConnectivity exchangeConnectivity) {
		this.exchangeConnectivity = exchangeConnectivity;
	}

	// All this values are delegated every time because in this example the info
	// is local.
	// On a real exchange though, there would be a subscription to MD and the
	// OMS would cache
	// this data
	@Override
	public double lastDividend(String ticker, String mic) {
		return exchangeConnectivity.lastDividend(ticker);
	}

	@Override
	public double lastPrice(String ticker, String mic) {
		return exchangeConnectivity.lastPrice(ticker);
	}

	@Override
	public double fixedDividend(String ticker, String mic) {
		return exchangeConnectivity.fixedDividend(ticker);
	}

	@Override
	public double parValue(String ticker, String mic) {
		return exchangeConnectivity.parValue(ticker);
	}

	@Override
	public double tickerPrice(String ticker, String mic) {
		return exchangeConnectivity.tickerPrice(ticker);
	}

	@Override
	public String primaryExchangeOf(String ticker) {

		// we could have a local cache of the traded stocks in each exchange
		// but since there is only one exchange and it's local, we can delegate
		// also since there is no opening phase, stocks can be added intraday

		if (!exchangeConnectivity.getTradedStocks().contains(ticker)) {
			throw new IllegalArgumentException(
					String.format("The stock %s is not traded in any know Exchange", ticker));
		}

		return GbceConnectivity.GBCE;
	}
}
