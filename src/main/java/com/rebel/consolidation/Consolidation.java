package com.rebel.consolidation;

import com.rebel.consolidation.services.ElasticClient;

public class Consolidation {

	private ElasticClient elasticClient;

	public Consolidation() {
		this.elasticClient = new ElasticClient();
	}

	public void run() {

	}

	public static void main(String[] args) throws Exception {
		Consolidation consolidation = new Consolidation();
		consolidation.run();
	}
}
