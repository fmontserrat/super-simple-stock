package com.simplebank.supersimplestocks.oms.impl;

import com.simplebank.supersimplestocks.fix.Order;
import com.simplebank.supersimplestocks.fix.Side;
import com.simplebank.supersimplestocks.fix.Trade;
import com.simplebank.supersimplestocks.oms.OrderManagementSystem;

/**
 * Default first generation implementation of the OMS
 * 
 * @author Francesc Montserrat
 * 
 */
public class SimpleOrderManagementSystem implements OrderManagementSystem {

  public Order enterNewOrderSingle(Side side, String ticker, int qty, double price) {
    fatFingerCheck(ticker, qty);
    return new Order(side, ticker, qty, price);
  }

  public Trade execute(Order order) {
    return new Trade(order.getSide(), order.getTicker(), order.getQty(), order.getPrice());
  }


  private void fatFingerCheck(String ticker, int qty) {
    // TODO implement. FF not yet there. 
  }
}
