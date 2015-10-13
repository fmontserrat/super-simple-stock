# super-simple-stocks

A Global Beverage Corporation Exchange

### What can it do?
- Create new orders using a Super Simple Order Management System (OMS)
- Execute orders on the GBCE
- Fat Finger checks based on last price
- Add new stocks intraday
- Calculate  dividend yield, P/E ratio and ticker prices for all traded stocks
- Obtain the up to date index for the GBCE based on all traded stocks
- Supports two security types
- Execution guaranteed even without liquidity

### How to use

Build and run unit tests
```
mvn clean install
```

New tests can be quickly added to [CustomComponentTests](src/test/java/com/simplebank/supersimplestocks/component/CustomComponentTests.java)

### Some notes
- There's no opening price so ticker prices and index are 0 until there are trades or after all trades expire
- Index is calculated with all stocks in the exchange in the trading session including those where all trades expired (index would be zero)
- New intraday tickers would be included in the index

### Future features
- Market data subscription: multiple clients to consume market data in real time
- Asynchronous ticker price and index calculation on new trades and trade eviction
- Increased precision using BigDecimal
- Trading phases
- NBBO, order types, cancels and amendments
- FIX protocol
- Crossing and settlement of trades
- More beverages and perhaps some snacks!
