package sk.ban.service;

import sk.ban.exception.ParserException;
import org.junit.Test;
import sk.ban.worker.DataCleaner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by USER on 28. 1. 2015.
 */
public class ParsedDataCleanerTest {



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
	public void cleanAuthorsWhenAuthorsAreInOneString(){
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
