package com.gbce.impl;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.gbce.FinanceMath;
import com.gbce.GbceConnectivity;
import com.simplebank.supersimplestocks.TestGroup;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.Side;
import com.simplebank.supersimplestocks.fix.Trade;

import static org.testng.Assert.assertEquals;
import static com.gbce.impl.DividendDataFactory.createsNewCommonStock;
import static com.gbce.impl.DividendDataFactory.createsNewPreferredStock;
import static com.simplebank.supersimplestocks.TestUtils.randomQuantities;
import static com.simplebank.supersimplestocks.TestUtils.randomPrices;

import java.util.Arrays;
import java.util.List;

@Test(groups = TestGroup.UNIT)
public class GbceConnectivityImplTest {

	GbceConnectivity cbceConnectivity;

	@BeforeMethod
	public void reset() {
		cbceConnectivity = new GbceConnectivityImpl();
		cbceConnectivity.addTickerToMarket(createsNewCommonStock("TEA", 0.0, 100));
		cbceConnectivity.addTickerToMarket(createsNewCommonStock("POP", 8.0, 100));
		cbceConnectivity.addTickerToMarket(createsNewCommonStock("ALE", 23.0, 60));
		cbceConnectivity.addTickerToMarket(createsNewPreferredStock("GIN", 8.0, 0.02, 100));
		cbceConnectivity.addTickerToMarket(createsNewCommonStock("JOE", 13.0, 250));
	}

	@Test
	public void tradeToMarketTest() {
		String ticker = "POP";
		Order order = new Order(Side.BUY, ticker, 10, 20.0);
		Trade trade = cbceConnectivity.sendToMarket(order);
		assertEquals(cbceConnectivity.lastPrice(ticker), trade.getPrice());
		assertEquals(cbceConnectivity.tickerPrice(ticker), trade.getPrice());
	}

	@Test
	public void threeTradesToMarketTest() {
		String ticker = "TEA";
		Order order1 = new Order(Side.SELL, ticker, 10, 20.0);
		Order order2 = new Order(Side.SELL, ticker, 30, 21.0);
		Order order3 = new Order(Side.SELL, ticker, 25, 20.5);
		cbceConnectivity.sendToMarket(order1);
		cbceConnectivity.sendToMarket(order2);
		Trade trade3 = cbceConnectivity.sendToMarket(order3);

		assertEquals(cbceConnectivity.lastPrice(ticker), trade3.getPrice());
		assertEquals(cbceConnectivity.tickerPrice(ticker), 20.653846153846153);
	}

	@Test
	public void calculateIndexWithTwoTickersIsZero() {
		String ticker1 = "TEA";
		String ticker2 = "ALE";
		Order order1 = new Order(Side.SELL, ticker1, 10, 20.0);
		Order order2 = new Order(Side.SELL, ticker1, 30, 21.0);
		Order order3 = new Order(Side.SELL, ticker1, 25, 20.5);
		cbceConnectivity.sendToMarket(order1);
		cbceConnectivity.sendToMarket(order2);
		cbceConnectivity.sendToMarket(order3);

		Order order4 = new Order(Side.BUY, ticker2, 12, 36.3);
		Order order5 = new Order(Side.SELL, ticker2, 30, 36.2);
		Order order6 = new Order(Side.BUY, ticker2, 55, 35.9);
		cbceConnectivity.sendToMarket(order4);
		cbceConnectivity.sendToMarket(order5);
		cbceConnectivity.sendToMarket(order6);

		assertEquals(cbceConnectivity.tickerPrice(ticker1), 20.653846153846153);
		assertEquals(cbceConnectivity.tickerPrice(ticker2), 36.042268041237115);
		assertEquals(cbceConnectivity.GbceIndex(), 0.0);
	}

	@Test
	public void calculateIndexWithAllTickers() {

		List<String> tradedStocks = cbceConnectivity.getTradedStocks();
		int[] quantities = randomQuantities(tradedStocks.size());
		Double[] prices = randomPrices(tradedStocks.size());

		for (int i = 0; i < tradedStocks.size(); i++) {
			Order order = new Order(Side.SELL, tradedStocks.get(i), quantities[i], prices[i]);
			cbceConnectivity.sendToMarket(order);
		}

		assertEquals(cbceConnectivity.GbceIndex(), FinanceMath.geometricMean(Arrays.asList(prices)), 0.000000001);
	}
}
