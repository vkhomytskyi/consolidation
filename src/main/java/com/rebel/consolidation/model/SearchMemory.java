package com.rebel.consolidation.model;

import java.io.Serializable;

public class SearchMemory implements Serializable {
	public Integer totalFound;
	public Long    scopusFound;
	public Long    rinzFound;
	public Long    kpiFound;
	public Long    date;

	public SearchMemory(
			Integer totalFound,
			Long scopusFound,
			Long rinzFound,
			Long kpiFound,
			Long date
	) {
		this.totalFound = totalFound;
		this.scopusFound = scopusFound;
		this.rinzFound = rinzFound;
		this.kpiFound = kpiFound;
		this.date = date;
	}
}
