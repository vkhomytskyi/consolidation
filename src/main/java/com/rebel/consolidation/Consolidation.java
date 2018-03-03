package com.rebel.consolidation;

import com.rebel.consolidation.model.DocumentQueryBuilder;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.test.TestDocumentGenerator;
import org.elasticsearch.index.query.QueryBuilder;

public class Consolidation {

	private String index = "documents";
	private String type  = "document";

	private ElasticClient elasticClient;

	public Consolidation() {
		this.elasticClient = new ElasticClient();
	}

	public void run() {
//		generateTestData();
		quiality("source1");
		quiality("source2");
		quiality("source3");
	}

	public void quiality(String source) {
		double quality = 1D * elasticClient.count(index, type, queryBuilder(source, "interesting"), 10000) / elasticClient.count(index, type, queryBuilder(source, null), 1000);
		System.out.println("Quality of " + source + ": " + quality * 100 + "%");
	}

	public QueryBuilder queryBuilder(String source, String text) {
		return DocumentQueryBuilder
				.builder()
				.text("KPI", "NTUU")
				.source(source)
				.text(text)
				.build();
	}

	public void generateTestData() {
		elasticClient.deleteIndex(index);
		elasticClient.createIndex(index);

		elasticClient.bulkIndex(
				TestDocumentGenerator.generate(
						10000,
						"source1",
						"some interesting info",
						"some shitty info",
						10
				),
				index,
				type
		);

		elasticClient.bulkIndex(
				TestDocumentGenerator.generate(
						10000,
						"source2",
						"some interesting info",
						"some shitty info",
						5
				),
				index,
				type
		);

		elasticClient.bulkIndex(
				TestDocumentGenerator.generate(
						10000,
						"source3",
						"some interesting info",
						"some shitty info",
						2
				),
				index,
				type
		);
	}

	public static void main(String[] args) {
		Consolidation consolidation = new Consolidation();
		consolidation.run();
		System.exit(0);
	}
}
