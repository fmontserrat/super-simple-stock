package com.simplebank.supersimplestocks.component;

import org.testng.annotations.Test;

import com.simplebank.supersimplestocks.ComponentTestBase;
import com.simplebank.supersimplestocks.TestGroup;

@Test(groups = TestGroup.COMPONENT)
public class CustomComponentTests extends ComponentTestBase {

	@Test
	public void customTest(){
		
		// TODO new tests here
		
		//New order
		//Order order = orderManagementSystem.enterNewOrderSingle(...)
		
		// Execute
		//orderManagementSystem.execute(order)
		
		// Default tickers are already on the market (see parent class)
		// to add new ones
		//gbceAdmin.addTickerToMarket(...);
	}
}
