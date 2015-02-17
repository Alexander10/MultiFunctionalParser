package sk.ban.service;

import sk.ban.exception.ParserException;
import org.junit.Before;
import org.junit.Test;
import sk.ban.parser.pdf.MyPDFParser;

import java.io.File;
import java.net.URL;

/**
 * Created by USER on 30. 1. 2015.
 */
public class PDFParserTest {

	private static final String PDF_DOCUMENT = "testData/dialogo-1-1-25.pdf";
	private static  URL url;

	@Before
	public void init(){
		url = Thread.currentThread().getContextClassLoader().getResource(PDF_DOCUMENT);
	}


	@Test(expected = ParserException.class)
	public void noneValidFile(){

		MyPDFParser parser = new MyPDFParser();
		parser.parse(new File(url.getPath() + "s"));
	}


	@Test()
	public void parseSuccessfulData(){
		MyPDFParser parser = new MyPDFParser();
		parser.parse(new File(url.getPath()));
	}
}
