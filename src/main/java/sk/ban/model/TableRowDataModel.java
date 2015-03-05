package sk.ban.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.ban.data.Document;
import sk.ban.data.TableRow;

/**
 * Created by BAN on 18. 1. 2015.
 */
public class TableRowDataModel {

	private StringProperty fileName = new SimpleStringProperty("");

	private DocumentDataModel docxFile;
	private DocumentDataModel pdfFile;
	private DocumentDataModel pdfResult;
	private DocumentDataModel docxResult;
	private SimpleBooleanProperty ok = new SimpleBooleanProperty(false);
	private DocumentDataModel finalResult;


	public TableRowDataModel(String fileName) {
		this.fileName.setValue(fileName);

	}
	public TableRowDataModel(TableRow row) {
		this(row.getFinalDocument(), row.getPdfDocument(), row.getDocxDocument());
		ok.setValue(row.isAlreadyChecked());
	}

	public TableRowDataModel(Document finalDTO, Document pdfData, Document docxData) {
		fileName.setValue(finalDTO.getContentData() != null ? finalDTO.getContentData().getTitle() : "");
		docxFile = new DocumentDataModel(docxData);
		pdfFile =  new DocumentDataModel(pdfData);
		finalResult = new DocumentDataModel(finalDTO);
		pdfResult = pdfFile;
		docxResult = docxFile;
	}


	public TableRow convert() {
		Document docx = docxFile != null && docxFile.getReferenceDTO() != null ? docxFile.getReferenceDTO() : null;
		Document pdf = pdfResult != null && pdfResult.getReferenceDTO() != null ? pdfResult.getReferenceDTO() : null;
		return new TableRow(fileName.getValue(), docx, pdf, finalResult.getDocument(), ok.getValue());
	}

	public String getFileName() {
		return fileName.get();
	}

	public StringProperty fileNameProperty() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName.set(fileName);
	}

	public DocumentDataModel getDocxFile() {
		return docxFile;
	}

	public void setDocxFile(DocumentDataModel docxFile) {
		this.docxFile = docxFile;
	}

	public DocumentDataModel getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(DocumentDataModel pdfFile) {
		this.pdfFile = pdfFile;
	}

	public DocumentDataModel getPdfResult() {
		return pdfResult;
	}

	public void setPdfResult(DocumentDataModel pdfResult) {
		this.pdfResult = pdfResult;
	}

	public DocumentDataModel getDocxResult() {
		return docxResult;
	}

	public void setDocxResult(DocumentDataModel docxResult) {
		this.docxResult = docxResult;
	}

	public boolean getOk() {
		return ok.get();
	}

	public SimpleBooleanProperty okProperty() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok.set(ok);
	}

	public DocumentDataModel getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(DocumentDataModel finalResult) {
		this.finalResult = finalResult;
	}
}
