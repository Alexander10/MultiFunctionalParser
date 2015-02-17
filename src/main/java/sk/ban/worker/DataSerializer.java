package sk.ban.worker;

import sk.ban.data.SerializationObject;
import sk.ban.data.TableRow;
import sk.ban.exception.ParserException;

import java.io.*;
import java.util.List;

/**
 * Created by BAN on 15. 2. 2015.
 */
public class DataSerializer {

	public static void serializeData(SerializationObject obj, File file) {

		try (
				OutputStream outputStream = new FileOutputStream(file);
				OutputStream buffer = new BufferedOutputStream(outputStream);
				ObjectOutput output = new ObjectOutputStream(buffer);
		) {
			output.writeObject(obj);
		} catch (IOException ex) {
			throw new ParserException("During serialization occurs problem: " + ex);
		}
	}

	public static SerializationObject desereliazeData(File file) {

		try (
				InputStream inputStream = new FileInputStream(file);
				InputStream buffer = new BufferedInputStream(inputStream);
				ObjectInput input = new ObjectInputStream(buffer);
		) {
			//deserialize the List
			return (SerializationObject) input.readObject();
		} catch (ClassNotFoundException ex) {
			throw new ParserException("During deserialization occurs problem: " + ex);
		} catch (IOException ex) {
			throw new ParserException("During deserialization occurs problem: " + ex);
		}
	}
}
