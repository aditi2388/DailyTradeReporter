package com.trade.processor;

import java.util.List;
import java.util.Map;

import com.trade.model.Trade;
import com.trade.service.TradeService;
import com.trade.service.TradeServiceImpl;
import com.trade.util.TradeConstants;

public class TradeProcessor {

	public void processTradeReport(String input) {

		TradeService tradeService = new TradeServiceImpl();
		// 1. read the trade instructions from trade_inout.txt file under resources
		List<Trade> trades = tradeService.getTradesList(input);

		// 2. calculate trades Incoming and outgoing amounts

		trades = tradeService.calculateIntOutAmt(trades);

		// 3. fix the settlement date if it falls under weekend based on currency

		trades = tradeService.fixSettlementDate(trades);

		// 4. divide trades based on incoming and outgoing trades and populate the
		// result map
		Map<Character, List<Trade>> map = tradeService.populateBuySellTrades(trades);

		// 5. print date and its corresponding total incoming amount
		tradeService.printTotalIncomingAmt(map.get(TradeConstants.SELL));

		// 6. print date and its corresponding total outgoing amount
		tradeService.printTotalOutgoingAmt(map.get(TradeConstants.BUY));

		// 7. print entity rank based on its total incoming and outgoing amounts
		tradeService.printRankingReport(map);
	}
}
