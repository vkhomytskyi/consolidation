package com.rebel.consolidation.services;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import com.rebel.consolidation.model.SearchMemory;
import com.rebel.consolidation.util.JsonUtils;
import io.vertx.core.json.JsonObject;

import java.util.Comparator;

import static java.util.stream.Collectors.toList;

public class MemoryService {

	private HazelcastInstance hazelcastInstance;

	public MemoryService() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setNetworkConfig(new ClientNetworkConfig().addAddress("127.0.0.1:5701"));
		hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
	}

	public void saveMemory(String id, SearchMemory searchMemory) {
		memoryMap().put(id, searchMemory);
	}

	public JsonObject memories() {
		MultiMap<String, SearchMemory> multiMap = memoryMap();

		JsonObject result = new JsonObject();

		for (String id : multiMap.keySet()) {
			result.put(
					id,
					JsonUtils.toJson(multiMap.get(id).stream().sorted(Comparator.comparing(m -> m.date)).collect(toList()))
			);
		}

		return result;
	}

	private MultiMap<String, SearchMemory> memoryMap() {
		return hazelcastInstance.getMultiMap("memory-map");
	}
}
