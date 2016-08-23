package org.samples.portfolio;


public class CompanyPosition {

	private String companyName;

	private String companyCode;

	private double companyRank;

	private int points;

	private long updateTime;


	public CompanyPosition(String company, String ticker, double price, int shares) {
		this.companyName = company;
		this.companyCode = ticker;
		this.companyRank = price;
		this.points = shares;
		this.updateTime = System.currentTimeMillis();
	}

	public CompanyPosition(CompanyPosition other, int sharesToAddOrSubtract) {
		this.companyName = other.companyName;
		this.companyCode = other.companyCode;
		this.companyRank = other.companyRank;
		this.points = other.points + sharesToAddOrSubtract;
		this.updateTime = System.currentTimeMillis();
	}

	private CompanyPosition() {
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public double getCompanyRank() {
		return companyRank;
	}

	public void setCompanyRank(double companyRank) {
		this.companyRank = companyRank;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public long getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "PortfolioPosition [company=" + this.companyName + ", ticker=" + this.companyCode
				+ ", price=" + this.companyRank + ", shares=" + this.points + "]";
	}

}
