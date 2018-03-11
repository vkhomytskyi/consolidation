package com.rebel.consolidation.services;

import com.rebel.consolidation.model.SearchStatistics;
import com.rebel.consolidation.test.Sources;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class RatioService {

	private final StatisticsService statisticsService;

	public RatioService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	private Map<String, Long> defaultMap() {
		Map<String, Long> map = new HashMap<>();
		map.put(Sources.SCOPUS.toLowerCase(), 33L);
		map.put(Sources.RINZ.toLowerCase(), 33L);
		map.put(Sources.KPI.toLowerCase(), 33L);
		return map;
	}

	public Map<String, Long> getRatios(String query) {
		SearchStatistics stats = statisticsService.lastResult(query);

		if (isNull(stats))
			return defaultMap();

		double scopusRatio = stats.scopusRequested != 0 ? 1D * stats.scopusFound / stats.scopusRequested : 0;
		double rinzRatio = stats.rinzRequested != 0 ?  1D * stats.rinzFound / stats.rinzRequested : 0;
		double kpiRatio = stats.kpiRequested != 0 ? 1D * stats.kpiFound / stats.kpiRequested : 0;
		double totalRatio = scopusRatio + rinzRatio + kpiRatio;

		Map<String, Long> ratios = new HashMap<>();
		ratios.put(Sources.SCOPUS.toLowerCase(), Math.round(scopusRatio / totalRatio * 100));
		ratios.put(Sources.RINZ.toLowerCase(), Math.round(rinzRatio / totalRatio * 100));
		ratios.put(Sources.KPI.toLowerCase(), Math.round(kpiRatio / totalRatio * 100));

		return ratios;
	}
}
