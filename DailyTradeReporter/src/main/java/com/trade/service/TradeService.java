package com.trade.service;

import java.util.List;
import java.util.Map;

import com.trade.model.Trade;

public interface TradeService {
	
	public List<Trade> getTradesList();

	public void printTotalIncomingAmt(List<Trade> trades);
	
	public void printTotalOutgoingAmt(List<Trade> trades);
	
	public List<Trade> calculateIntOutAmt(List<Trade> trades);
	
	public List<Trade> fixSettlementDate(List<Trade> trades);
	
	public Map<Character, List<Trade>> populateBuySellTrades(List<Trade> trades);
	
	public void printRankingReport(Map<Character, List<Trade>> result);
}
