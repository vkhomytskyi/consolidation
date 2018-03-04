package com.rebel.consolidation.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtils {
	public static long toEpochMilli(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault())
				.toInstant().toEpochMilli();
	}

	public static long toEpochMilli(LocalDate localDate) {
		return localDate.atStartOfDay(ZoneId.systemDefault())
				.toInstant().toEpochMilli();
	}

	public static long random(LocalDate fromDate, LocalDate toDate) {
		long fromDateMillis = toEpochMilli(fromDate);
		long toDateMillis = toEpochMilli(toDate);

		long diff = toDateMillis - fromDateMillis;

		return fromDateMillis + ThreadLocalRandom.current().nextLong(diff);
	}
}
