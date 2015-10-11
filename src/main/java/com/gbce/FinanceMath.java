package com.gbce;

import java.util.Collection;

import com.simplebank.supersimplestocks.fix.Trade;

public class FinanceMath {

	public static double geometricMean(final Collection<Double> values) {
		return java.lang.Math.pow(values.stream().reduce(1.0, (a, b) -> a * b), 1.0 / values.size());
	}

	public static double tickerPrice(final Collection<Trade> trades) {
		double sumPriceTimesQty = trades.stream().mapToDouble(t -> t.getPrice() * t.getQty()).sum();
		int sumQty = trades.stream().mapToInt(t -> t.getQty()).sum();
		
		System.out.println("sumPriceTimesQty " + sumPriceTimesQty);
		System.out.println("sumQty " + sumQty);
		
		return sumPriceTimesQty /sumQty;
	}
}
