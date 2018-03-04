package com.rebel.consolidation.test;

import com.rebel.consolidation.model.Document;

import java.time.LocalDate;
import java.util.Arrays;

public class TestDocument extends Document {
	public TestDocument() {
		this.title = "Test Document";
		this.keywords = Arrays.asList("keyword1", "keyword2", "keyword3", "keyword4");
		this.author = "Test Author";
	}

	public TestDocument(String text, String source, Long date) {
		this();
		this.text = text;
		this.source = source;
		this.publicationDate = date;
	}
}
