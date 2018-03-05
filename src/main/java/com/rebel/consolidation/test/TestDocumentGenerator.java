package com.rebel.consolidation.test;

import com.github.javafaker.Faker;
import com.rebel.consolidation.util.DateUtils;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class TestDocumentGenerator {

	private static final Logger logger = LoggerFactory.getLogger(TestDocumentGenerator.class);
	private static final Faker  FAKER  = new Faker();

	private TestDocumentGenerator() {}

	public static List<TestDocument> generate(
			int count,
			String source,
			String validText,
			String nonValidText,
			int percentage
	) {
		logger.info("Started generating for " + source);

		AtomicLong counter = new AtomicLong();

		if (percentage > 100 || percentage < 0)
			throw new IllegalArgumentException("Wrong percentage value, must be < 100 && > 0");

		List<TestDocument> result = new ArrayList<>(count);

		int valid = count / 100 * percentage;

		for (int i = 0; i < valid; i++) {
			result.add(new TestDocument(FAKER.book().title(), randomText() + validText, source, FAKER.book().author(), randomDate(), randomKeywords()));
		}

		for (int i = 0; i < count - valid; i++) {
			result.add(new TestDocument(FAKER.book().title(), randomText() + nonValidText, source, FAKER.book().author(), randomDate(), randomKeywords()));
		}

		Collections.shuffle(result);

		result.forEach(d -> d.id = counter.getAndIncrement());

		logger.info(source + " ready!");

		return result;
	}

	private static long randomDate() {
		return DateUtils
				.random(
						LocalDate.of(1989, 1, 1),
						LocalDate.now()
				);
	}

	private static List<String> randomKeywords() {
		int size = ThreadLocalRandom.current().nextInt(3, 6);
		return FAKER.lorem().words(size);
	}

	private static String randomText() {
		return FAKER.lorem().paragraph(5) + " ";
	}
}
