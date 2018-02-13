package com.rebel.consolidation.model;

import java.time.LocalDate;
import java.util.List;

public class Document {
	private String       title;
	private String       text;
	private List<String> keywords;
	private String       source;
	private String       author;
	private LocalDate    publicationDate;

	public String title() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String text() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> keywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String source() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String author() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public LocalDate publicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(LocalDate publicationDate) {
		this.publicationDate = publicationDate;
	}
}
