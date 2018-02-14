package com.rebel.consolidation;

import com.rebel.consolidation.model.Document;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.test.TestDocument;

public class Consolidation {

	private String index = "documents";
	private String type  = "document";

	private ElasticClient elasticClient;

	public Consolidation() {
		this.elasticClient = new ElasticClient();
	}

	public void run() {
//		elasticClient.deleteIndex(index);
//		elasticClient.createIndex(index);
//		elasticClient.indexDocument(new TestDocument("hello", "source1"), index, type);

		System.out.println(
				elasticClient.search(index, type, Document.class)
		);
	}

	public static void main(String[] args) {
		Consolidation consolidation = new Consolidation();
		consolidation.run();
	}
}
