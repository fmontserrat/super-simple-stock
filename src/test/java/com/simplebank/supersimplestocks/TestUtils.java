package com.simplebank.supersimplestocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.simplebank.supersimplestocks.fix.Side;
import com.simplebank.supersimplestocks.fix.Trade;

public class TestUtils {

	public static int[] randomQuantities(int size) {
		Random random = new Random();
		int[] quantities = new int[size];

		for (int i = 0; i < size; i++) {
			quantities[i] = random.nextInt();
		}

		return quantities;
	}

	public static Double[] randomPrices(int size) {
		Random random = new Random();
		Double[] prices = new Double[size];

		for (int i = 0; i < size; i++) {
			prices[i] = random.nextDouble();
		}

		return prices;
	}

	public static List<Trade> createListOfTrades(List<Integer> qties, List<Double> prices) {
		List<Trade> trades = new ArrayList<>();
		if (qties.size() != prices.size()) {
			throw new IllegalArgumentException("Number of qties doesn't match number of prices");
		}

		for (int i = 0; i < qties.size(); i++) {
			trades.add(new Trade(i, Side.BUY, "FAK", qties.get(i), prices.get(i)));
		}

		return trades;
	}
}
