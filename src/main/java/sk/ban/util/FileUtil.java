package sk.ban.util;

import sk.ban.enums.FileExtension;
import sk.ban.exception.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by USER on 26. 1. 2015.
 */
public class FileUtil {


	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * method retrieves all files in current directory
	 *
	 * @param dir
	 * @return
	 */
	public static List<File> getAllFilesInDir(File dir, FileExtension ... filesExt) {
		List<File> fileNames = new ArrayList<>();
		try {
			Files.walk(Paths.get(dir.toString()), 1).forEach(filePath -> {
				System.out.println(filePath);
				if (Files.isRegularFile(filePath) && matchAny(filePath, filesExt)) {
					fileNames.add(filePath.toFile());
				}
			});
		} catch (IOException e) {
			throw new ParserException("Problem with traversing via directory " + e);
		}
		return fileNames;
	}

	private static boolean matchAny(Path path, FileExtension[] exts) {
		return Arrays.stream(exts)
				.filter(ext -> ext.getName().equalsIgnoreCase(getFileExtension(path.toFile())))
				.count() == 0 ? false : true;
	}

	/**
	 * Return the file extension i. e. pdf, docx ...
	 * @param file
	 * @return
	 */
	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else return "";
	}

	/**
	 * find out whether file exists
	 * @param name
	 * @return
	 */
	public static boolean existsFile(String name){
		File file = new File(name);
		if(file.exists()){
			return true;
		}
		return false;
	}
	/**
	 * return current working directory, if path is file return dir if path is dir return current dir
	 * @param path
	 * @return
	 */
	public static File getWorkingDir(String path){

		if(!existsFile(path)){
			return null;
		}

		File file = new File(path);
		if(file.isDirectory()){
			return file;
		}
		return file.getParentFile();
	}

	public static String getFileNameWithoutExt(File file) {
		return getFileNameWithoutExt(file.getName());
	}

	public static String getFileNameWithoutExt(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new IllegalArgumentException("File:" + fileName);
		}
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	public static void openFile(String filePath) {
		if (filePath == null || filePath.isEmpty()) {
			log.info("File doesn't exist");
			return;
		}
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File(filePath));
			}
		} catch (IOException ioe) {
			throw new ParserException("Problem with opening file: " + filePath);
		}
	}

	public static FileExtension getFileType(File file) {
		if (file.getName().endsWith(FileExtension.DOCX.getName())) {
			return FileExtension.DOCX;
		}
		return FileExtension.PDF;
	}

	/**
	 * @param file - file(Zip file) which we want to open
	 * @return inputStream of word/document.xml
	 */
	public static Optional<InputStream> openDocx(File file) {

		Optional<InputStream> in;
		if (file.exists()) {
			System.out.println("exists");
		}

		try {
			ZipFile zip = new ZipFile(file);
			log.debug("open zip file");
			ZipEntry entry = zip.getEntry("word/document.xml");
			in = Optional.of(zip.getInputStream(entry));

		} catch (NullPointerException e) {
			throw new ParserException("Xml file document.xml in word directory doesn't exist: " + e);
		} catch (FileNotFoundException e) {
			throw new ParserException("Docx file could not be find: " + file.getName());
		} catch (IOException e) {
			throw new ParserException("Problem with Docx file by unziping: " + e);
		}

		return in;
	}
}
