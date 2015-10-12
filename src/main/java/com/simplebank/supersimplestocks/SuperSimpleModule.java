package com.simplebank.supersimplestocks;

import com.gbce.GbceAdmin;
import com.gbce.GbceConnectivity;
import com.gbce.impl.GbceExchange;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.simplebank.supersimplestocks.md.MarketDataService;
import com.simplebank.supersimplestocks.md.impl.MarketDataServiceImpl;
import com.simplebank.supersimplestocks.oms.OrderManagementSystem;
import com.simplebank.supersimplestocks.oms.impl.SimpleOrderManagementSystem;
import com.simplebank.supersimplestocks.sd.StaticDataService;
import com.simplebank.supersimplestocks.sd.impl.StaticDataServiceImpl;

/**
 * Simple binding for DI
 * 
 * @author Francesc Montserrat
 *
 */
public class SuperSimpleModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(OrderManagementSystem.class).to(SimpleOrderManagementSystem.class).in(Singleton.class);
		binder.bind(StaticDataService.class).to(StaticDataServiceImpl.class).in(Singleton.class);
		binder.bind(MarketDataService.class).to(MarketDataServiceImpl.class).in(Singleton.class);
		binder.bind(GbceExchange.class).in(Singleton.class);
		binder.bind(GbceConnectivity.class).to(GbceExchange.class);
		binder.bind(GbceAdmin.class).to(GbceExchange.class);
		bindProperties(binder);
	}

	protected void bindProperties(Binder binder) {
		// this could be external or simply a property (but requirements state to keep data in memory, no disk)
		binder.bindConstant().annotatedWith(Names.named(GbceExchange.TRADE_EXPIRATION_TIME_IN_SECONDS)).to(15 * 60);
		binder.bindConstant().annotatedWith(Names.named(GbceExchange.TRADE_CACHE_CLEANUP_FREQUENCY_IN_MS)).to(100);
	}

}
