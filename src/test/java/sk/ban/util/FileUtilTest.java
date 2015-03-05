package sk.ban.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.ban.enums.FileExtension;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilTest {

	private File dir = new File("testDir");
	private File file1 = new File(dir, "file1.docx");
	private File file2 = new File(dir, "file2.pdf");

	@Before
	public void setUp() throws Exception {
		dir.mkdir();
		file1.createNewFile();
		file2.createNewFile();
	}

	@After
	public void finish() {
		dir.delete();
	}

	@Test
	public void testGetAllFilesInDir() throws Exception {

		List<File> files = FileUtil.getAllFilesInDir(dir, FileExtension.PDF, FileExtension.DOCX);

		assertTrue(files.stream().filter(file -> file.getName().equalsIgnoreCase("file1.docx")).count() == 1);
		assertTrue(files.stream().filter(file -> file.getName().equalsIgnoreCase("file2.pdf")).count() == 1);

	}

	@Test
	public void testExistsFile() throws Exception {

		assertTrue(FileUtil.existsFile(file1.getAbsolutePath()));

		File noneCreatedFile = new File("noneFile");
		assertFalse(FileUtil.existsFile(noneCreatedFile.getAbsolutePath()));

	}

	@Test
	public void testGetWorkingDir() throws Exception {

		String absolutePathDir = dir.getAbsolutePath();

		assertEquals(absolutePathDir, FileUtil.getWorkingDir(dir.getAbsolutePath()).getAbsolutePath());

		assertEquals(absolutePathDir, FileUtil.getWorkingDir(file1.getAbsolutePath()).getAbsolutePath());
	}


	@Test
	public void testGetFileType() throws Exception {
		assertEquals(FileExtension.DOCX, FileUtil.getFileType(file1));
		assertEquals(FileExtension.PDF, FileUtil.getFileType(file2));

	}

	public void testOpenDocx() throws Exception {

	}
}