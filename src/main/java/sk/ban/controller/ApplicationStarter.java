package sk.ban.controller;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.ban.util.ApplicationCloseEvent;
import sk.ban.WeldFXMLLoader;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by BAN on 17. 1. 2015.
 */
public class ApplicationStarter {


	private static final Logger log = LoggerFactory.getLogger(MainController.class);


	@Inject
	Event<ApplicationCloseEvent> applicationCloseEventEvent;

	@Inject
	WeldFXMLLoader fxmlLoader;

	public void launchJavaFxApplication(@Observes Stage stage) {

		DOMConfigurator.configure(WeldFXMLLoader.class.getResource("/log4j-parser.xml"));

		Parent root = fxmlLoader.load(getClass().getResource("/fxml/main.fxml"));
		Scene startScene = new Scene(root);
		startScene.getStylesheets().add("style.css");
		stage.setWidth(1300);
		stage.setHeight(800);
		stage.setTitle("Multifunctional Parser");
		stage.setScene(startScene);
		stage.setOnCloseRequest((ev) -> this.close());
		stage.show();

	}

	private void close() {
		applicationCloseEventEvent.fire(new ApplicationCloseEvent());
		log.info("Close application");
		Platform.exit();
		System.exit(0);
	}
}
