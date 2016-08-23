package org.samples.portfolio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.samples.portfolio.Company;
import org.samples.portfolio.CompanyPosition;
import org.samples.portfolio.service.Trade.TradeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;


@Service
public class TradeServiceImpl implements TradeService {

	private static final Log logger = LogFactory.getLog(TradeServiceImpl.class);

	private final SimpMessageSendingOperations messagingTemplate;

	private final CompanyService portfolioService;

	private final List<TradeResult> tradeResults = new CopyOnWriteArrayList<>();


	@Autowired
	public TradeServiceImpl(SimpMessageSendingOperations messagingTemplate, CompanyService portfolioService) {
		this.messagingTemplate = messagingTemplate;
		this.portfolioService = portfolioService;
	}

	/**
	 * In real application a trade is probably executed in an external system, i.e. asynchronously.
	 */
	public void executeTrade(Trade trade) {

		Company portfolio = this.portfolioService.findCompany();
		String comapnyCode = trade.getCompanyCode();
		int pointsToTrade = trade.getPoints();

		CompanyPosition newPosition = (trade.getAction() == TradeAction.Buy) ?
				portfolio.add(comapnyCode, pointsToTrade) : portfolio.subtract(comapnyCode, pointsToTrade);

		if (newPosition == null) {
			String payload = "Rejected trade " + trade;
			this.messagingTemplate.convertAndSend("/queue/errors", payload);
			return;
		}

		this.tradeResults.add(new TradeResult(newPosition));
	}

	@Scheduled(fixedDelay=1500)
	public void sendTradeNotifications() {

		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);

		for (TradeResult result : this.tradeResults) {
			if (System.currentTimeMillis() >= (result.timestamp + 1500)) {
				logger.debug("Sending position update: " + result.position);
				this.messagingTemplate.convertAndSend("/queue/position-updates", result.position, map);
				this.tradeResults.remove(result);
			}
		}
	}


	private static class TradeResult {

		private final CompanyPosition position;
		private final long timestamp;

		public TradeResult(CompanyPosition position) {
			this.position = position;
			this.timestamp = System.currentTimeMillis();
		}
	}

}
