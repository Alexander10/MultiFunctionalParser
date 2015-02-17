package sk.ban.parser;

import sk.ban.data.Document;

import java.io.File;

/**
 * Created by USER on 9. 2. 2015.
 */
public interface Parserable {

	public Document parse(File file);
}
