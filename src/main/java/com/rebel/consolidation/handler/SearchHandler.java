package com.rebel.consolidation.handler;

import com.rebel.consolidation.model.*;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.services.RatioService;
import com.rebel.consolidation.services.StatisticsService;
import com.rebel.consolidation.test.Sources;
import com.rebel.consolidation.util.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static com.rebel.consolidation.handler.SearchHandler.Fields.*;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class SearchHandler implements Handler<RoutingContext> {

	private final ElasticClient     elasticClient;
	private final StatisticsService statisticsService;
	private final RatioService      ratioService;
	private final Logger logger = LoggerFactory.getLogger(SearchHandler.class);

	public SearchHandler(StatisticsService statisticsService, RatioService ratioService) {
		this.elasticClient = new ElasticClient();
		this.statisticsService = statisticsService;
		this.ratioService = ratioService;
	}

	class Fields {
		static final String TITLE_TERMS   = "titleTerms";
		static final String TEXT_TERMS    = "textTerms";
		static final String SOURCE        = "source";
		static final String KEYWORD_TERMS = "keywordTerms";
		static final String AUTHOR_TERMS  = "authorTerms";
		static final String FROM_DATE     = "fromDate";
		static final String TILL_DATE     = "tillDate";
		static final String LIMIT         = "limit";
		static final String ITERATIONS    = "iterations";
	}

	@Override
	public void handle(RoutingContext context) {
		JsonObject body = context.getBodyAsJson();

//		logger.info("Request: {0}", body.encode());

		Long limit = body.getLong(LIMIT, 1000L);
		Long iterations = body.getLong(ITERATIONS, 1L);

		if (!body.containsKey(SOURCE))
			body.put(SOURCE, new JsonArray().add(Sources.SCOPUS).add(Sources.KPI).add(Sources.RINZ));
		if (body.containsKey(LIMIT))
			body.remove(LIMIT);
		if (body.containsKey(ITERATIONS))
			body.remove(ITERATIONS);

		SearchResult result = null;

		for (int i = 0; i < iterations; i++) {
			DocumentQueryBuilder documentQueryBuilder = DocumentQueryBuilder.builder()
					.title(mapToString(body, TITLE_TERMS))
					.text(mapToString(body, TEXT_TERMS))
					.source(mapSources(body, limit))
					.keyword(mapToString(body, KEYWORD_TERMS))
					.author(mapToString(body, AUTHOR_TERMS))
					.fromDate(body.getLong(FROM_DATE))
					.tillDate(body.getLong(TILL_DATE));

			QueryBuilder query = documentQueryBuilder.build();

			result = elasticClient.search(query, Document.class);

			enrichResult(result, documentQueryBuilder.sources(), limit);

			saveResult(body.getMap().toString(), limit, documentQueryBuilder.sources(), result);
		}

		response(context, result);
	}

	private void saveResult(String query, Long requested, Map<String, Long> requests, SearchResult result) {
		statisticsService.saveResult(
				query,
				new SearchStatistics(
						requested,
						result.totalCount,
						requests.get(Sources.SCOPUS.toLowerCase()),
						result.scopusCount,
						requests.get(Sources.RINZ.toLowerCase()),
						result.rinzCount,
						requests.get(Sources.KPI.toLowerCase()),
						result.kpiCount,
						System.currentTimeMillis()
				)
		);
	}

	private void response(RoutingContext context, SearchResult searchResult) {
//		logger.info("Response: {0}", JsonUtils.toJson(searchResult).encode());
		context.response().end(JsonUtils.toJson(searchResult).encodePrettily());
	}

	private void enrichResult(SearchResult searchResult, Map<String, Long> requested, Long limit) {
		searchResult.scopusRequested = requested.get(Sources.SCOPUS.toLowerCase());
		searchResult.kpiRequested = requested.get(Sources.KPI.toLowerCase());
		searchResult.rinzRequested = requested.get(Sources.RINZ.toLowerCase());
		searchResult.totalRequested = limit;
	}

	private String[] mapToString(JsonObject body, String name) {
		JsonArray jsonArray = body.getJsonArray(name);
		if (isNull(jsonArray))
			return null;

		return jsonArray
				.stream()
				.map(o -> ((String) o).toLowerCase())
				.collect(toList())
				.toArray(new String[jsonArray.size()]);
	}

	private Map<String, Long> mapSources(JsonObject body, Long limit) {
		String[] sources = mapToString(body, SOURCE);
		if (sources.length == 1)
			return Collections.singletonMap(sources[0], limit);

		Map<String, Double> ratios = ratioService.getRatios(body.getMap().toString(), Methods.PROPORTIONAL);

		logger.info("Ratios: " + ratios);

		return Arrays
				.stream(sources)
				.collect(toMap(Function.identity(), s -> (long) (ratios.get(s) * limit)));
	}
}