package com.simplebank.supersimplestocks.component;

import org.testng.annotations.Test;

import com.gbce.GbceConnectivity;
import com.google.inject.Inject;
import com.simplebank.supersimplestocks.ComponentTestBase;
import com.simplebank.supersimplestocks.TestGroup;
import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.Side;

import static org.testng.Assert.assertEquals;
import static com.simplebank.supersimplestocks.SuperSimpleTestModule.TEST_EXPIRATION_TIME_IN_SECONDS;;

//Note: having a different group we can configure CI to run only unit tests
// and the component to be executed periodically or only during releases.
// Specially because this tests wait for trades to expire
@Test(groups = TestGroup.COMPONENT)
public class SuperSimpleStocksComponentTest extends ComponentTestBase {

	@Inject
	GbceConnectivity gbce;

	@Test(priority = 1)
	public void tickerPricesAndIndexAtOpeningShouldBeZero() {
		assertEquals(orderManagementSystem.tickerPrice(TEA), 0.0);
		assertEquals(orderManagementSystem.tickerPrice(POP), 0.0);
		assertEquals(orderManagementSystem.tickerPrice(ALE), 0.0);
		assertEquals(orderManagementSystem.tickerPrice(GIN), 0.0);
		assertEquals(orderManagementSystem.tickerPrice(JOE), 0.0);
		assertEquals(orderManagementSystem.gbceIndex(), 0.0);
	}

	@Test
	public void tradeExpirationTest() throws InterruptedException {
		Order order = orderManagementSystem.enterNewOrderSingle(Side.BUY, TEA, 10, 100.0);
		orderManagementSystem.execute(order);
		assertEquals(orderManagementSystem.tickerPrice(TEA), 100.0);
		Thread.sleep(TEST_EXPIRATION_TIME_IN_SECONDS * 1000);
		assertEquals(orderManagementSystem.tickerPrice(TEA), 0.0);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "FatFinger.*")
	public void fatFingerTest() {
		Order order = orderManagementSystem.enterNewOrderSingle(Side.BUY, TEA, 10, 100.0);
		orderManagementSystem.execute(order);
		Order secondOrder = orderManagementSystem.enterNewOrderSingle(Side.BUY, TEA, 10, 115.1);
		orderManagementSystem.execute(secondOrder);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "FatFinger.*")
	public void stillFatFingerBasedOnLastPriceAfterExpirationTest() throws InterruptedException {
		Order order = orderManagementSystem.enterNewOrderSingle(Side.BUY, TEA, 10, 100.0);
		Thread.sleep(TEST_EXPIRATION_TIME_IN_SECONDS * 1000);
		orderManagementSystem.execute(order);
		Order secondOrder = orderManagementSystem.enterNewOrderSingle(Side.BUY, TEA, 10, 115.1);
		assertEquals(orderManagementSystem.tickerPrice(TEA), 0.0);
		orderManagementSystem.execute(secondOrder);
	}
}
