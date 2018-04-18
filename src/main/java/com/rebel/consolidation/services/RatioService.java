package com.rebel.consolidation.services;

import com.rebel.consolidation.model.Methods;
import com.rebel.consolidation.model.SearchStatistics;
import com.rebel.consolidation.test.Sources;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class RatioService {

	private final StatisticsService statisticsService;
	private final Logger logger = LoggerFactory.getLogger(RatioService.class);

	public RatioService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	private Map<String, Double> defaultMap() {
		Map<String, Double> map = new HashMap<>();
		map.put(Sources.SCOPUS.toLowerCase(), 1D / 3);
		map.put(Sources.RINZ.toLowerCase(), 1D / 3);
		map.put(Sources.KPI.toLowerCase(), 1D / 3);
		return map;
	}

	public Map<String, Double> getRatios(String query, String method) {
		SearchStatistics stats = statisticsService.lastResult(query);

		if (isNull(stats))
			return defaultMap();

		return getRatios(stats, method);
	}

	private Map<String, Double> getRatios(SearchStatistics statistics, String method) {
		switch (method) {
			case Methods.PROPORTIONAL:
				return proportional(statistics);
			case Methods.DUAL:
				return dual(statistics);
			default:
				return defaultMap();
		}
	}

	private Map<String, Double> proportional(SearchStatistics stats) {
		double scopusRatio = stats.scopusRequested != 0 ? 1D * stats.scopusFound / stats.scopusRequested : 0;
		double rinzRatio = stats.rinzRequested != 0 ? 1D * stats.rinzFound / stats.rinzRequested : 0;
		double kpiRatio = stats.kpiRequested != 0 ? 1D * stats.kpiFound / stats.kpiRequested : 0;

		double totalRatio = scopusRatio + rinzRatio + kpiRatio;

		logger.info("Ratio [Scopus]: {0}% [RINZ]: {1}% [KPI]: {2}%", scopusRatio / totalRatio, rinzRatio  / totalRatio, kpiRatio  / totalRatio);

		Map<String, Double> ratios = new HashMap<>();
		ratios.put(Sources.SCOPUS.toLowerCase(), scopusRatio / totalRatio);
		ratios.put(Sources.RINZ.toLowerCase(), rinzRatio / totalRatio);
		ratios.put(Sources.KPI.toLowerCase(), kpiRatio / totalRatio);

		return ratios;
	}

	private Map<String, Double> dual(SearchStatistics stats) {
		double scopusRatio = stats.scopusRequested != 0 ? 1D * stats.scopusFound / stats.scopusRequested : 0;
		double rinzRatio = stats.rinzRequested != 0 ? 1D * stats.rinzFound / stats.rinzRequested : 0;
		double kpiRatio = stats.kpiRequested != 0 ? 1D * stats.kpiFound / stats.kpiRequested : 0;

		Map<String, Double> ratios = new HashMap<>();
		ratios.put(Sources.SCOPUS.toLowerCase(), scopusRatio);
		ratios.put(Sources.RINZ.toLowerCase(), rinzRatio);
		ratios.put(Sources.KPI.toLowerCase(), kpiRatio);

//		Map<String, Double> ratios = new HashMap<>();
//		ratios.put(Sources.SCOPUS.toLowerCase(), scopusRatio / totalRatio);
//		ratios.put(Sources.RINZ.toLowerCase(), rinzRatio / totalRatio);
//		ratios.put(Sources.KPI.toLowerCase(), kpiRatio / totalRatio);

		return ratios;
	}
}
