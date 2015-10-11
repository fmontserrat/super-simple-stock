package com.gbce.impl;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.gbce.FinanceMath;
import com.gbce.GbceConnectivity;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.fix.Trade;

import static com.simplebank.supersimplestocks.fix.SecurityType.CS;
import static com.simplebank.supersimplestocks.fix.SecurityType.PS;

public class GbceConnectivityImpl implements GbceConnectivity {

	private final Map<String, Deque<Trade>> tradeHistory = new HashMap<>();
	private final Map<String, Double> tickerPrices = new HashMap<>();

	private Map<String, TradeHistoricalData> marketHistoricalData;

	@Override
	public Trade sendToMarket(Order order) {
		Trade trade = new Trade(order.getSide(), order.getTicker(), order.getQty(), order.getPrice());
		recordTrade(trade);
		return trade;
	}

	public void init() {
		for (String ticker : getTradedStocks()) {
			tradeHistory.put(ticker, new LinkedList<Trade>());
		}
		loadDefaultHistoricalData();
	}

	@Override
	public List<String> getTradedStocks() {
		// TODO defaults somewhere
		return Arrays.asList(new String[] { "TEA", "POP", "ALE", "GIN", "JOE" });
	}

	@Override
	public double lastPrice(String ticker) {
		verifyIfTraded(ticker);
		return tradeHistory.get(ticker).getLast().getPrice();
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
		return FinanceMath.geometricMean(tickerPrices.values());
	}

	@Override
	public double tickerPrice(String ticker) {
		verifyIfTraded(ticker);
		return FinanceMath.tickerPrice(last15minOf(this.tradeHistory.get(ticker)));
	}

	private Deque<Trade> last15minOf(Deque<Trade> trades) {
		//TODO filter
		return trades;
	}

	private void verifyIfTraded(String ticker) {
		if (!getTradedStocks().contains(ticker)) {
			throw new IllegalArgumentException(String.format("The stock %s is not traded in %s", ticker, GBCE));
		}
	}

	private void recordTrade(Trade trade) {
		tradeHistory.get(trade.getTicker()).add(trade);
	}

	void enterDividendStaticData(String ticker, TradeHistoricalData tradeHistoricalData) {
		marketHistoricalData.put(ticker, tradeHistoricalData);
	}

	void loadDefaultHistoricalData() {
		// TODO load from somewhere else and make factory
		marketHistoricalData.put("TEA", new TradeHistoricalData(CS, 0, 0, 100));
		marketHistoricalData.put("POP", new TradeHistoricalData(CS, 8, 0, 100));
		marketHistoricalData.put("ALE", new TradeHistoricalData(CS, 23, 0, 60));
		marketHistoricalData.put("GIN", new TradeHistoricalData(PS, 8, 0.02, 100));
		marketHistoricalData.put("JOE", new TradeHistoricalData(CS, 13, 0, 250));
	}

	private class TradeHistoricalData {

		private final int parValue;
		private final double fixedDividend, lastDividend;
		private final SecurityType securityType;

		public TradeHistoricalData(SecurityType securityType, double lastDividend, double fixedDividend, int parValue) {
			this.parValue = parValue;
			this.fixedDividend = fixedDividend;
			this.lastDividend = lastDividend;
			this.securityType = securityType;
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

	}
}
