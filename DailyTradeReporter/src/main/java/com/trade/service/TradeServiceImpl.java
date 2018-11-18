package com.trade.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trade.model.Trade;
import com.trade.util.TradeConstants;

public class TradeServiceImpl implements TradeService {

	private static final Logger LOG = LoggerFactory.getLogger(TradeServiceImpl.class);

	@Override
	public List<Trade> getTradesList(String fileName) {
		List<Trade> trades = new ArrayList<Trade>();
		String line = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		try {
			FileReader reader = new FileReader(
					new File(getClass().getClassLoader().getResource(fileName).getFile()));
			BufferedReader br = new BufferedReader(reader);
			br.readLine(); // skipping the header
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length == 8) {
					Trade trade = new Trade();
					trade.setEntity(tokens[0]);
					trade.setBuyOrSell(tokens[1].charAt(0));
					trade.setAgreedFx(Double.parseDouble(tokens[2]));
					trade.setCurrency(tokens[3]);
					trade.setInstructionDate(sdf.parse(tokens[4]));
					trade.setSettlementDate(sdf.parse(tokens[5]));
					trade.setUnits(Integer.parseInt(tokens[6]));
					trade.setPricePerUnit(Double.parseDouble(tokens[7]));

					trades.add(trade);

				} else {
					LOG.error("This is an Invalid Trade Entry, hence skipping it.");
					continue;
				}
			}
			br.close();
		} catch (IOException | ParseException e) {
			LOG.error("Invalid Input File..");
			System.exit(1);
		}
		return trades;
	}

	@Override
	public List<Trade> calculateIntOutAmt(List<Trade> trades) {
		BigDecimal amount = new BigDecimal(0.00);

		if (trades != null) {
			for (Trade trade : trades) {
				amount = BigDecimal.valueOf(trade.getPricePerUnit() * trade.getUnits() * trade.getAgreedFx());
				if (TradeConstants.BUY.equals(trade.getBuyOrSell())) {
					trade.setOutAmt(amount);
				} else {
					trade.setIntAmt(amount);
				}
			}
		}
		return trades;
	}

	@Override
	public List<Trade> fixSettlementDate(List<Trade> trades) {
		for (Trade trade : trades) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(trade.getSettlementDate());
			if (trade.getCurrency().equalsIgnoreCase(TradeConstants.AED)
					|| trade.getCurrency().equalsIgnoreCase(TradeConstants.SAR)) {
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
					cal.add(Calendar.DATE, 2);
				} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					cal.add(Calendar.DATE, 1);
				}
			} else {
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					cal.add(Calendar.DATE, 2);
				} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DATE, 1);
				}
			}
			trade.setSettlementDate(cal.getTime());
		}
		return trades;
	}

	public Map<Character, List<Trade>> populateBuySellTrades(List<Trade> trades) {
		Map<Character, List<Trade>> map = new HashMap<Character, List<Trade>>();
		for (Trade trade : trades) {
			List<Trade> list = map.get(trade.getBuyOrSell());
			if (list == null) {
				list = new ArrayList<Trade>();
				map.put(trade.getBuyOrSell(), list);
			}
			list.add(trade);
		}

		return map;
	}

	@Override
	public void printTotalIncomingAmt(List<Trade> trades) {

		Map<Date, BigDecimal> dailyIncoming = new TreeMap<Date, BigDecimal>();
		for (Trade trade : trades) {
			BigDecimal amount = dailyIncoming.get(trade.getSettlementDate());
			if (dailyIncoming.containsKey(trade.getSettlementDate())) {
				amount = dailyIncoming.get(trade.getSettlementDate()).add(trade.getIntAmt());
				dailyIncoming.put(trade.getSettlementDate(), amount);
			} else {
				dailyIncoming.put(trade.getSettlementDate(), trade.getIntAmt());
			}
		}

		System.out.format("Amount in USD settled incoming everyday. \n\n");
		System.out.format("Date				Amount\n\n");
		
		for (Map.Entry<Date, BigDecimal> entry : dailyIncoming.entrySet()) {
			System.out.format("%tc %10s USD\n\n", entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void printTotalOutgoingAmt(List<Trade> trades) {
		Map<Date, BigDecimal> dailyOutgoing = new TreeMap<Date, BigDecimal>();
		for (Trade trade : trades) {
			BigDecimal amount = dailyOutgoing.get(trade.getSettlementDate());
			if (dailyOutgoing.containsKey(trade.getSettlementDate())) {
				amount = dailyOutgoing.get(trade.getSettlementDate()).add(trade.getOutAmt());
				dailyOutgoing.put(trade.getSettlementDate(), amount);
			} else {
				dailyOutgoing.put(trade.getSettlementDate(), trade.getOutAmt());
			}
		}
		
		System.out.format("Amount in USD settled outgoing everyday. \n\n");
		System.out.format("Date				Amount\n\n");
		for (Map.Entry<Date, BigDecimal> entry : dailyOutgoing.entrySet()) {
			System.out.format("%tc %10s USD\n\n", entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void printRankingReport(Map<Character, List<Trade>> result) {
		List<Trade> outgoing = result.get(TradeConstants.BUY);
		List<Trade> incoming = result.get(TradeConstants.SELL);

		// Ranking for outgoing entities based on their total outgoing amount

		Map<String, BigDecimal> entityOutgoing = new HashMap<String, BigDecimal>();

		for (Trade trade : outgoing) {
			BigDecimal amount = entityOutgoing.get(trade.getEntity());
			if (entityOutgoing.containsKey(trade.getEntity())) {
				amount = entityOutgoing.get(trade.getEntity()).add(trade.getOutAmt());
				entityOutgoing.put(trade.getEntity(), amount);
			} else {
				entityOutgoing.put(trade.getEntity(), trade.getOutAmt());
			}
		}
		
		Map<String, BigDecimal> sortedOutgoingMap = sortMap(entityOutgoing);
		
		System.out.println("Ranking for incoming entities based on their total outgoing amount\n");

		int outRank = 1;
		for (Entry<String, BigDecimal> entry : sortedOutgoingMap.entrySet()) {
			System.out.format("%s %s %s USD \n\n", outRank++, entry.getKey(), entry.getValue(), "");
		}

		// Ranking for incoming entities based on their total incoming amount

		Map<String, BigDecimal> entityincoming = new HashMap<String, BigDecimal>();

		for (Trade trade : incoming) {
			BigDecimal amount = entityincoming.get(trade.getEntity());
			if (entityincoming.containsKey(trade.getEntity())) {
				amount = entityincoming.get(trade.getEntity()).add(trade.getIntAmt());
				entityincoming.put(trade.getEntity(), amount);
			} else {
				entityincoming.put(trade.getEntity(), trade.getIntAmt());
			}
		}
		
		Map<String, BigDecimal> sortedIncomingMap = sortMap(entityincoming);
		
		System.out.println("Ranking for incoming entities based on their total incoming amount\n\n");

		int intRank = 1;
		for (Entry<String, BigDecimal> entry : sortedIncomingMap.entrySet()) {
			System.out.format("%s %s %s USD \n", intRank++, entry.getKey(), entry.getValue(), "");
		}

	}
	
	private Map<String,BigDecimal> sortMap(Map<String,BigDecimal> map){
		Map<String,BigDecimal> sorted = map.entrySet().
				stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
				                 LinkedHashMap::new));
		return sorted;

	}
}
