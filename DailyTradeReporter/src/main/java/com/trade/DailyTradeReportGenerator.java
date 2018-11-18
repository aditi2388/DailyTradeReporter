package com.trade;

import com.trade.processor.TradeProcessor;

public class DailyTradeReportGenerator {

	public static void main(String[] args) {

		TradeProcessor tradeProcessor = new TradeProcessor();
		tradeProcessor.processTradeReport("trade_input.txt");
	}

}
