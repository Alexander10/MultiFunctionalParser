package sk.ban.enums;

/**
 * Created by USER on 7. 2. 2015.
 */
public enum FileExtension {
	PDF("pdf"), DOCX("docx");
	private String name;

	FileExtension(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
