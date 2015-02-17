package sk.ban.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by USER on 26. 1. 2015.
 */
public class ParserException extends RuntimeException {

	private static final Logger log = LoggerFactory.getLogger(ParserException.class);

	public ParserException(String message){
		super(message);
		log.error(message);
	}

	public ParserException(Exception e){
		super(e);
		log.error(e.toString());
	}
}
