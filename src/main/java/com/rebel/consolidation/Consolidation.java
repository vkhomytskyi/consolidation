package com.rebel.consolidation;

import com.rebel.consolidation.model.DocumentQueryBuilder;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.test.Sources;
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
		generateTestData();
//		quality("source1");
//		quality("source2");
//		quality("source3");
	}

	public void quality(String source) {
		Long valid =
				elasticClient.count(
						DocumentQueryBuilder
								.builder()
								.text("interesting")
								.build()
				);

		Long total =
				elasticClient.count(
						DocumentQueryBuilder
								.builder()
								.build()
				);

		double quality = 100D * valid / total;
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		System.out.println("Quality of " + source + ": " + numberFormat.format(quality) + "%");
	}

	public void generateTestData() {

		elasticClient.deleteIndex("documents");
		elasticClient.createIndex("documents");

		generateTestData(
				"documents",
				TestDocumentGenerator.generate(
						10000,
						Sources.SCOPUS,
						"some interesting info",
						"some shitty info",
						10
				)
		);

		generateTestData(
				"documents",
				TestDocumentGenerator.generate(
						10000,
						Sources.RINZ,
						"some interesting info",
						"some shitty info",
						5
				)
		);

		generateTestData(
				"documents",
				TestDocumentGenerator.generate(
						10000,
						Sources.KPI,
						"some interesting info",
						"some shitty info",
						2
				)
		);
	}

	public void generateTestData(String index, Collection<TestDocument> collection) {
//		elasticClient.deleteIndex(source);
//		elasticClient.createIndex(source);
		elasticClient.bulkIndex(collection, index);
	}

	public static void main(String[] args) {
		Consolidation consolidation = new Consolidation();
		consolidation.run();
		System.exit(0);
	}
}
