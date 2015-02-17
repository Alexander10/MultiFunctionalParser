package sk.ban;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import sk.ban.exception.RuntimeExceptionHandler;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

public class WeldFXMLLoader extends Application {

	@Inject
	Instance<Object> instance;


	@Override
	public void start(Stage primaryStage) throws Exception {

		Thread.setDefaultUncaughtExceptionHandler(new RuntimeExceptionHandler());
		Weld weld = new Weld();
		WeldContainer weldContainer = weld.initialize();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(weld));
		weldContainer.event().select(Stage.class).fire(primaryStage);

	}

	public <T> T load(URL url) {
		FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory((param) -> instance.select(param).get());
		loader.setLocation(url);

		try {
			return (T) loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static class ShutdownHook extends Thread {

		private final Weld weld;

		ShutdownHook(Weld weld) {
			this.weld = weld;
		}

		public void run() {
			weld.shutdown();
		}

	}


	public static void main(String[] args) {
		Application.launch(WeldFXMLLoader.class, args);

	}
}
