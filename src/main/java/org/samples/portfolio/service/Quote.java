package org.samples.portfolio.service;

import java.math.BigDecimal;


public class Quote {

	private String comapnyCode;

	private BigDecimal quotePrice;

	public Quote(String ticker, BigDecimal price) {
		this.comapnyCode = ticker;
		this.quotePrice = price;
	}

	private Quote() {
	}

	public String getCompanyCode() {
		return this.comapnyCode;
	}

	public void setCompanyCode(String ticker) {
		this.comapnyCode = ticker;
	}

	public BigDecimal getPrice() {
		return this.quotePrice;
	}

	public void setPrice(BigDecimal price) {
		this.quotePrice = price;
	}

	@Override
	public String toString() {
		return "Quote [ticker=" + this.comapnyCode + ", price=" + this.quotePrice + "]";
	}
}
