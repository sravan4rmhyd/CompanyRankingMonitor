package org.samples.portfolio;

import java.util.*;


public class Company {

	private final Map<String,CompanyPosition> positionLookup = new LinkedHashMap<String,CompanyPosition>();


	public List<CompanyPosition> getPositions() {
		return new ArrayList<CompanyPosition>(positionLookup.values());
	}

	public void addPosition(CompanyPosition position) {
		this.positionLookup.put(position.getCompanyCode(), position);
	}

	public CompanyPosition getPortfolioPosition(String ticker) {
		return this.positionLookup.get(ticker);
	}

	/**
	 * @return the updated position or null
	 */
	public CompanyPosition add(String ticker, int sharesToBuy) {
		CompanyPosition position = this.positionLookup.get(ticker);
		if ((position == null) || (sharesToBuy < 1)) {
			return null;
		}
		position = new CompanyPosition(position, sharesToBuy);
		this.positionLookup.put(ticker, position);
		return position;
	}
	
	public CompanyPosition subtract(String companyCode, int updatePoints) {
		CompanyPosition position = this.positionLookup.get(companyCode);
		if ((position == null) || (updatePoints < 1) || (position.getPoints() < updatePoints)) {
			return null;
		}
		position = new CompanyPosition(position, -updatePoints);
		this.positionLookup.put(companyCode, position);
		return position;
	}

}
