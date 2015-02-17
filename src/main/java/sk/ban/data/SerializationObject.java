package sk.ban.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by USER on 17. 2. 2015.
 */
public class SerializationObject implements Serializable {

	private List<TableRow> rows;
	private String path;
	private String conferenceName;
	private String conferenceYear;
	private LocalDate registrationDate;
	private LocalDate publishedDate;

	public SerializationObject(List<TableRow> rows, String path, String conferenceName, String conferenceYear, LocalDate registrationDate, LocalDate publishedDate) {
		this.rows = rows;
		this.path = path;
		this.conferenceName = conferenceName;
		this.conferenceYear = conferenceYear;
		this.registrationDate = registrationDate;
		this.publishedDate = publishedDate;
	}

	public List<TableRow> getRows() {
		return rows;
	}

	public String getPath() {
		return path;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public String getConferenceYear() {
		return conferenceYear;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public LocalDate getPublishedDate() {
		return publishedDate;
	}
}
