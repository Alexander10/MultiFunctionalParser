package sk.ban.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by USER on 28. 1. 2015.
 */
public class StringUtil {

	/**
	 * @param data
	 * @return
	 */
	public static String[] parseDataByDelimiter(String data) {
		//String is immutable
		data = data.replaceAll(";", ",");

		//we suppose that, when string contains delimiter it will split data to array
		if (data.contains(",")) {
			return data.split(",");
		}

		return new String[]{data};
	}
	/**
	 * Remove none alphabetic characters from string
	 * look on first 5 characters and remove all none alphabetic characters
	 *
	 * @param text
	 * @return
	 */
	public static String removeNoneAlphabeticCharcters(String text) {
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]{0,5}");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			return text.substring(matcher.end(), text.length());
		}
		return text;
	}

	/**
	 * From string remove all none digits characters. After calling this method string could contain only digits
	 * @param text
	 * @return
	 */
	public static String removeNoneDigitCharacters(String text){
		return text.replaceAll("[^\\d.]","");
	}

	/**
	 * list contains all useless words if some useless word is find in text
	 * this word is removed and method return text without this removed word
	 *
	 * @param values - useless words
	 * @param text -
	 * @return
	 */
	public static String removeFirstUselessWord(List<String> values, String text) {
		Optional<String> opt = getMatchedText(values, text);
		if (opt.isPresent()) {
			return text.replace(opt.get(), "");
		}
		return text;
	}

	public static String removeFirstUselessWord(String value, String text){
		return text.replace(value,"");
	}

	private static final Optional<String> getMatchedText(List<String> values, String text) {
		return values.stream()
				.filter(name -> text.toLowerCase().contains(name.toLowerCase())).findFirst();
	}

	public static final boolean containsBoundedDigit(String text){
		return text.trim().matches("[-]\\s+\\d+\\s+[-]");
	}

}
