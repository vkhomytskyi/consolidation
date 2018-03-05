package com.rebel.consolidation.handler;

import com.rebel.consolidation.services.MemoryService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class MemoryHandler implements Handler<RoutingContext> {

	private final MemoryService memoryService;

	public MemoryHandler(MemoryService memoryService) {
		this.memoryService = memoryService;
	}

	@Override
	public void handle(RoutingContext context) {
		context.response().end(memoryService.memories().encodePrettily());
	}
}