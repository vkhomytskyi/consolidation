package com.rebel.consolidation.test;

import com.rebel.consolidation.util.DateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TestDocumentGenerator {
	public static List<TestDocument> generate(
			int count,
			String source,
			String validText,
			String nonValidText,
			int percentage
	) {
		AtomicLong counter = new AtomicLong();

		if (percentage > 100 || percentage < 0)
			throw new IllegalArgumentException("Wrong percentage value, must be < 100 && > 0");

		List<TestDocument> result = new ArrayList<>(count);

		int valid = count / 100 * percentage;

		for (int i = 0; i < valid; i++)
			result.add(new TestDocument(validText, source, randomDate()));

		for (int i = 0; i < count - valid; i++)
			result.add(new TestDocument(nonValidText, source, randomDate()));

		Collections.shuffle(result);

		result.forEach(d -> d.id = counter.getAndIncrement());

		return result;
	}

	private static long randomDate() {
		return DateUtils
				.random(
						LocalDate.of(1989, 1, 1),
						LocalDate.now()
				);
	}
}
