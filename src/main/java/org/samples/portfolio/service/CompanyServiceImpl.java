package org.samples.portfolio.service;

import org.samples.portfolio.Company;
import org.samples.portfolio.CompanyPosition;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
	Company company = new Company();

	public CompanyServiceImpl() {	
		company.addPosition(new CompanyPosition("EMC Corporation", "EMC", 24.30, 75));
		company.addPosition(new CompanyPosition("Google Inc", "GOOG", 905.09, 5));
		company.addPosition(new CompanyPosition("VMware, Inc.", "VMW", 65.58, 23));
		company.addPosition(new CompanyPosition("Red Hat", "RHT", 48.30, 15));		
	}


	public Company findCompany() {		
		if (company == null) {
			throw new IllegalArgumentException("exception in getting company");
		}
		return company;
	}

}
