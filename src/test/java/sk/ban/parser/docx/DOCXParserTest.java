package sk.ban.parser.docx;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import sk.ban.data.Document;
import sk.ban.exception.ParserException;
import sk.ban.util.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class DOCXParserTest {

	private static final String WORD_DOCUMENT = "testData/nase1.docx";
	private static final String DOCX_TITLE = "Optimization of Faculty of transport and traffic sciences web based upon student's habits";
	private static final List<String> authors = new ArrayList<>(Arrays.asList("Darko Dujmović", "Dragan Peraković", "Marko Periša"));
	private static final List<String> keywords = new ArrayList<>(Arrays.asList("formatting", "HTML5", "Content", "Mobile phone"));

	private static final String abstractText = "This paper introduces new web content technologies made to suit today's terminal devices and user habits. " +
			"First step is the analysis of current state in global telecommunications, and web content development technologies. Next step is to analyze FPZ's" +
			" students (Faculty of transport and traffic sciences – FPZ) trends both in telecommunications and content consumption. Further more, there is a need" +
			" to go over the improvements that HTML5 has over the previous generations, and finally the implementation of said technologies based upon the mentioned" +
			" analysis. The result is an optimized web page that displays properly no matter the device the user has.";

	private URL url;


	@Before
	public void setUp() {
		url = Thread.currentThread().getContextClassLoader().getResource(WORD_DOCUMENT);
	}

	@Test
	public void testParse() throws Exception {
		File file = new File(url.getPath());
		DOCXParser parser = new DOCXParser();
		Document result = parser.parse(file);

		assertEquals(DOCX_TITLE, result.getContentData().getTitle());
		Assert.assertEquals(abstractText, result.getContentData().getAbstractText());
		assertEquals(12, result.getContentData().getReferences().size());
		assertEquals(authors.stream().collect(Collectors.joining(";")), result.getContentData().getAuthors().stream().collect(Collectors.joining(";")));
		assertEquals(keywords.stream().collect(Collectors.joining(";")), result.getContentData().getKeywords().stream().collect(Collectors.joining(";")));
	}

	@Test
	public void openExistedDocxFile() {

		File file = new File(url.getPath());
		Optional<InputStream> opt = FileUtil.openDocx(file);
		assertTrue(opt.isPresent());

	}

	@Test(expected = ParserException.class)
	public void openNoneExistedFile() {
		//this should be not found proper file
		File file = new File(WORD_DOCUMENT);
		DOCXParser parser = new DOCXParser();
		Document dto = parser.parse(file);

	}
}