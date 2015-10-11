package com.simplebank.supersimplestocks.fix;

import java.util.Date;

/**
 * Super simple fix-based trade (no FIX library used for simplicity and to keep
 * the project lightweight)
 * 
 * Trade is immutable
 * 
 * @author Francesc Montserrat
 * 
 */
public class Trade {

	private final int tradeId;
	private final Side side;
	private final int qty;
	private final String ticker;
	private final double price;

	public Trade(int tradeId, Side side, String ticker, int qty, double price) {
		this.tradeId = tradeId;
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

	public int getId() {
		return tradeId;
	}

	@Override
	public String toString() {
		return "Trade [tradeId=" + tradeId + ", side=" + side + ", qty=" + qty + ", ticker=" + ticker + ", price="
				+ price + ", tradeDate=" + tradeDate + "]";
	}
}
