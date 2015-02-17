package sk.ban.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.ban.WeldFXMLLoader;
import sk.ban.enums.FileExtension;
import sk.ban.enums.MenuEventType;
import sk.ban.util.ApplicationCloseEvent;
import sk.ban.util.MenuEvent;
import sk.ban.util.MenuEventDefinition;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	private Stage stage;

	@Inject
	Event<ApplicationCloseEvent> applicationCloseEventEvent;

	@Inject
	@MenuEventDefinition(MenuEventType.OPEN_DIR)
	Event<MenuEvent> pathLocation;

	@Inject
	@MenuEventDefinition(MenuEventType.EXPORT_CONFIGURATION)
	Event<MenuEvent> exportDataEvent;

	@Inject
	@MenuEventDefinition(MenuEventType.IMPORT_CONFIGURATION)
	Event<MenuEvent> importDataEvent;

	@Inject
	WeldFXMLLoader fxmlLoader;


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void exportConfiguration() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as");
		File selectedFile = fileChooser.showSaveDialog(stage);
		exportDataEvent.fire(new MenuEvent(selectedFile));
	}

	@FXML
	public void importConfiguration() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as");
		File selectedFile = fileChooser.showOpenDialog(stage);
		importDataEvent.fire(new MenuEvent(selectedFile));
	}

	@FXML
	public void openDirectory() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");
		File defaultDirectory = new File("c:");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(stage);

		if (selectedDirectory == null) {
			log.info("Directory for parsing was not selected");
			return;
		}

		pathLocation.fire(new MenuEvent(selectedDirectory));
	}

	@FXML
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PDF Files", "*." + FileExtension.PDF),
				new FileChooser.ExtensionFilter("DOCX Files", "*." + FileExtension.DOCX));


		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile == null) {
			log.info("File for parsing was not selected");
			return;
		}
		pathLocation.fire(new MenuEvent(selectedFile));
	}

	@FXML
	public void close() {

		applicationCloseEventEvent.fire(new ApplicationCloseEvent());
		log.info("Close application");
		Platform.exit();
		System.exit(0);

	}

	public void setStage(@Observes Stage stage) {
		this.stage = stage;
	}

}
