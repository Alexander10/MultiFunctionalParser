package sk.ban.data;

import sk.ban.enums.PropertyType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BAN on 26. 1. 2015.
 */
public class DocumentContent implements Serializable {

	@PropertyInfo(label = "File name")
	private String fileName = "";

	@PropertyInfo(label = "Document title")
	private String title = "";

	private String subtitle = "";

	@PropertyInfo(label = "Authors", type = PropertyType.LIST)
	private List<String> authors = new ArrayList<>();

	@PropertyInfo(label = "Keywords", type = PropertyType.LIST)
	private List<String> keywords = new ArrayList<>();

	@PropertyInfo(label = "Abstract")
	private String abstractText = "";

	@PropertyInfo(label = "References", type = PropertyType.LIST)
	private List<String> references = new ArrayList<>();

	public DocumentContent() {
	}

	public DocumentContent(String fileName, String title, String subtitle, List<String> authors, List<String> keywords, String abstractText, List<String> references) {
		this.fileName = fileName;
		this.title = title;
		this.subtitle = subtitle;
		this.authors = authors;
		this.keywords = keywords;
		this.abstractText = abstractText;
		this.references = references;
	}

	public void addAuthor(String author) {
		this.authors.add(author);
	}

	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}

	public void addReference(String reference) {
		references.add(reference);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

	@Override
	public String toString() {
		return "DocumentContentData{" +
				"fileName='" + fileName + '\'' +
				", title='" + title + '\'' +
				", subtitle='" + subtitle + '\'' +
				", authors=" + authors +
				", keywords=" + keywords +
				", abstractText='" + abstractText + '\'' +
				", references=" + references +
				'}';
	}
}
