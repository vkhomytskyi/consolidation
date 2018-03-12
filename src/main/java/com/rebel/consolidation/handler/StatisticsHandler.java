package com.rebel.consolidation.handler;

import com.rebel.consolidation.services.StatisticsService;
import com.rebel.consolidation.test.Sources;
import com.rebel.consolidation.util.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.rebel.consolidation.handler.SearchHandler.Fields.ITERATIONS;
import static com.rebel.consolidation.handler.SearchHandler.Fields.LIMIT;
import static com.rebel.consolidation.handler.SearchHandler.Fields.SOURCE;

public class StatisticsHandler implements Handler<RoutingContext> {

	private final StatisticsService statisticsService;

	public StatisticsHandler(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@Override
	public void handle(RoutingContext context) {
		JsonObject body = context.getBodyAsJson();
		if (!body.containsKey(SOURCE))
			body.put(SOURCE, new JsonArray().add(Sources.SCOPUS).add(Sources.KPI).add(Sources.RINZ));
		if (body.containsKey(LIMIT))
			body.remove(LIMIT);
		if (body.containsKey(ITERATIONS))
			body.remove(ITERATIONS);

		String query = body.getMap().toString();

		context.response().end(JsonUtils.toJson(statisticsService.history(query)).encode());
	}
}