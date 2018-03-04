package com.rebel.consolidation.handler.model;

import com.rebel.consolidation.model.Document;

import java.util.List;

public class SearchResult<T extends Document> {
	private final Integer totalCount;
	private final List<T> documents;

	public SearchResult(List<T> documents) {
		this.totalCount = documents.size();
		this.documents = documents;
	}

	public Integer totalCount() {
		return totalCount;
	}

	public List<T> documents() {
		return documents;
	}
}
