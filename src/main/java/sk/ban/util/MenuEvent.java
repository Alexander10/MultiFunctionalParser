package sk.ban.util;

import java.io.File;

/**
 * Created by USER on 16. 2. 2015.
 */
public class MenuEvent {
	private File file;

	public MenuEvent(File file){
		this.file = file;
	}

	public File getFile(){
		return this.file;
	}

}
