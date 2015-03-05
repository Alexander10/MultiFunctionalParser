package sk.ban.data;

import java.io.Serializable;

/**
 * Created by BAN on 16. 2. 2015.
 */
public class TableRow implements Serializable {

	private String title = "";
	private Document docxDocument = new Document();
	private Document pdfDocument = new Document();
	private Document finalDocument = new Document();
	private boolean alreadyChecked = false;

	public TableRow(String title, Document docx, Document pdf, Document finalResult, boolean alreadyChecked){
		this.title = title;
		this.docxDocument = docx;
		this.pdfDocument = pdf;
		this.finalDocument = finalResult;
		this.alreadyChecked = alreadyChecked;
	}

	public String getTitle() {
		return title;
	}

	public Document getDocxDocument() {
		return docxDocument;
	}

	public Document getPdfDocument() {
		return pdfDocument;
	}

	public Document getFinalDocument() {
		return finalDocument;
	}

	public boolean isAlreadyChecked() {
		return alreadyChecked;
	}
}
