package com.simplebank.supersimplestocks;

import com.gbce.impl.GbceExchange;
import com.google.inject.Binder;
import com.google.inject.name.Names;

public class SuperSimpleTestModule extends SuperSimpleModule {

	public static final int TEST_EXPIRATION_TIME_IN_SECONDS = 3;

	@Override
	public void bindProperties(Binder binder) {
		binder.bindConstant().annotatedWith(Names.named(GbceExchange.TRADE_EXPIRATION_TIME_IN_SECONDS))
				.to(TEST_EXPIRATION_TIME_IN_SECONDS);
		binder.bindConstant().annotatedWith(Names.named(GbceExchange.TRADE_CACHE_CLEANUP_FREQUENCY_IN_MS)).to(100);
	}
}
