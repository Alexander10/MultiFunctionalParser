package sk.ban.data;

import sk.ban.enums.PropertyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by USER on 9. 2. 2015.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyInfo {



	String label() default "";

	PropertyType type() default PropertyType.STRING;

}
