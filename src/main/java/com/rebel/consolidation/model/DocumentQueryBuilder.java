package com.rebel.consolidation.model;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.*;

public class DocumentQueryBuilder {
	private String[]          titleTerms;
	private String[]          textTerms;
	private String[]          keywordTerms;
	private Map<String, Long> sources;
	private String[]          authorTerms;
	private Long              fromDate;
	private Long              tillDate;


	private DocumentQueryBuilder() {
	}

	public static DocumentQueryBuilder builder() {
		return new DocumentQueryBuilder();
	}

	public DocumentQueryBuilder title(String... titleTerms) {
		this.titleTerms = titleTerms;
		return this;
	}

	public DocumentQueryBuilder text(String... textTerms) {
		this.textTerms = textTerms;
		return this;
	}

	public DocumentQueryBuilder author(String... authorTerms) {
		this.authorTerms = authorTerms;
		return this;
	}

	public DocumentQueryBuilder keyword(String... keywordTerms) {
		this.keywordTerms = keywordTerms;
		return this;
	}

	public DocumentQueryBuilder source(Map<String, Long> sources) {
		this.sources = sources;
		return this;
	}

	public DocumentQueryBuilder fromDate(Long fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public DocumentQueryBuilder tillDate(Long tillDate) {
		this.tillDate = tillDate;
		return this;
	}

	public QueryBuilder build() {
		if (isNull(sources))
			throw new UnsupportedOperationException("At least one source should be selected");

		BoolQueryBuilder queryBuilder = boolQuery();
		if (nonNull(textTerms))
			queryBuilder.filter(termsQuery("text", textTerms));
		if (nonNull(titleTerms))
			queryBuilder.filter(termsQuery("title", titleTerms));
		if (nonNull(keywordTerms))
			queryBuilder.filter(termsQuery("keywords", keywordTerms));
		if (nonNull(authorTerms))
			queryBuilder.filter(termsQuery("author", authorTerms));
		if (nonNull(fromDate) || nonNull(tillDate)) {
			RangeQueryBuilder rangeQuery = rangeQuery("publicationDate");
			if (nonNull(fromDate))
				rangeQuery.from(fromDate);
			if (nonNull(tillDate))
				rangeQuery.to(tillDate);
			queryBuilder.filter(rangeQuery);
		}

		BoolQueryBuilder sourcesQuery = new BoolQueryBuilder();
		for (Map.Entry<String, Long> source : sources.entrySet()) {
			BoolQueryBuilder sourceQuery = new BoolQueryBuilder();
			sourceQuery.filter(termQuery("source", source.getKey()))
					.filter(rangeQuery("id").to(source.getValue()));
			queryBuilder.filter().forEach(sourceQuery::filter);
			sourcesQuery.should(sourceQuery);
		}

		return sourcesQuery;
	}

	public String[] titleTerms() {
		return titleTerms;
	}

	public String[] textTerms() {
		return textTerms;
	}

	public String[] keywordTerms() {
		return keywordTerms;
	}

	public Map<String, Long> sources() {
		return sources;
	}

	public String[] authorTerms() {
		return authorTerms;
	}

	public Long fromDate() {
		return fromDate;
	}

	public Long tillDate() {
		return tillDate;
	}
}