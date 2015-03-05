package sk.ban.enums;

import java.util.stream.Stream;

/**
 * Created by BAN on 21. 1. 2015.
 */
public enum DocumentPart {

	ABSTRACT("abstract"), KEYWORDS("keywords"), REFERENCES("references"), AUTHOR("author"), TITLE("papertitle"), SUBTITLE("papersubtitle");
	private final String name;

	DocumentPart(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static boolean contains(String value) {
		return Stream.of(DocumentPart.values())
				.filter(element -> element.getName().equalsIgnoreCase(value))
				.count() != 0;
	}

	public static DocumentPart fromString(String value){
		return Stream.of(DocumentPart.values())
				.filter(element -> element.getName().equalsIgnoreCase(value))
				.findFirst().get();
	}
}
