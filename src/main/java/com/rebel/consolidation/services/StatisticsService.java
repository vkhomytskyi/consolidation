package com.rebel.consolidation.services;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import com.rebel.consolidation.model.SearchStatistics;
import com.rebel.consolidation.util.JsonUtils;
import io.vertx.core.json.JsonObject;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StatisticsService {

	private final HazelcastInstance hazelcastInstance;

	public StatisticsService() {
//		ClientConfig clientConfig = new ClientConfig();
//		clientConfig.setNetworkConfig(new ClientNetworkConfig().addAddress("127.0.0.1:5701"));
//		hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
		hazelcastInstance = Hazelcast.newHazelcastInstance();
	}

	public void saveResult(String id, SearchStatistics searchMemory) {
		System.out.println("Saving history : " + id + " " + searchMemory);
		historyMap().put(id, searchMemory);
	}

	public SearchStatistics lastResult(String query) {
		List<SearchStatistics> history = history(query);
		System.out.println("History for: " + query + " " + history);
		if (history.isEmpty())
			return null;
		else
			return new LinkedList<>(history).getLast();
	}

	public List<SearchStatistics> history(String query) {
		MultiMap<String, SearchStatistics> multiMap = historyMap();

		return multiMap.get(query)
				.stream()
				.sorted(Comparator.comparing(m -> m.date))
				.collect(toList());
	}

	public JsonObject historyJson() {
		MultiMap<String, SearchStatistics> multiMap = historyMap();

		JsonObject result = new JsonObject();

		for (String id : multiMap.keySet())
			result.put(id, JsonUtils.toJson(history(id)));

		return result;
	}

	private MultiMap<String, SearchStatistics> historyMap() {
		return hazelcastInstance.getMultiMap("history-map");
	}
}