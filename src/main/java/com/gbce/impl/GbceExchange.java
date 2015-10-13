package com.gbce.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.gbce.FinanceMath;
import com.gbce.GbceAdmin;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Trade;

/**
 * Global Beverage Corporation Exchange
 * 
 * @author Francesc Montserrat
 *
 */
public class GbceExchange implements GbceAdmin {

	public static final String TRADE_EXPIRATION_TIME_IN_SECONDS = "tradeExpirationTimeInSeconds";

	public static final String TRADE_CACHE_CLEANUP_FREQUENCY_IN_MS = "tradeCacheCleanupFrequencyInMs";

	@Inject
	@Named(TRADE_EXPIRATION_TIME_IN_SECONDS)
	private int tradeExpirationTime;

	@Inject
	@Named(TRADE_CACHE_CLEANUP_FREQUENCY_IN_MS)
	private int tradeCacheCleanupFrequencyInMs;

	private int exchangeTradeId = 0;
	private Set<String> tradedStocks = new HashSet<String>();
	private final Map<String, Cache<Integer, Trade>> tradeHistory = new HashMap<>();
	private final Map<String, Double> tickerPrices = new HashMap<>();
	private final Map<String, Double> lastPrices = new HashMap<>();

	private Map<String, DividendData> marketHistoricalData = new HashMap<>();

	private ScheduledExecutorService scheduler;

	@Inject
	public void init() {
		scheduler = Executors.newScheduledThreadPool(1);

		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				for (Cache<Integer, Trade> cache : tradeHistory.values()) {
					cache.cleanUp();
				}
			}
		}, tradeCacheCleanupFrequencyInMs, tradeCacheCleanupFrequencyInMs, TimeUnit.MILLISECONDS);
	}

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
	public double gbceIndex() {

		for (String ticker : getTradedStocks()) {
			tickerPrice(ticker);
		}

		return FinanceMath.geometricMean(tickerPrices.values());
	}

	@Override
	public double tickerPrice(String ticker) {
		verifyIfTraded(ticker);

		Cache<Integer, Trade> cache = this.tradeHistory.get(ticker);

		// very tiny cleanup, either it happened in recent schedule or the cache
		// was auto cleaned in insertions (if there's liquidity)
		// in any case, this is a delta cleanup
		cache.cleanUp();

		// Using a view for performance reasons instead of copying
		// the map (receiver must guarantee not modifying the data)
		Map<Integer, Trade> tradeCache = cache.asMap();

		// Calculation of both tickerPrice and index could also be scheduled to happen on every insertion
		// or expiration of trades. To be considered if market data is published to the clients
		// (keeping it synchronous for the first version)
		double tickerPrice = tradeCache.size() == 0 ? 0 : FinanceMath.tickerPrice(tradeCache.values());
		tickerPrices.put(ticker, tickerPrice);
		return tickerPrice;
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

	@Override
	public List<String> getTradedStocks() {
		return ImmutableList.copyOf(tradedStocks);
	}

	private void recordTrade(Trade trade) {
		lastPrices.put(trade.getTicker(), trade.getPrice());
		tradeHistory.get(trade.getTicker()).put(trade.getId(), trade);
	}

	private Cache<Integer, Trade> buildTradeCache() {
		return CacheBuilder.newBuilder().expireAfterWrite(tradeExpirationTime, TimeUnit.SECONDS).build();
	}

	private void verifyIfTraded(String ticker) {
		if (!getTradedStocks().contains(ticker)) {
			throw new IllegalArgumentException(String.format("The stock %s is not traded in %s", ticker, GBCE));
		}
	}

	void setTradeExpirationTime(int tradeExpirationTime) {
		this.tradeExpirationTime = tradeExpirationTime;
	}

	void setTradeCacheCleanupFrequencyInMs(int tradeCacheCleanupFrequencyInMs) {
		this.tradeCacheCleanupFrequencyInMs = tradeCacheCleanupFrequencyInMs;
	}
	
	
}
