package sk.ban.worker;

import org.slf4j.LoggerFactory;
import sk.ban.data.PropertyInfo;
import sk.ban.enums.PropertyType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by BAN on 9. 2. 2015.
 */
public class DataValidator {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(DataValidator.class);

	public static boolean isValid(String... values) {
		for (String value : values) {
			if (value == null || value.isEmpty()) {
				log.info("Data are not valid");
				return false;
			}
		}
		return true;
	}

	/**
	 * using reflection to validate object properties which are annotated with {#link PropertyInfo}
	 * Validated are types (list and string)
	 *
	 * @param o
	 * @return
	 */
	public static String validate(Object o, String fileName) {

		StringBuilder builder = new StringBuilder("File: " + fileName + "\n");

		if (o == null) {
			return builder.append(" no data \n").toString();
		}

		Field[] allFields = o.getClass().getDeclaredFields();
		boolean valid = true;
		for (Field field : allFields) {
			if (field.isAnnotationPresent(PropertyInfo.class)) {

				Object value = runGetter(field, o);
				PropertyType propertyType = field.getAnnotation(PropertyInfo.class).type();
				String text = field.getAnnotation(PropertyInfo.class).label();

				switch (propertyType) {
					case STRING:
						String strValue = (String) value;
						if (strValue.isEmpty()) {
							builder.append("\t").append(text).append(" : ").append("value is missing!!! \n");
							valid = false;
						}
						break;
					case LIST:
						List list = (List) value;
						if (list.isEmpty() || list.size() == 0) {
							builder.append("\t").append(text).append(" : ").append("value is missing!!! \n");
							valid = false;
						}
						break;
				}
			}
		}
		if (valid) {
			return "";
		}
		builder.append("\n");
		return builder.toString();
	}

	private static Object runGetter(Field field, Object o) {
		for (Method method : o.getClass().getMethods()) {
			if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
					// MZ: Method found, run it
					try {
						return method.invoke(o);
					} catch (IllegalAccessException e) {
						log.error("Could not determine method: " + method.getName());
					} catch (InvocationTargetException e) {
						log.error("Could not determine method: " + method.getName());
					}

				}
			}
		}
		return null;
	}

}
