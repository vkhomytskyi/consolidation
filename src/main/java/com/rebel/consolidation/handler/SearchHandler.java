package com.rebel.consolidation.handler;

import com.rebel.consolidation.model.Document;
import com.rebel.consolidation.model.DocumentQueryBuilder;
import com.rebel.consolidation.model.SearchMemory;
import com.rebel.consolidation.model.SearchResult;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.services.MemoryService;
import com.rebel.consolidation.util.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class SearchHandler implements Handler<RoutingContext> {

	private final ElasticClient elasticClient;
	private final MemoryService memoryService;
	private final Logger logger = LoggerFactory.getLogger(SearchHandler.class);

	public SearchHandler(MemoryService memoryService) {
		this.elasticClient = new ElasticClient();
		this.memoryService = memoryService;
	}

	@Override
	public void handle(RoutingContext context) {
		JsonObject body = context.getBodyAsJson();

		logger.info("Request: {0}", body.encode());

		QueryBuilder query = DocumentQueryBuilder.builder()
				.title(mapToString(body, "titleTerms"))
				.text(mapToString(body, "textTerms"))
				.source(mapToString(body, "source"))
				.keyword(mapToString(body, "keywordTerms"))
				.author(mapToString(body, "authorTerms"))
				.fromDate(body.getLong("fromDate"))
				.tillDate(body.getLong("tillDate"))
				.limit(body.getLong("fromId"), body.getLong("toId"))
				.build();

		SearchResult result = elasticClient.search(query, Document.class);

		saveMemory(body.getMap().toString(), result);

		response(context, result);
	}

	private void saveMemory(String query, SearchResult result) {
		memoryService.saveMemory(
				query,
				new SearchMemory(
						result.totalCount,
						result.scopusCount,
						result.rinzCount,
						result.kpiCount,
						System.currentTimeMillis()
				)
		);
	}

	private void response(RoutingContext context, SearchResult searchResult) {
		logger.info("Response: {0}", JsonUtils.toJson(searchResult).encode());
		context.response().end(JsonUtils.toJson(searchResult).encodePrettily());
	}

	private String[] mapToString(JsonObject body, String name) {
		JsonArray jsonArray = body.getJsonArray(name);
		if (isNull(jsonArray))
			return null;

		return jsonArray
				.stream()
				.map(o -> ((String) o).toLowerCase())
				.collect(Collectors.toList())
				.toArray(new String[jsonArray.size()]);
	}
}