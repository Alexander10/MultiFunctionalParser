package sk.ban.parser.pdf;

import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by USER on 30. 1. 2015.
 */
public class MyPDFTextStripper extends PDFTextStripper {

	private static final Logger log = LoggerFactory.getLogger(MyPDFTextStripper.class);

	private PDFDocument documentContainer = new PDFDocument();

	public MyPDFTextStripper(PDFDocument document) throws IOException {
		super();
		this.documentContainer = document;
	}


	protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
		documentContainer.addLine(new PDFLine(text, textPositions));
	}


	public PDFDocument getPDFDocument() {
		return documentContainer;
	}


}
