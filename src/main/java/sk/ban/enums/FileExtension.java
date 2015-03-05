package sk.ban.enums;

/**
 * Created by BAN on 7. 2. 2015.
 */
public enum FileExtension {

	PDF("pdf"), DOCX("docx");
	private final String name;

	FileExtension(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
