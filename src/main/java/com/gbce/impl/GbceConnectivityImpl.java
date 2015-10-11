package com.gbce.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.gbce.FinanceMath;
import com.gbce.GbceConnectivity;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Trade;

public class GbceConnectivityImpl implements GbceConnectivity {

	private static final int EXPIRATION_TIME_IN_MINUTES = 15;

	private int exchangeTradeId = 0;
	private Set<String> tradedStocks = new HashSet<String>();
	private final Map<String, Cache<Integer, Trade>> tradeHistory = new HashMap<>();
	private final Map<String, Double> tickerPrices = new HashMap<>();
	private final Map<String, Double> lastPrices = new HashMap<>();

	private Map<String, DividendData> marketHistoricalData = new HashMap<>();

	@Override
	public Trade sendToMarket(Order order) {
		Trade trade = new Trade(++exchangeTradeId, order.getSide(), order.getTicker(), order.getQty(),
				order.getPrice());
		recordTrade(trade);
		return trade;
	}

	@Override
	public double lastPrice(String ticker) {
		verifyIfTraded(ticker);
		return lastPrices.get(ticker);
	}

	@Override
	public double lastDividend(String ticker) {
		verifyIfTraded(ticker);
		return marketHistoricalData.get(ticker).getLastDividend();
	}

	@Override
	public double fixedDividend(String ticker) {
		verifyIfTraded(ticker);
		return marketHistoricalData.get(ticker).getFixedDividend();
	}

	@Override
	public int parValue(String ticker) {
		verifyIfTraded(ticker);
		return marketHistoricalData.get(ticker).getParValue();
	}

	@Override
	public SecurityType obtainSecurityType(String ticker) {
		verifyIfTraded(ticker);
		return marketHistoricalData.get(ticker).getSecurityType();
	}

	@Override
	public double GbceIndex() {

		for (String ticker : getTradedStocks()) {
			tickerPrice(ticker);
		}

		return FinanceMath.geometricMean(tickerPrices.values());
	}

	@Override
	public double tickerPrice(String ticker) {
		verifyIfTraded(ticker);
		Map<Integer, Trade> tradeCache = this.tradeHistory.get(ticker).asMap();

		// NOTE: Using a view for performance reasons instead of copying
		// the map (receiver must guarantee not modifying the data)
		double tickerPrice = tradeCache.size() == 0 ? 0 : FinanceMath.tickerPrice(tradeCache.values());
		tickerPrices.put(ticker, tickerPrice);
		return tickerPrice;
	}

	@Override
	public List<String> getTradedStocks() {
		return ImmutableList.copyOf(tradedStocks);
	}

	private void recordTrade(Trade trade) {
		lastPrices.put(trade.getTicker(), trade.getPrice());
		tradeHistory.get(trade.getTicker()).put(trade.getId(), trade);
	}

	private Cache<Integer, Trade> buildTradeCache() {
		return CacheBuilder.newBuilder().expireAfterWrite(EXPIRATION_TIME_IN_MINUTES, TimeUnit.MINUTES).build();
	}

	private void verifyIfTraded(String ticker) {
		if (!getTradedStocks().contains(ticker)) {
			throw new IllegalArgumentException(String.format("The stock %s is not traded in %s", ticker, GBCE));
		}
	}

	@Override
	public void addTickerToMarket(DividendData dividendData) {
		String ticker = dividendData.getTicker();
		marketHistoricalData.put(ticker, dividendData);
		tradedStocks.add(ticker);
		tradeHistory.put(ticker, buildTradeCache());
		tickerPrices.put(ticker, 0.0);
		lastPrices.put(ticker, 0.0);
	}
}
