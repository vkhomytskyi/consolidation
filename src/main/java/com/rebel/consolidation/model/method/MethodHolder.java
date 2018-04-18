package com.rebel.consolidation.model.method;

import com.rebel.consolidation.model.Methods;

import java.util.HashMap;
import java.util.Map;

public class MethodHolder {
	public static Map<String, Method> methods = new HashMap<>();

	static {
		methods.put(Methods.PROPORTIONAL, new ProportionalMethod());
	}

	private static void register() {

	}
}
