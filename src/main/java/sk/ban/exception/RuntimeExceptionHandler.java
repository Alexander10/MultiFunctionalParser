package sk.ban.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by BAN on 24. 1. 2015.
 */
public class RuntimeExceptionHandler implements Thread.UncaughtExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.error("Unhandled RuntimeError", e);
	}
}
