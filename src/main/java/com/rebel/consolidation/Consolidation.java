package com.rebel.consolidation;

import com.rebel.consolidation.model.DocumentQueryBuilder;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.test.TestDocument;
import com.rebel.consolidation.test.TestDocumentGenerator;

import java.text.DecimalFormat;
import java.util.Collection;

public class Consolidation {

	private final ElasticClient elasticClient;

	public Consolidation() {
		this.elasticClient = new ElasticClient();
	}

	public void run() {
//		generateTestData();
		quality("source1");
		quality("source2");
		quality("source3");
	}

	public void quality(String source) {
		Long valid =
				elasticClient.count(
						source,
						DocumentQueryBuilder
								.builder()
								.text("interesting")
								.limit(1000L)
								.build()
				);

		Long total =
				elasticClient.count(
						source,
						DocumentQueryBuilder
								.builder()
								.limit(1000L)
								.build()
				);

		double quality = 100D * valid / total;
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		System.out.println("Quality of " + source + ": " + numberFormat.format(quality) + "%");
	}

	public void generateTestData() {
		String source1 = "source1";
		String source2 = "source2";
		String source3 = "source3";

		generateTestData(
				source1,
				TestDocumentGenerator.generate(
						10000,
						source1,
						"some interesting info",
						"some shitty info",
						10
				)
		);

		generateTestData(
				source2,
				TestDocumentGenerator.generate(
						10000,
						source2,
						"some interesting info",
						"some shitty info",
						5
				)
		);

		generateTestData(
				source3,
				TestDocumentGenerator.generate(
						10000,
						source3,
						"some interesting info",
						"some shitty info",
						2
				)
		);
	}

	public void generateTestData(String source, Collection<TestDocument> collection) {
		elasticClient.deleteIndex(source);
		elasticClient.createIndex(source);
		elasticClient.bulkIndex(collection, source);
	}

	public static void main(String[] args) {
		Consolidation consolidation = new Consolidation();
		consolidation.run();
		System.exit(0);
	}
}
