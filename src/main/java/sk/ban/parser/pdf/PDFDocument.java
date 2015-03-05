package sk.ban.parser.pdf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BAN on 31. 1. 2015.
 */
class PDFDocument {

	private List<PDFLine> lines = new ArrayList<>();
	private int pages;

	public PDFDocument(){

	}

	public void addLine(PDFLine line){
		lines.add(line);
	}

	public List<PDFLine> getLines(){
		return lines;
	}
}
