package sk.ban.component;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sk.ban.controller.EditModeController;
import sk.ban.enums.ButtonAction;
import sk.ban.exception.ParserException;
import sk.ban.model.DocumentDataModel;
import sk.ban.util.FileUtil;
import sk.ban.worker.BibExporter;

import java.io.IOException;

/**
 * Created by USER on 16. 2. 2015.
 */
public class ButtonCell<T> extends TableCell<T, DocumentDataModel> {

	private Button cellButton;
	private SimpleObjectProperty content = new SimpleObjectProperty();

	public ButtonCell(String name, ButtonAction action, TextArea taOutput) {
		cellButton = new Button(name);
		getStyleClass().add("custom-cell");
		content.bindBidirectional(itemProperty());
		addButtonActionListener(action, taOutput);
	}

	/**
	 * Method handle button actions in table
	 * @param action
	 */
	private void addButtonActionListener(ButtonAction action, TextArea taOutput){
		cellButton.setOnAction(t -> {
			DocumentDataModel model = (DocumentDataModel) content.getValue();
			if (action == ButtonAction.OPEN_FILE) {
				FileUtil.openFile(model.getReferenceDTO().getContentData().getFileName());

			} else if (action == ButtonAction.SHOW_STRING) {
				taOutput.appendText(BibExporter.exportDataToString(model.getReferenceDTO()));
				taOutput.appendText("\n\n");
			} else {
				openEditMode((DocumentDataModel) content.getValue());
			}
		});
	}

	/**
	 * This solution is working only in this configuration
	 *
	 * @param item
	 * @param empty
	 * @see <a href="http://stackoverflow.com/questions/23426939/how-to-create-a-tableview-having-a-button-embedded-in-its-one-column">Stack Overflow</a>
	 */
	@Override
	protected void updateItem(DocumentDataModel item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty && item != null && !item.toString().isEmpty() ) {
			setGraphic(cellButton);
		} else {
			setGraphic(null);
		}
	}


	private void openEditMode(DocumentDataModel documentDataModel){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editmode.fxml"));
		EditModeController controller = new EditModeController();
		controller.setDataModel(documentDataModel);
		loader.setController(controller);
		loader.setRoot(controller);
		Parent root;
		try {
			root = loader.load();
			Scene scene = new Scene(root, 1300, 800);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Multifunctional Parser - Edit Mode");
			stage.show();
		} catch (IOException ex) {
			throw new ParserException("Problem with opening edit mode window: " + ex);
		}
	}
}
