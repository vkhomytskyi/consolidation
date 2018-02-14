package com.rebel.consolidation.test;

import java.util.ArrayList;
import java.util.List;

public class TestDocumentGenerator {
	public static List<TestDocument> generate(
			int count,
			String source,
			String validText,
			String nonValidText,
			int percentage
	) {
		if (percentage > 100 || percentage < 0)
			throw new IllegalArgumentException("Wrong percentage value, must be < 100 && > 0");

		List<TestDocument> result = new ArrayList<>(count);

		int valid = count / 100 * percentage;

		for (int i = 0; i < valid; i++)
			result.add(new TestDocument(validText, source));

		for (int i = 0; i < count - valid; i++)
			result.add(new TestDocument(nonValidText, source));

		return result;
	}
}
