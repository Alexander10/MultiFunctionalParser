package sk.ban.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.slf4j.LoggerFactory;
import sk.ban.util.ApplicationCloseEvent;

import javax.enterprise.event.Observes;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by USER on 20. 1. 2015.
 */
public class ConsoleController implements Initializable {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConsoleController.class);

	private static final int MAX_CHARACTERS_TEXT_AREA = 10000;

	@FXML
	private TextArea console;

	private ConsoleAppenderWriter writer = new ConsoleAppenderWriter();

	/**
	 * Caution, {@link WriterAppender} is Log4j specific.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		writer.scheduleRecurringFlushAction();

		WriterAppender wa = new WriterAppender(new PatternLayout("%d{ISO8601} \t %-5p \t %m%n"), writer);
		wa.setThreshold(Level.INFO);
		Logger.getRootLogger().addAppender(wa);
	}


	public void closeDownTimer(@Observes ApplicationCloseEvent ace) {
		log.debug("Shutting down Writer {}", writer);
		writer.shutdownTimer();
		log.debug("Writer {} shutdown complete", writer);
	}

	/**
	 * Handle writing of LOG statements into {@link #console}.
	 * <p>
	 * Instead of directly appending the logs to UI, they are first stored in {@link StringBuffer}.
	 * UI component {@link #console} is updated regularly with new logs.
	 * Rate of updates is {@link #FLUSH_INTERVAL_MILLIS}).
	 * <p>
	 * Maximum number of characters of {@link #console} is limited  to {@link #MAX_CHARACTERS_TEXT_AREA}.
	 * When maximum is exceeed, text size is reduced during {@link #flushAction}.
	 */
	private final class ConsoleAppenderWriter extends Writer {

		private static final int FLUSH_INTERVAL_MILLIS = 200;

		/**
		 * Logger is probably not threadsafe, so using at least some kind of synchronisation
		 */
		private StringBuffer buffer = new StringBuffer();

		private Timer timer = new Timer();

		/**
		 * Reduce number of GC by having only 1 instance of {@link Runnable} during {@link #flushTimerTask}
		 */
		private Runnable flushAction = () -> {

			String message = buffer.toString();
			buffer.setLength(0);
			buffer.trimToSize();

			if (message.length() > MAX_CHARACTERS_TEXT_AREA) {
				// replace the whole console text, since is already too long
				console.setText("");

			} else if (console.getLength() + message.length() > MAX_CHARACTERS_TEXT_AREA) {
				// remove half of the text to make some space for new text
				console.deleteText(0, console.getLength() / 2);
			}

			// append moves scrollbar of text area to bottom
			console.appendText(message);

		};

		/**
		 * We need to store {@link TimerTask} instance, so we can gracefully shutdown the {@link #timer} oject.
		 */
		private TimerTask flushTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (buffer.length() > 0) {
					Platform.runLater(flushAction);
				}
			}
		};

		/**
		 * After starting the {@link TimerTask}, timer needs to be closed via {@link #shutdownTimer()}
		 */
		public void scheduleRecurringFlushAction() {
			timer.schedule(flushTimerTask, 0, FLUSH_INTERVAL_MILLIS);
		}

		public void shutdownTimer() {
			flushTimerTask.cancel();
			timer.cancel();
			timer.purge();
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			buffer.append(cbuf, off, len);
		}

		@Override
		public void flush() throws IOException {
			// doing nothing, since flush to "console" is called from timer
		}

		@Override
		public void close() throws IOException {
		}

		@Override
		public String toString() {
			return "ConsoleAppenderWriter{" +
					"timer=" + timer +
					", flushTimerTask=" + flushTimerTask +
					"}";
		}

	}

}
