package com.trade;


import org.junit.Test;

import com.trade.processor.TradeProcessor;

public class DailyTradeReportGeneratorTest {
	
	TradeProcessor tradeProcessor = new TradeProcessor();

	@Test
	public void testInput1() {
		tradeProcessor.processTradeReport("test_input1.txt");
	}
	
	@Test
	public void testInput2() {
		tradeProcessor.processTradeReport("test_input2.txt");
	}
	
	@Test
	public void testInput3() {
		tradeProcessor.processTradeReport("test_input3.txt");
	}
	
	@Test
	public void testInput4() {
		tradeProcessor.processTradeReport("test_input4.txt");
	}

}
