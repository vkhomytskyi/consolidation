package com.rebel.consolidation.model;

import java.util.List;

public class SearchResult {
	public Long           totalRequested;
	public Integer        totalCount;
	public Long           scopusRequested;
	public Long           scopusCount;
	public Long           rinzRequested;
	public Long           rinzCount;
	public Long           kpiRequested;
	public Long           kpiCount;
	public List<Document> documents;
}
