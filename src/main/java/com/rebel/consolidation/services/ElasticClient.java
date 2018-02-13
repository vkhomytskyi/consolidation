package com.rebel.consolidation.services;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.Closeable;
import java.io.IOException;

public class ElasticClient implements Closeable {

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

	@Override
	public void close() throws IOException {
		client.close();
	}
}
