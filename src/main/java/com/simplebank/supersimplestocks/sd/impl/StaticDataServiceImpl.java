package com.simplebank.supersimplestocks.sd.impl;

import com.gbce.GbceConnectivity;
import com.google.inject.Inject;
import com.simplebank.supersimplestocks.fix.SecurityType;
import com.simplebank.supersimplestocks.sd.StaticDataService;

public class StaticDataServiceImpl implements StaticDataService {

  private final GbceConnectivity gbceConnectivity;

  @Inject
  public StaticDataServiceImpl(final GbceConnectivity gbceConnectivity) {
    this.gbceConnectivity = gbceConnectivity;
  }

  /*
   * Delegated to exchange for simplicity
   */
  @Override
  public SecurityType securityType(String ticker) {
    return gbceConnectivity.obtainSecurityType(ticker);
  }
}
