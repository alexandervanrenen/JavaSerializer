package test;

import static org.junit.Assert.assertTrue;
import main.ISerializer;
import main.Serializer;

import org.junit.Test;

public class TestDriver {

	@Test
	public void serialize() {
		// Create data
		ComplexData dataToBeSerialized = new ComplexData();
		dataToBeSerialized.aInt = 42;
		dataToBeSerialized.aInteger = 1729;
		dataToBeSerialized.aString = "schazam";
		dataToBeSerialized.data.setA(28);
		dataToBeSerialized.data.getMyArray()[3] = 5;
		dataToBeSerialized.data.getPersonArray()[0] = new Person(123, 321);
		dataToBeSerialized.data.getPersonArray()[1] = new Person(2, 1);
		dataToBeSerialized.data.getPersonArray()[2] = new Person(5, 3);

		// Serialize
		ISerializer serializer = new Serializer();
		byte[] result = serializer.marshal(dataToBeSerialized);

		// Deserialize
		ComplexData deserializedData = (ComplexData) serializer.unmarshal(ComplexData.class, result);

		// Check
		assertTrue(deserializedData.equals(dataToBeSerialized));
	}
}
