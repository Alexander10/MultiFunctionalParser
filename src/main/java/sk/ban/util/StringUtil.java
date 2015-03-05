package sk.ban.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BAN on 28. 1. 2015.
 */
public class StringUtil {

	/**
	 * Method split data by semicolons or by commas
	 *
	 * @param data
	 * @return
	 */
	public static String[] splitData(String data) {
		data = data.replaceAll(";", ",");

		//we suppose that, when string contains delimiter it will split data to array
		if (data.contains(",")) {
			return Arrays.stream(data.split(",")).filter(str -> !str.isEmpty()).toArray(String[]::new);
		}

		return Arrays.stream(new String[]{data}).filter(str -> !str.isEmpty()).toArray(String[]::new);
	}

	/**
	 * Remove none alphabetic characters from string
	 * look from left on first 5 characters and remove from left all none alphabetic characters
	 *
	 * @param text
	 * @return
	 */
	public static String removeStartNoneAlphanumericString(String text) {
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]{0,5}");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			return text.substring(matcher.end(), text.length());
		}
		return text;
	}

	/**
	 * From string remove all none digits characters. After calling this method string could contain only digits
	 *
	 * @param text
	 * @return
	 */
	public static String removeNoneDigitCharacters(String text) {
		return text.replaceAll("[^\\d.]", "");
	}

	/**
	 * list contains all useless words if some useless word is find in text
	 * this word is removed and method return text without this removed word
	 *
	 * @param values - useless words
	 * @param text   -
	 * @return
	 */
	public static String removeFirstUselessWord(List<String> values, String text) {
		Optional<String> opt = getMatchedText(values, text);
		if (opt.isPresent()) {
			return text.replace(opt.get(), "");
		}
		return text;
	}

	private static Optional<String> getMatchedText(List<String> values, String text) {
		return values.stream()
				.filter(name -> text.toLowerCase().contains(name.toLowerCase()))
				.findFirst();
	}

	/**
	 * Check whether input string contains string which match to following predicate - digits -
	 *
	 * @param text
	 * @return
	 */
	public static boolean containsBoundedDigit(String text) {
		return text.trim().matches("[-]\\s+\\d+\\s+[-]");
	}

	/**
	 * Count number of occurences of subtring in string
	 *
	 * @param string
	 * @param substring
	 * @return
	 */
	public static int countMatches(String string, String substring) {

		Pattern p = Pattern.compile(substring);
		Matcher m = p.matcher(string);
		int count = 0;
		while (m.find()) {
			count += 1;
		}

		return count;
	}

}
