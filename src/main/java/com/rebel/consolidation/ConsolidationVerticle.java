package com.rebel.consolidation;

import com.rebel.consolidation.handler.StatisticsHandler;
import com.rebel.consolidation.handler.SearchHandler;
import com.rebel.consolidation.services.RatioService;
import com.rebel.consolidation.services.StatisticsService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class ConsolidationVerticle extends AbstractVerticle {

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.route().handler(
				CorsHandler
						.create("*")
						.allowedMethod(HttpMethod.POST)
						.allowedMethod(HttpMethod.GET)
						.allowedMethod(HttpMethod.OPTIONS)
						.allowedHeader("Content-Type")
		);
		router.post().handler(BodyHandler.create());

		StatisticsService statisticsService = new StatisticsService();
		RatioService ratioService = new RatioService(statisticsService);

		router.post("/search").handler(new SearchHandler(statisticsService, ratioService));
		router.get("/statistics").handler(new StatisticsHandler(statisticsService));

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
}
