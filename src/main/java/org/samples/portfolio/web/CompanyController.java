package org.samples.portfolio.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.samples.portfolio.Company;
import org.samples.portfolio.CompanyPosition;
import org.samples.portfolio.service.CompanyService;
import org.samples.portfolio.service.Trade;
import org.samples.portfolio.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;


@Controller
public class CompanyController {

	private static final Log logger = LogFactory.getLog(CompanyController.class);

	private final CompanyService portfolioService;

	private final TradeService tradeService;


	@Autowired
	public CompanyController(CompanyService portfolioService, TradeService tradeService) {
		this.portfolioService = portfolioService;
		this.tradeService = tradeService;
	}

	@SubscribeMapping("/positions")
	public List<CompanyPosition> getPositions() throws Exception {
		Company portfolio = this.portfolioService.findCompany();
		return portfolio.getPositions();
	}

	@MessageMapping("/trade")
	public void executeTrade(Trade trade) {
		logger.debug("Trade: " + trade);
		this.tradeService.executeTrade(trade);
	}

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}

}
