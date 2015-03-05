package sk.ban.data;

import java.io.Serializable;

/**
 * Created by BAN on 26. 1. 2015.
 */
public class DocumentNavigation implements Serializable {

	@PropertyInfo(label = "Last Page Number")
	private String lastPage = "";

	@PropertyInfo(label = "Section Name")
	private String section = "";

	private final String cntOfPages = "";

	@PropertyInfo(label = "Start Page Number")
	private String startPage = "";

	private String sectionNumber = "";

	public DocumentNavigation() {

	}

	public String getLastPage() {
		return lastPage;
	}

	public String getSection() {
		return section;
	}

	public String getCntOfPages() {
		return cntOfPages;
	}

	public String getStartPage() {
		return startPage;
	}

	public String getSectionNumber() {
		return sectionNumber;
	}

	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	@Override
	public String toString() {
		return "DocumentAdditionalData{" +
				"lastPage='" + lastPage + '\'' +
				", section='" + section + '\'' +
				", cntOfPages='" + cntOfPages + '\'' +
				", startPage='" + startPage + '\'' +
				", sectionNumber='" + sectionNumber + '\'' +
				'}';
	}
}
