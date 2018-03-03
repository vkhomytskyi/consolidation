package com.rebel.consolidation;

import com.rebel.consolidation.util.LoggerHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

public class ConsolidationVerticle extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(ConsolidationVerticle.class);

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.route().handler(LoggerHandler.create());
		router.route().handler(CorsHandler.create("*"));

		router.route().handler(msg -> {
			msg.response().end(new JsonObject().put("hello", "hello").encode());
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);

		logger.info("Server is started");
	}
}
