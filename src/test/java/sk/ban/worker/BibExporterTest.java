package sk.ban.worker;

import org.junit.Before;
import org.junit.Test;
import sk.ban.data.Document;
import sk.ban.data.DocumentContent;
import sk.ban.data.DocumentNavigation;
import sk.ban.util.StringUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BibExporterTest {

	private static final String title = "Optimization of Faculty of transport and traffic sciences web based upon student's habits";
	private static final List<String> authors = new ArrayList<>(Arrays.asList("Darko Dujmović", "Dragan Peraković", "Marko Periša"));
	private static final List<String> keywords = new ArrayList<>(Arrays.asList("formatting", "HTML5", "Content", "Mobile phone"));
	private static final String startPage = "485";
	private static final String lastPage = "489";
	private static final String sectionTitle = "Informatics, Artificial Intelligence, Mobile Computing,Open Source, World Wide Web";
	private static final String fileName = "file.pdf";

	private static final String abstractText = "This paper introduces new web content technologies made to suit today's terminal devices and user habits. " +
			"First step is the analysis of current state in global telecommunications, and web content development technologies. Next step is to analyze FPZ's" +
			" students (Faculty of transport and traffic sciences – FPZ) trends both in telecommunications and content consumption. Further more, there is a need" +
			" to go over the improvements that HTML5 has over the previous generations, and finally the implementation of said technologies based upon the mentioned" +
			" analysis. The result is an optimized web page that displays properly no matter the device the user has.";

	private static String dir = "";
	private static final String EMPTY_FILE = "testData";

	@Before
	public void setUp() {
		dir = Thread.currentThread().getContextClassLoader().getResource(EMPTY_FILE).getPath();
	}

	@Test
	public void testExportToFile() throws Exception {
		List<Document> documents = Arrays.asList(getDocument(), getDocument());
		File bibFile = new File(dir);

		BibExporter exporter = new BibExporter("Conference", "23-02-2015", "23-02-2015", "23-02-2015");
		exporter.exportToFile(bibFile, documents);
		if (!exporter.getPathToExportedFile().isPresent()) {
			throw new NullPointerException();
		}
		String content = new String(Files.readAllBytes(Paths.get(exporter.getPathToExportedFile().get())));
		assertEquals(2, StringUtil.countMatches(content, title));
		assertEquals(2, StringUtil.countMatches(content, lastPage));
		assertEquals(2, StringUtil.countMatches(content, startPage));
		assertEquals(2, StringUtil.countMatches(content, sectionTitle));
		new File(exporter.getPathToExportedFile().get()).delete();

	}

	@Test
	public void testExportToString() throws Exception {

		Document document = getDocument();
		String result = BibExporter.exportToString(document);
		assertFalse(result.isEmpty());
		assertTrue(result.contains(title));
		assertTrue(result.contains(abstractText));
		assertTrue(result.contains(lastPage));
		assertTrue(result.contains(startPage));
		assertTrue(result.contains(sectionTitle));
	}

	@Test
	public void testExportToStringWithEmptyDocument() {
		String result = BibExporter.exportToString(null);
		assertTrue(result.isEmpty());

	}

	private static  Document getDocument() {
		DocumentContent content = new DocumentContent();
		DocumentNavigation navigation = new DocumentNavigation();

		content.setFileName(fileName);
		content.setAuthors(authors);
		content.setTitle(title);
		content.setKeywords(keywords);
		content.setAbstractText(abstractText);

		navigation.setStartPage(startPage);
		navigation.setLastPage(lastPage);
		navigation.setSection(sectionTitle);

		return new Document(navigation, content);

	}
}