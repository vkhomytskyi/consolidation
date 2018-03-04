package com.rebel.consolidation.model;

import java.util.List;

public class Document {
	public Long         id;
	public String       title;
	public String       text;
	public List<String> keywords;
	public String       source;
	public String       author;
	public Long         publicationDate;

	@Override
	public String toString() {
		return "Document{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", text='" + text + '\'' +
				", keywords=" + keywords +
				", source='" + source + '\'' +
				", author='" + author + '\'' +
				", publicationDate=" + publicationDate +
				'}';
	}
}
