package sk.ban.parser;

import sk.ban.data.Document;

import java.io.File;

/**
 * Created by BAN on 9. 2. 2015.
 */
public interface Parserable {

	/**
	 *
	 * @param file - it should be from which we want to parse data
	 * @return filled Document with parsed data
	 */
	public Document parse(File file);
}
