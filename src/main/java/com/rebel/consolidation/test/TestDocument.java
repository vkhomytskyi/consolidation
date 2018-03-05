package com.rebel.consolidation.test;

import com.rebel.consolidation.model.Document;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestDocument extends Document {
	public TestDocument(
			String title,
			String text,
			String source,
			String author,
			Long publicationDate,
			List<String> keywords
	) {
		this.title = title;
		this.text = text;
		this.source = source;
		this.author = author;
		this.publicationDate = publicationDate;
		this.keywords = keywords;
	}
}
