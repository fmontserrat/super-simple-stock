package com.simplebank.supersimplestocks.md.impl;

import java.util.Arrays;

import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gbce.GbceConnectivity;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class MarketDataServiceImplTest {

  @Mock
  private GbceConnectivity gcConnectivity;

  private MarketDataServiceImpl marketDataService;

  @BeforeClass
  public void setup() {
    initMocks(this);
    when(gcConnectivity.getTradedStocks()).thenReturn(
        Arrays.asList(new String[] {"TEA", "POP", "ALE", "GIN", "JOE"}));
    marketDataService = new MarketDataServiceImpl(gcConnectivity);
    marketDataService.init();
  }

  @Test
  public void validPrimaryExchange() {
    String primaryExchange = marketDataService.primaryExchangeOf("TEA");
    assertEquals(primaryExchange, GbceConnectivity.GBCE);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void invalidPrimaryExchange() {
    marketDataService.primaryExchangeOf("FAK");
  }
}
