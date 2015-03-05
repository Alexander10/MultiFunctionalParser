package sk.ban.worker;

import sk.ban.data.Document;
import sk.ban.data.DocumentContent;

/**
 * Created by BAN on 5. 2. 2015.
 */
public class DataCombiner {

	public static Document combineData(Document pdfData, Document docxData) {

		if (!docxData.hasContentData()) {
			return pdfData;
		}

		Document finalResult = new Document(pdfData.getAdditionalData(), docxData.getContentData());
		DocumentContent contentDTO = docxData.getContentData();

		if(!pdfData.hasContentData()){
			return finalResult;
		}

		if (contentDTO.getTitle().isEmpty()) {
			contentDTO.setTitle(pdfData.getContentData().getTitle());
		}

		if (contentDTO.getSubtitle().isEmpty()) {
			contentDTO.setSubtitle(pdfData.getContentData().getSubtitle());
		}

		if (contentDTO.getAuthors().isEmpty()) {
			contentDTO.setAuthors(pdfData.getContentData().getAuthors());
		}

		if (contentDTO.getAbstractText().isEmpty()) {
			contentDTO.setAbstractText(pdfData.getContentData().getAbstractText());
		}

		if (contentDTO.getKeywords().isEmpty()) {
			contentDTO.setKeywords(pdfData.getContentData().getKeywords());
		}

		if (contentDTO.getReferences().isEmpty()) {
			contentDTO.setReferences(pdfData.getContentData().getReferences());
		}

		return finalResult;
	}
}
