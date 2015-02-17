package sk.ban.parser.pdf;

import sk.ban.exception.ParserException;
import sk.ban.data.Document;
import sk.ban.data.DocumentContent;
import sk.ban.data.DocumentNavigation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.ban.parser.Parserable;
import sk.ban.util.Constants;
import sk.ban.util.StringUtil;
import sk.ban.worker.DataCleaner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by USER on 30. 1. 2015.
 */
public class MyPDFParser implements Parserable {

	private static final Logger log = LoggerFactory.getLogger(MyPDFParser.class);

	private static final double SUBTITLE_FONT_MIN_SIZE = 13.5;
	private static final double TITLE_FONT_MIN_SIZE = 16;
	private static final int INFINITY_LINE = Integer.MAX_VALUE;


	public Document parse(File file) {

		PDFDocument doc = parsePDFToLines(file);
		List<PDFLine> lines = doc.getLines();

		DocumentContent contentDTO = new DocumentContent();
		contentDTO.setFileName(file.toString());
		//entry point to alg. is find first the index of abstract
		int abstractIndex = findTextIndex(lines, (line) -> isAbstract(line));
		abstractIndex = abstractIndex != 0 ? abstractIndex : 10;        //for case when abstract will be not found

		//parse title
		ReturnValue title = getRawText(lines, 0, abstractIndex, (line, prev) -> line.avgFontSize() > TITLE_FONT_MIN_SIZE && line.hasTheSameFormat(prev));
		contentDTO.setTitle(title.getText());

		//parse subtitle => find after title
		ReturnValue subTitle = getRawText(lines, title.getLastIndex() + 1, abstractIndex,
				(line, prev) -> line.avgFontSize() > SUBTITLE_FONT_MIN_SIZE && line.hasTheSameFormat(prev));
		contentDTO.setSubtitle(subTitle.getText());

		//parse authors => +1 find after subtitle index
		ReturnValue authors = getRawText(lines, subTitle.getLastIndex()==0? title.getLastIndex() + 1 : subTitle.getLastIndex()+1,
				abstractIndex, (line, prev) -> line.hasTheSameFormat(prev) && line.avgFontSize() < TITLE_FONT_MIN_SIZE);
		contentDTO.setAuthors(Arrays.asList(StringUtil.parseDataByDelimiter(authors.getText())));

		//parse abstract => 50 is predicate max length of Abstract text
		ReturnValue abstractVal = getRawText(lines, abstractIndex, abstractIndex + 50,
				(line, prev) -> line.hasTheSameFormat(prev) && !line.getText().contains("Keywords"));
		contentDTO.setAbstractText(StringUtil.removeNoneAlphabeticCharcters(
				StringUtil.removeFirstUselessWord(Constants.ABSTRACT, abstractVal.getText())));

		//parse keywords => +1 means next line after Abstract should be keywords and max predicated length is 4 lines
		ReturnValue keywords = getRawText(lines, abstractVal.getLastIndex() + 1, abstractVal.getLastIndex() + 4,
				(line, prev) -> line.hasTheSameFormat(prev) && line.getText().contains("Keywords"));
		contentDTO.setKeywords(Arrays.asList(StringUtil.parseDataByDelimiter(
				StringUtil.removeNoneAlphabeticCharcters(
						StringUtil.removeFirstUselessWord(Constants.KEYWORDS, keywords.getText())))));

		//first find REFERENCES text and then go until the SECTION text (probably this is not good approach ???)
		int referencesIndex = findTextIndex(lines, (line) -> line.getText().trim().equalsIgnoreCase("REFERENCES"));
		ReturnValue references = getRawText(lines, referencesIndex + 1, INFINITY_LINE, (line, prev) -> !line.getText().equalsIgnoreCase("SECTION"));
		String[] arrOfReferences = references.getText().split("\\[\\d+\\]\\s+");
		contentDTO.setReferences(Arrays.stream(arrOfReferences).filter(ref -> !ref.trim().isEmpty()).collect(Collectors.toList()));

		DataCleaner.cleanData(contentDTO);

		DocumentNavigation dtoPageNavigable = parseDocumentNaviagationInfo(lines);

		log.info("PDF file was successfully parsed: " + file.toString());
		return new Document(dtoPageNavigable, contentDTO);
	}


	/**
	 * @param lines
	 * @return
	 */
	private DocumentNavigation parseDocumentNaviagationInfo(List<PDFLine> lines) {

		DocumentNavigation dto = new DocumentNavigation();
		int index = 0;

		for (PDFLine line : lines) {
			if (line.isPageA4Footer()) {
				//find line with SECTION string
				if (isSectionTitleText(dto, line.getText())) {
					//after SECTION find two next lines, on these lines should be placed section title
					dto.setSection(getRawText(lines, ++index, index + 2, (l, prev) -> l.hasTheSameFormat(prev)).getText());
				} else if (StringUtil.containsBoundedDigit(line.getText())) {

					if (dto.getStartPage().isEmpty()) {
						dto.setStartPage(StringUtil.removeNoneDigitCharacters(line.getText()));
					}

					dto.setLastPage(StringUtil.removeNoneDigitCharacters(line.getText()));
				}
			}
			index++;
		}

		return dto;
	}


	private boolean isSectionTitleText(DocumentNavigation dto, String text) {
		return dto.getSection().isEmpty() && text.trim().equalsIgnoreCase("SECTION");
	}

	private ReturnValue getRawText(List<PDFLine> lines, int start, int end, BiPredicate<PDFLine, Optional<PDFLine>> predicate) {

		int index = 0;
		StringBuilder builder = new StringBuilder();
		Optional<PDFLine> prevLine = Optional.empty();

		for (int idx = start; idx < lines.size() && idx < end; idx++) {
			PDFLine line = lines.get(idx);
			if (predicate.test(line, prevLine)) {
				builder.append(line.getText());
				index = idx;
			} else if (builder.length() != 0) {
				break;
			}
			prevLine = Optional.of(line);
		}

		return new ReturnValue(builder.toString().trim(), index);
	}

	/**
	 * Parse full document into list of lines
	 *
	 * @param file
	 * @return
	 */
	private PDFDocument parsePDFToLines(File file) {
		PDFDocument document;
		try {
			MyPDFTextStripper stripper = new MyPDFTextStripper(new PDFDocument());
			PDDocument doc = PDDocument.load(file);
			stripper.setStartPage(0);
			stripper.setEndPage(10000);

			stripper.getText(doc);
			document = stripper.getPDFDocument();
			List<PDFLine> lines = document.getLines();
			//lines.stream().map((line) -> line.getText() + " " + line.getYPosition()).forEach(System.out::println);
			doc.close();
		} catch (IOException e) {
			throw new ParserException("Problem with parsing pdf file: " + e);
		}
		return document;

	}


	/**
	 * @param lines
	 * @return
	 */
	private int findTextIndex(List<PDFLine> lines, Predicate<PDFLine> predicate) {
		for (int idx = 0; idx < lines.size(); idx++) {
			if (predicate.test(lines.get(idx))) {
				return idx;
			}
		}
		return 0;
	}


	/**
	 * find out if line contains at first position Abstract
	 *
	 * @param line
	 * @return
	 */
	private boolean isAbstract(PDFLine line) {
		String text = line.getText().trim();
		if (text.length() < Constants.ABSTRACT.length()) {
			return false;
		}

		String abstractText = text.substring(0, Constants.ABSTRACT.length());
		if (abstractText.equalsIgnoreCase(Constants.ABSTRACT.toString())) {
			return true;
		}
		return false;
	}


	private class ReturnValue {
		private String text;
		private int lastIndex;

		public ReturnValue(String text, int lastIndex) {
			this.text = text;
			this.lastIndex = lastIndex;
		}

		public String getText() {
			return text;
		}

		public int getLastIndex() {
			return lastIndex;
		}
	}


}
