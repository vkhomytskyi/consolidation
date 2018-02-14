package com.rebel.consolidation.services;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.rebel.consolidation.util.JsonUtils.fromJson;
import static com.rebel.consolidation.util.JsonUtils.toJson;
import static java.util.stream.Collectors.toList;

public class ElasticClient implements Closeable {

	private final Logger logger = LoggerFactory.getLogger(ElasticClient.class);

	private RestHighLevelClient client;

	public ElasticClient() {
		initConnection();
	}

	private void initConnection() {
		client = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")
				)
		);
	}

	public <T> void index(T document, String index, String type) {
		try {
			IndexRequest indexRequest = new IndexRequest(index, type);
			indexRequest.source(toJson(document).encode(), XContentType.JSON);
			client.index(indexRequest);
		} catch (Exception e) {
			logger.error("Index error", e);
		}
	}

	public <T> void bulkIndex(Collection<T> documents, String index, String type) {
		BulkRequest bulkRequest = new BulkRequest()
				.add(
						documents
								.stream()
								.map(d -> new IndexRequest(index, type)
										.source(toJson(d).encode(), XContentType.JSON)
								)
								.collect(toList())
				);

		try {
			client.bulk(bulkRequest);
		} catch (Exception e) {
			logger.error("Index error", e);
		}
	}

	public void deleteIndex(String index) {
		try {
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
			client.indices().delete(deleteIndexRequest);
		} catch (Exception e) {
			logger.error("Delete index error", e);
		}
	}

	public void createIndex(String index) {
		try {
			CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
			client.indices().create(createIndexRequest);
		} catch (Exception e) {
			logger.error("Create index error", e);
		}
	}

	public <T> List<T> search(String index, String type, QueryBuilder queryBuilder, Class<T> clazz) {
		List<T> result = new ArrayList<>();
			SearchResponse searchResponse = search(index, type, queryBuilder);;
			SearchHits hits = searchResponse.getHits();
			if (hits.totalHits > 0)
				for (SearchHit hit : hits.getHits())
					result.add(fromJson(hit.getSourceAsString(), clazz));

		return result;
	}

	private SearchResponse search(String index, String type, QueryBuilder queryBuilder) {
		SearchRequest searchRequest = new SearchRequest(index)
				.types(type)
				.searchType(SearchType.DFS_QUERY_THEN_FETCH);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
				.query(queryBuilder);
		searchRequest.source(searchSourceBuilder);

		try {
			return client.search(searchRequest);
		} catch (Exception e) {
			logger.error("Search error", e);
			throw new NullPointerException("Search error");
		}
	}

	public Long count(String index, String type, QueryBuilder queryBuilder) {
			SearchResponse searchResponse = search(index, type, queryBuilder);
			return searchResponse.getHits().totalHits;
	}

	@Override
	public void close() throws IOException {
		client.close();
	}
}