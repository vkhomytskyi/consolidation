package com.rebel.consolidation.handler;

import com.rebel.consolidation.handler.model.SearchResult;
import com.rebel.consolidation.model.Document;
import com.rebel.consolidation.model.DocumentQueryBuilder;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.test.TestDocument;
import com.rebel.consolidation.util.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class SearchHandler implements Handler<RoutingContext> {

	private final ElasticClient elasticClient;

	public SearchHandler() {
		this.elasticClient = new ElasticClient();
	}

	@Override
	public void handle(RoutingContext context) {
		JsonObject body = context.getBodyAsJson();

		QueryBuilder query = DocumentQueryBuilder.builder()
				.title(mapToString(body, "titleTerms"))
				.text(mapToString(body, "textTerms"))
				.keyword(mapToString(body, "keywordTerms"))
				.author(mapToString(body, "authorTerms"))
				.fromDate(body.getLong("fromDate"))
				.tillDate(body.getLong("tillDate"))
				.build();

		response(context, new SearchResult<>(Arrays.asList(new TestDocument())));
	}

	private void response(RoutingContext context, SearchResult<Document> searchResult) {
		context.response().end(JsonUtils.toJson(searchResult).encodePrettily());
	}

	private String[] mapToString(JsonObject body, String name) {
		JsonArray jsonArray = body.getJsonArray(name);
		if (isNull(jsonArray))
			return null;

		return jsonArray
				.stream()
				.map(o -> (String) o)
				.collect(Collectors.toList())
				.toArray(new String[jsonArray.size()]);
	}
}
