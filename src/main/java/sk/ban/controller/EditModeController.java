package sk.ban.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sk.ban.model.DocumentDataModel;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by USER on 14. 2. 2015.
 */
public class EditModeController extends AnchorPane implements Initializable {

	@FXML
	private GridPane gridPane;

	@FXML
	private Button btnDiscardChanges;

	private DocumentDataModel documentDataModel;

	private DocumentDataModel workingCopy;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		workingCopy = new DocumentDataModel(documentDataModel);

		int basicInfoRowIdx = 1;
		gridPane.addRow(basicInfoRowIdx, new Label("Title"), createTextField(workingCopy.titleProperty()));
		gridPane.addRow(++basicInfoRowIdx, new Label("Subtitle"), createTextField(workingCopy.subTitleProperty()));
		gridPane.addRow(++basicInfoRowIdx, new Label("Abstract"), createTextArea(workingCopy.abstractTextProperty()));
		gridPane.addRow(++basicInfoRowIdx, new Label("Section"), createTextField(workingCopy.sectionProperty()));
		gridPane.addRow(++basicInfoRowIdx, new Label("Start page"), createTextField(workingCopy.startPageProperty()));
		gridPane.addRow(++basicInfoRowIdx, new Label("Last page"), createTextField(workingCopy.lastPageProperty()));

		ListView listViewAuthors = createListView(workingCopy.authorsProperty());
		listViewAuthors.setMaxHeight(150);
		gridPane.addRow(++basicInfoRowIdx, new Label("Authors"), listViewAuthors);

		ListView listViewKeywords = createListView(workingCopy.keywordsProperty());
		listViewKeywords.setMaxHeight(200);
		gridPane.addRow(++basicInfoRowIdx, new Label("Keywords"), listViewKeywords);

		ListView listViewReferences = createListView(workingCopy.referencesProperty());
		gridPane.add(listViewReferences, 2, 1);
		Optional<Node> node = getNodeFromGridPane(gridPane, 2, 1);
		if (node.isPresent()) {
			gridPane.setRowSpan(node.get(), basicInfoRowIdx + 1);
		}

	}

	@FXML
	public void addKeyword() {
		workingCopy.keywordsProperty().add(new SimpleStringProperty(""));
	}

	@FXML
	public void addAuthor() {
		workingCopy.authorsProperty().add(new SimpleStringProperty(""));
	}

	@FXML
	public void addReference() {
		workingCopy.referencesProperty().add(new SimpleStringProperty(""));
	}

	@FXML
	public void discardChanges() {
		workingCopy = new DocumentDataModel(documentDataModel.getReferenceDTO());
		Stage stage = (Stage) btnDiscardChanges.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void saveChanges() {
		documentDataModel.copyData(workingCopy);
	}

	private TextField createTextField(SimpleStringProperty value) {
		TextField textField = new TextField();
		textField.textProperty().bindBidirectional(value);
		textField.setMaxWidth(400);
		return textField;
	}

	private TextArea createTextArea(SimpleStringProperty property) {
		TextArea textArea = new TextArea();
		textArea.textProperty().bindBidirectional(property);
		textArea.setMaxWidth(400);
		textArea.setWrapText(true);
		return textArea;
	}

	private ListView createListView(SimpleListProperty<SimpleStringProperty> property) {
		ListView listView = new ListView();
		listView.itemsProperty().bindBidirectional(property);
		listView.setCellFactory((prop) -> new InstantEditingCell());
		listView.setEditable(true);
//		listView.getSelectionModel().selectedItemProperty()
//				.addListener( -> {
//				});
		return listView;
	}

	private Optional<Node> getNodeFromGridPane(GridPane gridPane, int col, int row) {
		return gridPane.getChildren().stream()
				.filter(node -> GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row)
				.findAny();

	}


	public void setDataModel(DocumentDataModel model) {
		documentDataModel = model;
	}

	class InstantEditingCell extends ListCell<SimpleStringProperty> {

		@Override
		public void updateItem(SimpleStringProperty item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setGraphic(null);
			} else {
				final TextField textField = new TextField(getString());
				textField.setMaxWidth(1000);
				textField.setEditable(true);
				Bindings.bindBidirectional(textField.textProperty(), item);
				setGraphic(textField);
			}
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}
}
