package sk.ban.worker;

import sk.ban.data.DocumentContent;
import sk.ban.util.Constants;
import sk.ban.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by USER on 27. 1. 2015.
 */
public class DataCleaner {

	public static void cleanData(DocumentContent dto) {

		dto.setKeywords(cleanKeywords(dto.getKeywords()));
		dto.setAuthors(cleanAuthors(dto.getAuthors()));
		dto.setAbstractText(cleanAbstract(dto.getAbstractText()));

	}

	private static List<String> cleanKeywords(List<String> keywordsList) {
		return keywordsList.stream()
				.map((String keyword) -> StringUtil.removeFirstUselessWord(Constants.KEYWORDS, keyword))
				.map((String keyword) -> StringUtil.parseDataByDelimiter(keyword))
				.flatMap(Arrays::stream)
				.map(StringUtil::removeNoneAlphabeticCharcters)
				.filter( text -> !text.trim().isEmpty())
				.collect(Collectors.toList());


	}

	/**
	 * Method removes Abstract keyword from the beginning of string
	 * and also all none important character from beginning (Abstract - )
	 *
	 * @param abst
	 * @return
	 */
	private static String cleanAbstract(String abst) {
		abst = abst.replace(Constants.ABSTRACT, "");
		return StringUtil.removeNoneAlphabeticCharcters(abst);
	}

	private static List<String> cleanAuthors(List<String> authors) {
		return authors.stream()
				.map(StringUtil::parseDataByDelimiter)
				.flatMap(Arrays::stream)
				.map((String author) -> StringUtil.removeNoneAlphabeticCharcters(author))
				.filter( text -> !text.trim().isEmpty())
				.collect(Collectors.toList());
	}



}
