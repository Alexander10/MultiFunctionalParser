package sk.ban.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import sk.ban.data.Document;
import sk.ban.data.DocumentContent;
import sk.ban.data.DocumentNavigation;
import sk.ban.worker.BibExporter;

import java.util.Collections;
import java.util.List;

/**
 * Created by USER on 14. 2. 2015.
 */
public class DocumentDataModel {

	private SimpleStringProperty title = new SimpleStringProperty("");
	private SimpleStringProperty subTitle = new SimpleStringProperty("");
	private SimpleStringProperty abstractText = new SimpleStringProperty("");
	private SimpleStringProperty section = new SimpleStringProperty("");
	private SimpleStringProperty startPage = new SimpleStringProperty("");
	private SimpleStringProperty lastPage = new SimpleStringProperty("");


	private SimpleListProperty authors = new SimpleListProperty(FXCollections.observableArrayList());

	private SimpleListProperty keywords = new SimpleListProperty(FXCollections.observableArrayList());
	private SimpleListProperty references = new SimpleListProperty(FXCollections.observableArrayList());
	private Document referenceDTO;


	public DocumentDataModel(Document dto) {
		if(dto != null) {
			this.referenceDTO = dto;
			title.setValue(dto.getContentData() != null ? dto.getContentData().getTitle() : "");
			subTitle.setValue(dto.getContentData() != null ? dto.getContentData().getSubtitle() : "");
			abstractText.setValue(dto.getContentData() != null ? dto.getContentData().getAbstractText() : "");
			section.setValue(dto.getAdditionalData() != null ? dto.getAdditionalData().getSection() : "");
			startPage.setValue(dto.getAdditionalData() != null ? dto.getAdditionalData().getStartPage() : "");
			lastPage.setValue(dto.getAdditionalData() != null ? dto.getAdditionalData().getLastPage() : "");

			fillObservableList(authors, dto.getContentData().getAuthors());
			fillObservableList(keywords, dto.getContentData().getKeywords());
			fillObservableList(references, dto.getContentData().getReferences());
		}

	}

	public DocumentDataModel(DocumentDataModel model) {
		this.referenceDTO = model.getReferenceDTO();

		copyData(model);
	}

	private void fillObservableList(SimpleListProperty<SimpleStringProperty> observable, List<String> data) {
		data.stream()
				.forEach(item -> observable.add(new SimpleStringProperty(item)));
	}

	public Document getFilledDocumentDTO() {
		DocumentContent contentDTO = new DocumentContent();
		contentDTO.setTitle(title.getValue());
		contentDTO.setSubtitle(subTitle.getValue());
		contentDTO.setAbstractText(abstractText.getValue());
		fillListData(contentDTO.getAuthors(), authors);
		fillListData(contentDTO.getKeywords(), keywords);
		fillListData(contentDTO.getReferences(), references);

		DocumentNavigation navigationDTO = new DocumentNavigation();
		navigationDTO.setSection(section.getValue());
		navigationDTO.setStartPage(startPage.getValue());
		navigationDTO.setLastPage(lastPage.getValue());

		return new Document(navigationDTO, contentDTO);
	}

	public void copyData(DocumentDataModel model) {

		this.title = new SimpleStringProperty(model.getTitle());
		this.subTitle = new SimpleStringProperty(model.getSubTitle());
		this.abstractText = new SimpleStringProperty(model.getAbstractText());
		this.section = new SimpleStringProperty(model.getSection());
		this.startPage = new SimpleStringProperty(model.getStartPage());
		this.lastPage = new SimpleStringProperty(model.getLastPage());

		authors = copyList(model.authorsProperty());
		keywords = copyList(model.keywordsProperty());
		references = copyList(model.referencesProperty());
	}

	/**
	 * copying of SimpleList is not possible directly by Collections.copy due to problem with list size
	 * first is needed to create list with properly size
	 *
	 * @param src
	 */
	@SuppressWarnings("unchecked")
	private SimpleListProperty copyList(SimpleListProperty src) {
		SimpleListProperty dest = new SimpleListProperty(FXCollections.observableArrayList());
		if (!src.isEmpty() && src.size() > 0) {
			dest = new SimpleListProperty(FXCollections.observableArrayList(src));
			Collections.copy(dest, src);

		}
		return dest;
	}

	private void fillListData(List<String> data, SimpleListProperty<SimpleStringProperty> properties) {
		properties.forEach(item -> data.add(item.getValue()));
	}

	public String getTitle() {
		return title.get();
	}

	public SimpleStringProperty titleProperty() {
		return title;
	}

	public String getSubTitle() {
		return subTitle.get();
	}

	public SimpleStringProperty subTitleProperty() {
		return subTitle;
	}

	public String getAbstractText() {
		return abstractText.get();
	}

	public SimpleStringProperty abstractTextProperty() {
		return abstractText;
	}

	public String getSection() {
		return section.get();
	}

	public SimpleStringProperty sectionProperty() {
		return section;
	}

	public String getStartPage() {
		return startPage.get();
	}

	public SimpleStringProperty startPageProperty() {
		return startPage;
	}

	public String getLastPage() {
		return lastPage.get();
	}

	public SimpleStringProperty lastPageProperty() {
		return lastPage;
	}

	public Object getAuthors() {
		return authors.get();
	}

	public SimpleListProperty<SimpleStringProperty> authorsProperty() {
		return authors;
	}

	public Object getKeywords() {
		return keywords.get();
	}

	public SimpleListProperty<SimpleStringProperty> keywordsProperty() {
		return keywords;
	}

	public Object getReferences() {
		return references.get();
	}

	public SimpleListProperty<SimpleStringProperty> referencesProperty() {
		return references;
	}

	public Document getReferenceDTO() {
		return referenceDTO;
	}

	@Override
	public String toString() {
		return BibExporter.exportDataToString(referenceDTO);
	}


}
