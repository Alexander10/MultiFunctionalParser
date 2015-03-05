package sk.ban.worker;

import sk.ban.data.DocumentContent;
import sk.ban.util.Constants;
import sk.ban.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by BAN on 27. 1. 2015.
 */
public class DataCleaner {

	/**
	 * Clean keywords, authors, abstract data
	 * @param dto
	 */
	public static void cleanData(DocumentContent dto) {

		dto.setKeywords(cleanKeywords(dto.getKeywords()));
		dto.setAuthors(cleanAuthors(dto.getAuthors()));
		dto.setAbstractText(cleanAbstract(dto.getAbstractText()));
	}

	/**
	 * Clean kewords - means
	 * 1) that data contains some string with semicolons this string will be splited into more keywords
	 * 2) if some keyword contains keyword word this word will be deleted
	 * 3) if some keyword contains at start none alphabetic characters these characters will be deleted
	 * 4) trim data
	 * @param keywordsList
	 * @return
	 */
	private static List<String> cleanKeywords(List<String> keywordsList) {
		return keywordsList.stream()
				.map((String keyword) -> StringUtil.removeFirstUselessWord(Constants.KEYWORDS, keyword))
				.map(StringUtil::splitData)
				.flatMap(Arrays::stream)
				.map(StringUtil::removeStartNoneAlphanumericString)
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
		return StringUtil.removeStartNoneAlphanumericString(abst);
	}

	/**
	 * Clean authors - means
	 * 1) that data contains some string with semicolons this string will be splited into more authors
	 * 2) if some authors contains at start none alphabetic characters these characters will be deleted
	 * 3) trim data
	 * @param authors
	 * @return
	 */
	private static List<String> cleanAuthors(List<String> authors) {
		return authors.stream()
				.map(StringUtil::splitData)
				.flatMap(Arrays::stream)
				.map(StringUtil::removeStartNoneAlphanumericString)
				.filter( text -> !text.trim().isEmpty())
				.collect(Collectors.toList());
	}



}
