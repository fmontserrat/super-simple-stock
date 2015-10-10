package com.simplebank.supersimplestocks.fix;


/**
 * Super simple fix-based order 
 * (no FIX library used for simplicity and to keep the project lightweight)
 * 
 * All orders are Limit orders
 * 
 * @author Francesc Montserrat
 *
 */
public class Order {

  private OrdStatus ordStatus;
  private final int qty;
  private final Side side;
  private final double price;
  private final String ticker;
  
  public Order(Side side, String ticker, int qty, double price){
    this.ticker = ticker;
    this.side = side;
    this.qty = qty;
    this.price = price;
    this.ordStatus = OrdStatus.NEW;
  }

  public String getTicker() {
    return ticker;
  }
  
  public OrdStatus getOrdStatus() {
    return ordStatus;
  }

  public void setOrdStatus(OrdStatus ordStatus) {
    this.ordStatus = ordStatus;
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
}
