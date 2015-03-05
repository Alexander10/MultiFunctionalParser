package sk.ban.parser.pdf;

import org.apache.pdfbox.util.TextPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by BAN on 31. 1. 2015.
 */
class PDFLine {

	private final String line;
	private List<TextPosition> charPositions = new ArrayList<>();
	private static final double FOOTER_POSITION = 750;

	public PDFLine(String text, List<TextPosition> charPositions) {
		this.charPositions = charPositions;
		this.line = text;
	}

	public String getText() {
		return line;
	}

	public double avgFontSize() {
		return charPositions.stream()
				.mapToDouble(TextPosition::getFontSizeInPt)
				.average().getAsDouble();
	}

	public double getYPosition() {
		return charPositions.get(0).getY();
	}

	/**
	 * what to do when in one line is more than one font name
	 * @return
	 */
	public List<String> getFontNames(){
		return charPositions.stream()
				.map(ch -> ch.getFont().getFontDescriptor().getFontName())
				.distinct().collect(Collectors.toList());
	}

	/**
	 * TODO: this is wrooong idea (please consider better solution, maybe discuss with someone) ;)
	 *
	 * @param line
	 * @return
	 */
	public boolean hasTheSameFormat(Optional<PDFLine> line){
		return !line.isPresent() || avgFontSize() == line.get().avgFontSize();
	}

	public boolean isPageA4Footer(){
		return getYPosition() > FOOTER_POSITION;
	}

}
