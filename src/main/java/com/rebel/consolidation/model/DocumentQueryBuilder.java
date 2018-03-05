package com.rebel.consolidation.model;

import com.rebel.consolidation.util.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.time.LocalDate;

import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

public class DocumentQueryBuilder {
	private String[] titleTerms;
	private String[] textTerms;
	private String[] keywordTerms;
	private String[] sources;
	private String[] authorTerms;
	private Long fromId = 0L;
	private Long toId;
	private Long fromDate;
	private Long tillDate;


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

	public DocumentQueryBuilder source(String... sources) {
		this.sources = sources;
		return this;
	}

	public DocumentQueryBuilder limit(Long to) {
		this.toId = to;
		return this;
	}

	public DocumentQueryBuilder limit(Long from, Long to) {
		this.fromId = from;
		this.toId = to;
		return this;
	}

	public DocumentQueryBuilder fromDate(LocalDate fromDate) {
		return fromDate(DateUtils.toEpochMilli(fromDate));
	}

	public DocumentQueryBuilder tillDate(LocalDate tillDate) {
		return tillDate(DateUtils.toEpochMilli(tillDate));
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
		BoolQueryBuilder queryBuilder = boolQuery();
		if (nonNull(textTerms))
			queryBuilder.filter(termsQuery("text", textTerms));
		if (nonNull(titleTerms))
			queryBuilder.filter(termsQuery("title", titleTerms));
		if (nonNull(keywordTerms))
			queryBuilder.filter(termsQuery("keywords", keywordTerms));
		if (nonNull(authorTerms))
			queryBuilder.filter(termsQuery("author", authorTerms));
		if (nonNull(sources))
			queryBuilder.filter(termsQuery("source", sources));

		if (nonNull(fromId) || nonNull(toId)) {
			RangeQueryBuilder rangeQuery = rangeQuery("id");
			if (nonNull(fromId))
				rangeQuery.from(fromId);
			if (nonNull(toId))
				rangeQuery.to(toId);
			queryBuilder.filter(rangeQuery);
		}

		if (nonNull(fromDate) || nonNull(tillDate)) {
			RangeQueryBuilder rangeQuery = rangeQuery("publicationDate");
			if (nonNull(fromDate))
				rangeQuery.from(fromDate);
			if (nonNull(tillDate))
				rangeQuery.to(tillDate);
			queryBuilder.filter(rangeQuery);
		}

		return queryBuilder;
	}
}
