package sk.ban.worker;

import org.junit.Test;
import sk.ban.data.DocumentContent;
import sk.ban.exception.ParserException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DataCleanerTest {


	private static final String title = "Optimization of Faculty of transport and traffic sciences web based upon student's habits";
	private static final List<String> authors = new ArrayList<>(Arrays.asList("Darko Dujmović; Dragan Peraković", "", "#@Marko Periša"));
	private static final List<String> keywords = new ArrayList<>(Arrays.asList("formatting, HTML5", "", "#$Content", "Mobile phone"));


	private static final String abstractText = "Abstract - This paper introduces new web content technologies made to suit today's terminal devices and user habits. " +
			"First step is the analysis of current state in global telecommunications, and web content development technologies. Next step is to analyze FPZ's" +
			" students (Faculty of transport and traffic sciences – FPZ) trends both in telecommunications and content consumption. Further more, there is a need" +
			" to go over the improvements that HTML5 has over the previous generations, and finally the implementation of said technologies based upon the mentioned" +
			" analysis. The result is an optimized web page that displays properly no matter the device the user has.";

	@Test
	public void testCleanData() {
		DocumentContent content = new DocumentContent("", title, "", authors, keywords, abstractText, null);
		DataCleaner.cleanData(content);

		assertFalse(content.getAbstractText().contains("Abstract"));

		assertEquals(content.getAuthors(), Arrays.asList("Darko Dujmović", "Dragan Peraković", "Marko Periša"));

		assertEquals(content.getKeywords(), Arrays.asList("formatting", "HTML5", "Content", "Mobile phone"));
	}

	@Test
	public void cleanKeywordsWhenKeywordsAreInList() {
		List<String> keywords1 = new ArrayList<>(Arrays.asList("Keywords - key1", "key2"));
		try {
			DataCleaner cleaner = new DataCleaner();
			Method method = DataCleaner.class.getDeclaredMethod("cleanKeywords", List.class);
			List<String> cleanedKeywords = (List<String>) callMethodByReflection(method, cleaner, keywords1);

			assertEquals(2, cleanedKeywords.size());
			assertEquals("key1", cleanedKeywords.get(0));
			assertEquals("key2", cleanedKeywords.get(1));

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void cleanKeywordsWhenAllKeywordsAreInOneString() {
		List<String> keywords1 = new ArrayList<>(Arrays.asList(new String[]{"Key words - key1, key2"}));
		try {
			DataCleaner cleaner = new DataCleaner();
			Method method = DataCleaner.class.getDeclaredMethod("cleanKeywords", List.class);
			List<String> cleanedKeywords = (List<String>) callMethodByReflection(method, cleaner, keywords1);

			assertEquals(2, cleanedKeywords.size());
			assertEquals("key1", cleanedKeywords.get(0));
			assertEquals("key2", cleanedKeywords.get(1));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void cleanAuthorsWhenAuthorsAreInOneString() {
		List<String> authors = new ArrayList<>(Arrays.asList(new String[]{"- author1; author2, author3"}));
		try {
			DataCleaner cleaner = new DataCleaner();
			Method method = DataCleaner.class.getDeclaredMethod("cleanAuthors", List.class);
			List<String> cleanedAuthors = (List<String>) callMethodByReflection(method, cleaner, authors);

			assertEquals(3, cleanedAuthors.size());
			assertEquals("author1", cleanedAuthors.get(0));
			assertEquals("author2", cleanedAuthors.get(1));
			assertEquals("author3", cleanedAuthors.get(2));
		} catch (NoSuchMethodException e) {
			throw new ParserException("Cleaning author data is inccorect: " + e);
		}
	}


	/**
	 * Due to testing private method is not possible directly we use to
	 * invoke private methods Java reflection
	 *
	 * @param method
	 * @param obj
	 * @param args
	 * @return
	 */
	private Object callMethodByReflection(Method method, Object obj, Object args) {
		method.setAccessible(true);
		try {
			return method.invoke(obj, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}