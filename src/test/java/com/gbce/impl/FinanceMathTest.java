package com.gbce.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.gbce.FinanceMath;
import com.simplebank.supersimplestocks.TestGroup;

import static org.testng.Assert.assertEquals;
import static com.simplebank.supersimplestocks.TestUtils.createListOfTrades;

@Test(groups = TestGroup.UNIT)
public class FinanceMathTest {

	public static final String GEOMETRIC_MEAN_DATA_PROVIDER = "GeometricMeanDataProvider";

	public static final String QTY_AND_PRICES_DATA_PROVIDER = "QtyAndPricesDataProvider";

	@DataProvider(name = GEOMETRIC_MEAN_DATA_PROVIDER)
	public static Object[][] geometriMeanDataProider() {
		return new Object[][] { { Arrays.asList(new Double[] { 1.0, 2.0, 3.0 }), 1.8171205928321397 },
				{ Arrays.asList(new Double[] { 5.0, 20.0, 40.0, 80.0, 100.0 }), 31.697863849222276 },
				{ Arrays.asList(new Double[] { 1.3, 5.6, 90.8, 328.3, 2.0 }), 13.41231118846773 },
				{ Arrays.asList(new Double[] { 1.3, 5.6, 90.8, 328.3, 2.0, 0.5, 1.2, 3.5, 666.4, 30.1 }),
						10.621941090783023 },
				{ Arrays.asList(new Double[] { 61.0, 84.0, 41.0, 97.0, 46.0, 67.0, 5.0, 99.0, 65.0, 85.0, 90.0, 79.0,
						94.0, 63.0, 25.0, 6.0, 7.0, 56.0, 17.0, 3.0, 52.0, 40.0, 84.0, 72.0, 23.0, 20.0 }),
						38.0817970275889 } };
	}

	@DataProvider(name = QTY_AND_PRICES_DATA_PROVIDER)
	public static Object[][] doubleValuesDataProvider() {
		return new Object[][] {
				{ Arrays.asList(new Integer[] { 1, 3, 4 }), Arrays.asList(new Double[] { 1.0, 2.0, 3.0 }), 2.375 },
				{ Arrays.asList(new Integer[] { 10, 20, 150 }), Arrays.asList(new Double[] { 2.0, 2.0, 2.0 }), 2 },
				{ Arrays.asList(new Integer[] { 30, 30, 30 }), Arrays.asList(new Double[] { 3.0, 3.0, 3.0 }), 3 },
				{ Arrays.asList(new Integer[] { 60, 44, 60 }), Arrays.asList(new Double[] { 45.2, 45.3, 45.3 }),
						45.26341463414634 } };
	}

	@Test(dataProvider = GEOMETRIC_MEAN_DATA_PROVIDER, dataProviderClass = FinanceMathTest.class)
	public void geometricMean(Collection<Double> values, double expectedGeometricMean) {
		assertEquals(FinanceMath.geometricMean(values), expectedGeometricMean);
	}

	@Test(dataProvider = QTY_AND_PRICES_DATA_PROVIDER, dataProviderClass = FinanceMathTest.class)
	public void tickerPrice(List<Integer> qties, List<Double> prices, double expectedTickPrice) {
		double tickerPrice = FinanceMath.tickerPrice(createListOfTrades(qties, prices));
		assertEquals(tickerPrice, expectedTickPrice);
	}
}
