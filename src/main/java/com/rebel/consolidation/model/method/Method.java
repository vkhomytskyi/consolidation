package com.rebel.consolidation.model.method;

import com.rebel.consolidation.model.SearchStatistics;

import java.util.Map;

public interface Method {
	String getName();
	Map<String, Double> calculate(SearchStatistics stats);
}
