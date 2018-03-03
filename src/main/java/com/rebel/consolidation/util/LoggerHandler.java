package com.rebel.consolidation.util;

import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class LoggerHandler implements Handler<RoutingContext> {

	private final Logger logger;

	private LoggerHandler() {
		this.logger = LoggerFactory.getLogger(LoggerHandler.class);
	}

	@Override
	public void handle(RoutingContext context) {
		logger.info(
				"METHOD: [{0}] PATH: [{1}] PARAMS: [{2}]",
				context.request().method().name(),
				context.request().path(),
				context.request().params()
		);
		context.next();
	}

	public static LoggerHandler create() {
		return new LoggerHandler();
	}
}
