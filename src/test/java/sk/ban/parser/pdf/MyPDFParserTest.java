package sk.ban.parser.pdf;

import org.junit.Before;
import org.junit.Test;
import sk.ban.data.Document;
import sk.ban.exception.ParserException;
import sk.ban.parser.Parserable;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;

public class MyPDFParserTest {

	private static final String PDF_DOCUMENT = "testData/nase1.pdf";
	private static URL url;
	private static final String title = "Optimization of Faculty of transport and traffic sciences web based upon student's habits";
	private static final List<String> authors = new ArrayList<>(Arrays.asList("Darko Dujmović", "Dragan Peraković", "Marko Periša"));
	private static final List<String> keywords = new ArrayList<>(Arrays.asList("formatting", "HTML5", "Content", "Mobile phone"));
	private static final String startPage = "485";
	private static final String lastPage =  "489";
	private static final String sectionTitle = "Informatics, Artificial Intelligence, Mobile Computing,Open Source, World Wide Web";

	private static final String abstractText = "This paper introduces new web content technologies made to suit today's terminal devices and user habits. " +
			"First step is the analysis of current state in global telecommunications, and web content development technologies. Next step is to analyze FPZ's" +
			" students (Faculty of transport and traffic sciences – FPZ) trends both in telecommunications and content consumption. Further more, there is a need" +
			" to go over the improvements that HTML5 has over the previous generations, and finally the implementation of said technologies based upon the mentioned" +
			" analysis. The result is an optimized web page that displays properly no matter the device the user has.";

	@Before
	public void init() {
		url = Thread.currentThread().getContextClassLoader().getResource(PDF_DOCUMENT);
	}


	@Test(expected = ParserException.class)
	public void noneValidFile() {

		MyPDFParser parser = new MyPDFParser();
		parser.parse(new File(url.getPath() + "s"));
	}

	@Test
	public void testParse() throws Exception {
		Parserable parser = new MyPDFParser();
		Document result = parser.parse(new File(url.getPath()));

		//content data
		assertEquals(12,result.getContentData().getReferences().size());
		assertEquals(title, result.getContentData().getTitle());
		assertEquals(abstractText, result.getContentData().getAbstractText());
		assertEquals(authors.stream().collect(Collectors.joining(",")), result.getContentData().getAuthors().stream().collect(Collectors.joining(",")));
		assertEquals(keywords.stream().collect(Collectors.joining(",")), result.getContentData().getKeywords().stream().collect(Collectors.joining(",")));

		//section data
		assertEquals(sectionTitle, result.getAdditionalData().getSection());
		assertEquals(lastPage, result.getAdditionalData().getLastPage());
		assertEquals(startPage, result.getAdditionalData().getStartPage());

	}
}