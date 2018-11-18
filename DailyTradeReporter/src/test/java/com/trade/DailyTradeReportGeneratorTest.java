package com.trade;

import org.junit.jupiter.api.Test;

import com.trade.processor.TradeProcessor;

class DailyTradeReportGeneratorTest {
	
	TradeProcessor tradeProcessor = new TradeProcessor();

	@Test
	void testInput1() {
		tradeProcessor.processTradeReport("test_input1.txt");
	}
	
	@Test
	void testInput2() {
		tradeProcessor.processTradeReport("test_input2.txt");
	}
	
	@Test
	void testInput3() {
		tradeProcessor.processTradeReport("test_input3.txt");
	}
	
	@Test
	void testInput4() {
		tradeProcessor.processTradeReport("test_input4.txt");
	}

}
