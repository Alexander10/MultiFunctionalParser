package sk.ban.service;


import sk.ban.data.Document;
import sk.ban.parser.docx.DOCXParser;
import sk.ban.exception.ParserException;
import org.junit.Before;
import org.junit.Test;
import sk.ban.util.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by USER on 26. 1. 2015.
 */
public class DOCXParserTest {

	private static final String WORD_DOCUMENT = "testData/nase1.docx";
	private static final String DOCX_TITLE = "Optimization of Faculty of transport and traffic sciences web based upon student's habits";

	private URL url;

	@Before
	public void initialize() {
		url = Thread.currentThread().getContextClassLoader().getResource(WORD_DOCUMENT);
	}

	@Test
	public void openExistedDocxFile() {

		File file = new File(url.getPath());
		Optional<InputStream> opt = FileUtil.openDocx(file);
		assertTrue(opt.isPresent());

	}

	@Test(expected = ParserException.class)
	public void openNoneExistedFile() {
		//this should be not found proper file
		File file = new File(WORD_DOCUMENT);
		DOCXParser parser = new DOCXParser();
		Document dto = parser.parse(file);

	}

	@Test
	public void parseData(){
		File file = new File(url.getPath());
		DOCXParser parser = new DOCXParser();
		Document dto = parser.parse(file);

		assertEquals(DOCX_TITLE, dto.getContentData().getTitle());
		assertEquals(12, dto.getContentData().getReferences().size());


	}



}
