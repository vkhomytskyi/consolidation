package com.rebel.consolidation;

import com.rebel.consolidation.model.Document;
import com.rebel.consolidation.services.ElasticClient;
import com.rebel.consolidation.test.TestDocumentGenerator;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.sun.tools.doclint.Entity.and;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class Consolidation {

	private String index = "documents";
	private String type  = "document";

	private ElasticClient elasticClient;

	public Consolidation() {
		this.elasticClient = new ElasticClient();
	}

	public void run() {
		quiality("source1");
		quiality("source2");
		quiality("source3");
	}

	public void quiality(String source) {
		double quality = 1D * elasticClient.count(index, type, queryBuilder(source, "interesting")) / elasticClient.count(index, type, queryBuilder(source));
		System.out.println("Quality of " + source + ": " + quality * 100 + "%");
	}

	public BoolQueryBuilder queryBuilder(String source) {
		return QueryBuilders
				.boolQuery()
				.filter(termQuery("source", source));
	}

	public BoolQueryBuilder queryBuilder(String source, String text) {
		return QueryBuilders
				.boolQuery()
				.filter(termQuery("source", source))
				.filter(termQuery("text", text));
	}

	public void generateTestData() {
		elasticClient.deleteIndex(index);
		elasticClient.createIndex(index);

		elasticClient.bulkIndex(
				TestDocumentGenerator.generate(
						1000,
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
						1000,
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
						1000,
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
	}
}
