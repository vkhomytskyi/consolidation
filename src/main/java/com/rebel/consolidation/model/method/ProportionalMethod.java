package com.rebel.consolidation.model.method;

import com.rebel.consolidation.model.Methods;
import com.rebel.consolidation.model.SearchStatistics;
import com.rebel.consolidation.test.Sources;

import java.util.HashMap;
import java.util.Map;

public class ProportionalMethod implements Method {

	@Override
	public String getName() {
		return Methods.PROPORTIONAL;
	}

	public Map<String, Double> calculate(SearchStatistics stats) {
		double scopusRatio = stats.scopusRequested != 0 ? 1D * stats.scopusFound / stats.scopusRequested : 0;
		double rinzRatio = stats.rinzRequested != 0 ? 1D * stats.rinzFound / stats.rinzRequested : 0;
		double kpiRatio = stats.kpiRequested != 0 ? 1D * stats.kpiFound / stats.kpiRequested : 0;

		double totalRatio = scopusRatio + rinzRatio + kpiRatio;

		Map<String, Double> ratios = new HashMap<>();
		ratios.put(Sources.SCOPUS.toLowerCase(), scopusRatio / totalRatio);
		ratios.put(Sources.RINZ.toLowerCase(), rinzRatio / totalRatio);
		ratios.put(Sources.KPI.toLowerCase(), kpiRatio / totalRatio);

		return ratios;
	}
}
