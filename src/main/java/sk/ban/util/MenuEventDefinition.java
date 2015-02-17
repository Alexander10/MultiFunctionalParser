package sk.ban.util;

import sk.ban.enums.MenuEventType;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by USER on 16. 2. 2015.
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuEventDefinition {
	 MenuEventType value();
}
