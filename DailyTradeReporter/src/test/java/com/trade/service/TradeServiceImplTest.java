package com.trade.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.trade.model.Trade;
import com.trade.util.TradeConstants;

public class TradeServiceImplTest {
	
	static TradeService tradeService = new TradeServiceImpl();
	
	static List<Trade> tradesList = new ArrayList<Trade>();
	
	static String test_input1 = "test_input1.txt";
	
	@BeforeClass
	public static void setup() {
		tradesList = tradeService.getTradesList(test_input1);
	}

	@Test
	public void testGetTradeList() {
		List<Trade> trades = tradeService.getTradesList(test_input1);
		Assert.assertNotNull(trades);
		Assert.assertEquals(trades.size(), 3);
	}
	
	
	@Test
	public void testcalculateIntOutAmt() {
	tradesList = tradeService.calculateIntOutAmt(tradesList);
	Assert.assertEquals(tradesList.get(0).getBuyOrSell(), 'B');
	Assert.assertNotNull(tradesList.get(0).getOutAmt());
	Assert.assertNull(tradesList.get(0).getIntAmt());
	}
	
	@Test
	public void testFixSettlementDate() {
		List<Trade> trades = tradeService.getTradesList(test_input1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(trades.get(0).getSettlementDate());
		Assert.assertEquals(cal.get(Calendar.DAY_OF_WEEK), Calendar.SATURDAY);
		tradesList = tradeService.fixSettlementDate(tradeService.getTradesList(test_input1));
		cal.setTime(tradesList.get(0).getSettlementDate());
		Assert.assertEquals(cal.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
	}
	
	@Test
	public void testpopulateBuySellTrades(){
		List<Trade> trades = tradeService.getTradesList(test_input1);
		trades = tradeService.calculateIntOutAmt(trades);
		trades = tradeService.fixSettlementDate(trades);
		Map<Character, List<Trade>> map = tradeService.populateBuySellTrades(trades);
		Assert.assertNotNull(map);
		System.out.println(map);
	}
	
	@Test
	public void testPrintTotalIncomingAmt() {
		List<Trade> trades = tradeService.getTradesList(test_input1);
		trades = tradeService.calculateIntOutAmt(trades);
		trades = tradeService.fixSettlementDate(trades);
		Map<Character, List<Trade>> map = tradeService.populateBuySellTrades(trades);
		tradeService.printTotalIncomingAmt(map.get(TradeConstants.SELL));
	}
	

	@Test
	public void testPrintTotalOutgoingAmt() {
		List<Trade> trades = tradeService.getTradesList(test_input1);
		trades = tradeService.calculateIntOutAmt(trades);
		trades = tradeService.fixSettlementDate(trades);
		Map<Character, List<Trade>> map = tradeService.populateBuySellTrades(trades);
		tradeService.printTotalOutgoingAmt(map.get(TradeConstants.BUY));
	}
	
	@Test
	public void testPrintRankingReport() {
		List<Trade> trades = tradeService.getTradesList(test_input1);
		trades = tradeService.calculateIntOutAmt(trades);
		trades = tradeService.fixSettlementDate(trades);
		Map<Character, List<Trade>> map = tradeService.populateBuySellTrades(trades);
		tradeService.printRankingReport(map);
	}
	
}
