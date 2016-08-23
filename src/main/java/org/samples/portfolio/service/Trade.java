package org.samples.portfolio.service;


public class Trade {

	private String companyCode;

	private int points;

	private TradeAction action;

	private String username;


	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int shares) {
		this.points = shares;
	}

	public TradeAction getAction() {
		return this.action;
	}

	public void setAction(TradeAction action) {
		this.action = action;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "[ticker=" + this.companyCode + ", shares=" + this.points
				+ ", action=" + this.action + ", username=" + this.username + "]";
	}


	public enum TradeAction {
		Buy, Sell;
	}

}
