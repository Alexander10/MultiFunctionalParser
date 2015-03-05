package sk.ban.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

	@Test
	public void testSplitDataByCommas() throws Exception {
		String data = "data1, data2,data3, data4";
		assertEquals(4, StringUtil.splitData(data).length);
	}

	@Test
	public void testSplitDataBySemicolons() {
		String data = "data1; data2,data3; data4,";
		assertEquals(4, StringUtil.splitData(data).length);
	}

	@Test
	public void testEmptyStringSplitData() {
		String data = "";
		assertEquals(0, StringUtil.splitData(data).length);
	}

	@Test
	public void testRemoveNoneAlphabeticCharcters() throws Exception {

		String data = "! #$%data";
		String expectedResult = "data";
		assertEquals(expectedResult, StringUtil.removeStartNoneAlphanumericString(data));
		assertEquals(expectedResult, StringUtil.removeStartNoneAlphanumericString(data));
	}

	@Test
	public void testRemoveNoneDigitCharacters() throws Exception {
		String input = "data932data32323data data434 $%%%  #@#@#";
		String expectedResult = "93232323434";

		assertEquals(expectedResult, StringUtil.removeNoneDigitCharacters(input));
		assertEquals(expectedResult, StringUtil.removeNoneDigitCharacters(expectedResult));
	}

	@Test
	public void testRemoveFirstUselessWord() throws Exception {
		List<String> uselessWords = new ArrayList<>(Arrays.asList("Useless1", "word2", "word3", "noneremoved"));
		String data = "word2 word3 Useless1";

		String expectedResult = "word2 word3 ";
		assertEquals(expectedResult, StringUtil.removeFirstUselessWord(uselessWords, data));
	}

	@Test
	public void testContainsBoundedDigit() throws Exception {
		String input = "- 3234 -";
		assertTrue(StringUtil.containsBoundedDigit(input));

		String input2 = "input 2 - input 2 ";
		assertFalse(StringUtil.containsBoundedDigit(input2));
	}
}