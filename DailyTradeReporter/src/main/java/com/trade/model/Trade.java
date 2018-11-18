package com.trade.model;

import java.math.BigDecimal;
import java.util.Date;

public class Trade {
	
	private String entity;
	private char buyOrSell;
	private double agreedFx;
	private String currency;
	private Date instructionDate;
	private Date settlementDate;
	private Integer units;
	private double pricePerUnit;
	private BigDecimal intAmt;
	private BigDecimal outAmt;
	
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public char getBuyOrSell() {
		return buyOrSell;
	}
	public void setBuyOrSell(char buyOrSell) {
		this.buyOrSell = buyOrSell;
	}
	public double getAgreedFx() {
		return agreedFx;
	}
	public void setAgreedFx(double agreedFx) {
		this.agreedFx = agreedFx;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Date getInstructionDate() {
		return instructionDate;
	}
	public void setInstructionDate(Date instructionDate) {
		this.instructionDate = instructionDate;
	}
	public Date getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
	public Integer getUnits() {
		return units;
	}
	public void setUnits(Integer units) {
		this.units = units;
	}
	public double getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public BigDecimal getIntAmt() {
		return intAmt;
	}
	public void setIntAmt(BigDecimal intAmt) {
		this.intAmt = intAmt;
	}
	public BigDecimal getOutAmt() {
		return outAmt;
	}
	public void setOutAmt(BigDecimal outAmt) {
		this.outAmt = outAmt;
	}
	
	@Override
	public String toString() {
		return "Trade [entity=" + entity + ", buyOrSell=" + buyOrSell + ", agreedFx=" + agreedFx + ", currency="
				+ currency + ", instructionDate=" + instructionDate + ", settlementDate=" + settlementDate + ", units="
				+ units + ", pricePerUnit=" + pricePerUnit + ", intAmt=" + intAmt + ", outAmt=" + outAmt + "]";
	}
	
	

}
