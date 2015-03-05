package sk.ban.worker;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import sk.ban.data.DocumentContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataValidatorTest {


	private static final String DOCUMENT = "testData/nase1.docx";
	private static final String TITLE = "Optimization of Faculty of transport and traffic sciences web based upon student's habits";
	private static final List<String> authors = new ArrayList<>(Arrays.asList("Darko Dujmović", "Dragan Peraković", "Marko Periša"));
	private static final List<String> keywords = new ArrayList<>(Arrays.asList("formatting", "HTML5", "Content", "Mobile phone"));
	private static final List<String> references = new ArrayList<>(Arrays.asList("Reference 1", "Reference 2", "Reference3", "Reference 4"));

	private static final String abstractText = "This paper introduces new web content technologies made to suit today's terminal devices and user habits. " +
			"First step is the analysis of current state in global telecommunications, and web content development technologies. Next step is to analyze FPZ's" +
			" students (Faculty of transport and traffic sciences – FPZ) trends both in telecommunications and content consumption. Further more, there is a need" +
			" to go over the improvements that HTML5 has over the previous generations, and finally the implementation of said technologies based upon the mentioned" +
			" analysis. The result is an optimized web page that displays properly no matter the device the user has.";
	private DocumentContent content = null;

	@Before
	public void setUp() {
		content = new DocumentContent(DOCUMENT, TITLE, "", authors, keywords, abstractText, references );

	}

	@Test
	public void testValidate_WithoutEmptinies() throws Exception {
		String result = DataValidator.validate(content, DOCUMENT);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testValidate_WithEmptinies(){
		content.setReferences(new ArrayList<>());
		String result = DataValidator.validate(content, DOCUMENT);
		assertFalse(result.isEmpty());
	}
}