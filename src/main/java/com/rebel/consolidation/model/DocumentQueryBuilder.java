package com.rebel.consolidation.model;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

public class DocumentQueryBuilder {
	private String[]  titleTerms;
	private String[]  textTerms;
	private String[]  keywordTerms;
	private String    source;
	private String[]  authorTerms;
	private LocalDate fromDate;
	private LocalDate tillDate;


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

	public DocumentQueryBuilder source(String source) {
		this.source = source;
		return this;
	}

	public DocumentQueryBuilder fromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public DocumentQueryBuilder tillDate(LocalDate tillDate) {
		this.tillDate = tillDate;
		return this;
	}

	public QueryBuilder build() {
		BoolQueryBuilder queryBuilder = boolQuery();
		if (nonNull(source))
			queryBuilder.filter(termsQuery("source", source));
		if (nonNull(textTerms))
			queryBuilder.filter(termsQuery("text", textTerms));
		if (nonNull(titleTerms))
			queryBuilder.filter(termsQuery("title", titleTerms));
		if (nonNull(keywordTerms))
			queryBuilder.filter(termsQuery("keywords", keywordTerms));
		if (nonNull(authorTerms))
			queryBuilder.filter(termsQuery("author", authorTerms));
		return queryBuilder;
	}
}
