package sk.ban.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.ban.component.ButtonCell;
import sk.ban.data.Document;
import sk.ban.data.SerializationObject;
import sk.ban.data.TableRow;
import sk.ban.enums.ButtonAction;
import sk.ban.enums.MenuEventType;
import sk.ban.model.DocumentDataModel;
import sk.ban.model.TableRowDataModel;
import sk.ban.service.ParserService;
import sk.ban.util.FileUtil;
import sk.ban.util.MenuEvent;
import sk.ban.util.MenuEventDefinition;
import sk.ban.worker.BibExporter;
import sk.ban.worker.DataCombiner;
import sk.ban.worker.DataSerializer;
import sk.ban.worker.DataValidator;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by BAN on 20. 1. 2015.
 */
@Singleton
public class ParserController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(ParserController.class);

	@FXML
	private TableView tableView;

	@FXML
	private TextField tfPath;

	@FXML
	private TextField tfYear;

	@FXML
	private TextField tfConferenceName;

	@FXML
	private DatePicker tfRegistrationDate;

	@FXML
	private DatePicker tfPublishedDate;

	@FXML
	private Button btnStart;

	@FXML
	private TextArea taOutput;

	@Inject
	private ParserService service;

	private ObservableList<TableRowDataModel> observableList = FXCollections.observableArrayList();

	private static final String STOP_PARSING = "Stop parsing";

	private static final String START_PARSING = "Start parsing";


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		TableColumn<TableRowDataModel, String> fileName = new TableColumn<>("Title");
		fileName.setMinWidth(350);
		fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));

		TableColumn<TableRowDataModel, String> openPDF = createColumnWithButton("Open PDF", "pdfFile", "Open", ButtonAction.OPEN_FILE);

		TableColumn<TableRowDataModel, String> openDOCX = createColumnWithButton("Open DOCX", "docxFile", "Open", ButtonAction.OPEN_FILE);

		TableColumn<TableRowDataModel, String> pdfResult = createColumnWithButton("PDF Result", "pdfResult", "Show", ButtonAction.SHOW_STRING);

		TableColumn<TableRowDataModel, String> docxResult = createColumnWithButton("DOCX Result", "docxResult", "Show", ButtonAction.SHOW_STRING);

		TableColumn<TableRowDataModel, String> finalResult = createColumnWithButton("FINAL Result", "finalResult", "Edit", ButtonAction.FINAL_RESULT);

		TableColumn<TableRowDataModel, Boolean> checked = new TableColumn<>("OK");
		checked.setCellValueFactory(new PropertyValueFactory<>("ok"));
		CheckBoxTableCell checkCell = new CheckBoxTableCell<>();
		checkCell.setVisible(true);

		checked.setCellFactory(param -> new CheckBoxTableCell<>());

		tableView.getColumns().addAll(fileName, openPDF, openDOCX, pdfResult, docxResult, finalResult, checked);
		tableView.setEditable(true);
	}

	@FXML
	public void parseData() {

		File file = new File(tfPath.getText());
		if (!file.exists()) {
			log.info("File for parsing was not found");
			return;
		}

		new Thread(() -> {

			observableList.clear();
			if (STOP_PARSING.equals(btnStart.getText())) {
				service.setRunning(false);
				log.info("Parser was stopped");
			} else {
				Platform.runLater(() -> btnStart.setText(STOP_PARSING));
				service.parseData(file);

				Map<String, List<Document>> data = service.getParsedData();

				for (Map.Entry<String, List<Document>> entry : data.entrySet()) {

					TableRowDataModel tableModel = new TableRowDataModel(entry.getKey());
					List<Document> dtos = entry.getValue();

					Document pdfFile = dtos.get(0);

					tableModel.setPdfFile(pdfFile.getContentData() != null ? new DocumentDataModel(pdfFile) : null);
					tableModel.setPdfResult(pdfFile.getContentData() != null ? new DocumentDataModel(pdfFile) : null);

					Document docxFile = dtos.get(1);
					tableModel.setDocxFile(docxFile.getContentData() != null ? new DocumentDataModel(docxFile) : null);
					tableModel.setDocxResult(docxFile.getContentData() != null ? new DocumentDataModel(docxFile) : null);

					tableModel.setFinalResult(new DocumentDataModel(DataCombiner.combineData(pdfFile, docxFile)));
					observableList.add(tableModel);

				}
			}
			Platform.runLater(() -> tableView.setItems(observableList));
			Platform.runLater(() -> btnStart.setText(START_PARSING));


		}).start();
	}

	@FXML
	public void validateData() {
		List<Document> resultList = service.getParsedDataAsList();

		for (Document dto : resultList) {
			Platform.runLater(() -> taOutput.appendText(DataValidator.validate(dto.getContentData(), dto.getContentData().getFileName())));
			Platform.runLater(() -> taOutput.appendText(DataValidator.validate(dto.getAdditionalData(), dto.getContentData().getFileName() + " " + dto.getContentData().getTitle())));
		}

		log.info("Validation was successfully ended");
	}

	@FXML
	public void saveData() {

		if (!DataValidator.isValid(tfConferenceName.getText(), tfPath.getText(), tfYear.getText()) && FileUtil.existsFile(tfPath.getText())) {
			return;
		}

		List<Document> resultList = service.getParsedDataAsList();
		new BibExporter(tfConferenceName.getText(), tfYear.getText(), tfPublishedDate.getValue().toString(), tfRegistrationDate.getValue().toString())
				.exporAllDataToFile(FileUtil.getWorkingDir(tfPath.getText()), resultList);
	}

	public void setPath(@Observes @MenuEventDefinition(MenuEventType.OPEN_DIR) MenuEvent event) {
		tfPath.setText(event.getFile().toString());
		log.info("Path to file or directory: " + event.getFile().toString());
	}

	public void exportData(@Observes @MenuEventDefinition(MenuEventType.EXPORT_CONFIGURATION) MenuEvent exportDataEvent) {

		List<TableRow> rows = new ArrayList<>();
		observableList.forEach(row -> rows.add(row.convert()));
		SerializationObject obj = new SerializationObject(rows, tfPath.getText(), tfConferenceName.getText(), tfYear.getText(), tfRegistrationDate.getValue(), tfPublishedDate.getValue());
		DataSerializer.serializeData(obj, exportDataEvent.getFile());

		log.info("Configuration was successfully saved to file: " + exportDataEvent.getFile().toString());
	}

	public void loadConfiguration(@Observes @MenuEventDefinition(MenuEventType.IMPORT_CONFIGURATION) MenuEvent importData) {

		observableList.clear();
		SerializationObject obj = DataSerializer.desereliazeData(importData.getFile());
		obj.getRows().forEach(row -> observableList.add(new TableRowDataModel(row)));
		tfConferenceName.setText(obj.getConferenceName());
		tfPath.setText(obj.getPath());
		tfYear.setText(obj.getConferenceYear());
		tfPublishedDate.setValue(obj.getPublishedDate());
		tfRegistrationDate.setValue(obj.getRegistrationDate());

		Platform.runLater(() -> tableView.setItems(observableList));

		log.info("Configuration was successfully loaded");

	}

	public <T, X> TableColumn<T, X> createColumnWithButton(String columnName, String bindPropertyName, String caption, ButtonAction action) {
		TableColumn<T, X> finalResult = new TableColumn<>(columnName);
		finalResult.setMinWidth(75);
		finalResult.setCellValueFactory(new PropertyValueFactory<>(bindPropertyName));
		finalResult.setCellFactory(param -> new ButtonCell(caption, action, taOutput));
		return finalResult;
	}


}
