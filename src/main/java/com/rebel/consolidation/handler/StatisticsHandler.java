package com.rebel.consolidation.handler;

import com.rebel.consolidation.services.StatisticsService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class StatisticsHandler implements Handler<RoutingContext> {

	private final StatisticsService statisticsService;

	public StatisticsHandler(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@Override
	public void handle(RoutingContext context) {
		context.response().end(statisticsService.historyJson().encodePrettily());
	}
}