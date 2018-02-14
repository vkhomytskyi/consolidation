package com.rebel.consolidation.model;

import java.time.LocalDate;
import java.util.List;

public class Document {
	public String       title;
	public String       text;
	public List<String> keywords;
	public String       source;
	public String       author;
	public LocalDate    publicationDate;

	@Override
	public String toString() {
		return "Document{" +
				"title='" + title + '\'' +
				", text='" + text + '\'' +
				", keywords=" + keywords +
				", source='" + source + '\'' +
				", author='" + author + '\'' +
				", publicationDate=" + publicationDate +
				'}';
	}
}
