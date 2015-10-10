package com.simplebank.supersimplestocks.fix;

import java.util.Date;

/**
 * Super simple fix-based trade (no FIX library used for simplicity and to keep the project
 * lightweight)
 * 
 * @author Francesc Montserrat
 * 
 */
public class Trade {

  private final int qty;
  private final Side side;
  private final double price;
  private final String ticker;

  public Trade(Side side, String ticker, int qty, double price) {
    this.side = side;
    this.ticker = ticker;
    this.qty = qty;
    this.price = price;
    this.tradeDate = new Date();
  }

  private final Date tradeDate;

  public Date getTradeDate() {
    return tradeDate;
  }

  public int getQty() {
    return qty;
  }

  public Side getSide() {
    return side;
  }

  public double getPrice() {
    return price;
  }

  public String getTicker() {
    return ticker;
  }
}
