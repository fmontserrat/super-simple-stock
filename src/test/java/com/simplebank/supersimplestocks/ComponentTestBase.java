package com.simplebank.supersimplestocks;

import static com.gbce.impl.DividendDataFactory.createsNewCommonStock;
import static com.gbce.impl.DividendDataFactory.createsNewPreferredStock;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.gbce.GbceAdmin;
import com.google.inject.Inject;
import com.simplebank.supersimplestocks.oms.OrderManagementSystem;

@Test(groups = TestGroup.COMPONENT)
@Guice(modules = SuperSimpleTestModule.class)
public abstract class ComponentTestBase {

	@Inject
	protected GbceAdmin gbceAdmin;
	
	@Inject
	protected OrderManagementSystem orderManagementSystem;

	public static final String TEA = "TEA";
	public static final String POP = "POP";
	public static final String ALE = "ALE";
	public static final String GIN = "GIN";
	public static final String JOE = "JOE";
	
	@BeforeClass(alwaysRun = true)
	protected void initDefaultTradedStocks(){
		gbceAdmin.addTickerToMarket(createsNewCommonStock(TEA, 0.0, 100));
		gbceAdmin.addTickerToMarket(createsNewCommonStock(POP, 8.0, 100));
		gbceAdmin.addTickerToMarket(createsNewCommonStock(ALE, 23.0, 60));
		gbceAdmin.addTickerToMarket(createsNewPreferredStock(GIN, 8.0, 0.02, 100));
		gbceAdmin.addTickerToMarket(createsNewCommonStock(JOE, 13.0, 250));
	}
}
