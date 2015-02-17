package sk.ban.service;

import org.junit.Test;
import sk.ban.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by USER on 30. 1. 2015.
 */
public class StringUtilTest {


	@Test
	public void dataParsedByDelimiter(){
		String data = new String("data1, data2, data4; data4;");
		String[] delimitedData = StringUtil.parseDataByDelimiter(data);
		assertEquals(4, delimitedData.length);
	}

	@Test
	public void removeFirstFiveNoneAlphabeticCharactersFromString(){
		String data = new String("  -;data");
		assertEquals("data",StringUtil.removeNoneAlphabeticCharcters(data));

		data = new String("=@data");
		assertEquals("data",StringUtil.removeNoneAlphabeticCharcters(data));

		data = new String("data");
		assertEquals("data",StringUtil.removeNoneAlphabeticCharcters(data));
	}

	@Test
	public void removeFirstUselessWordsFromString(){
		List<String> uselessWords = new ArrayList<>(Arrays.asList("Useless1","word2","word3","noneremoved"));
		String data = "word2 word3 Useless1";

		String expectedResult = "word2 word3 ";
		assertEquals(expectedResult, StringUtil.removeFirstUselessWord(uselessWords, data));
	}
}
