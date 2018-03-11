package com.rebel.consolidation.model;

import java.io.Serializable;

public class SearchStatistics implements Serializable {

	public Long    totalRequested;
	public Integer totalFound;
	public Long    scopusRequested;
	public Long    scopusFound;
	public Long    rinzRequested;
	public Long    rinzFound;
	public Long    kpiRequested;
	public Long    kpiFound;
	public Long    date;

	public SearchStatistics(
			Long totalRequested,
			Integer totalFound,
			Long scopusRequested,
			Long scopusFound,
			Long rinzRequested,
			Long rinzFound,
			Long kpiRequested,
			Long kpiFound,
			Long date
	) {
		this.totalRequested = totalRequested;
		this.totalFound = totalFound;
		this.scopusRequested = scopusRequested;
		this.scopusFound = scopusFound;
		this.rinzRequested = rinzRequested;
		this.rinzFound = rinzFound;
		this.kpiRequested = kpiRequested;
		this.kpiFound = kpiFound;
		this.date = date;
	}

	@Override
	public String toString() {
		return "SearchStatistics{" +
				"totalRequested=" + totalRequested +
				", totalFound=" + totalFound +
				", scopusRequested=" + scopusRequested +
				", scopusFound=" + scopusFound +
				", rinzRequested=" + rinzRequested +
				", rinzFound=" + rinzFound +
				", kpiRequested=" + kpiRequested +
				", kpiFound=" + kpiFound +
				", date=" + date +
				'}';
	}
}
