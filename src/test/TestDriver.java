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
		dataToBeSerialized.data.a = 28;

		// Serialize
		ISerializer serializer = new Serializer();
		byte[] result = serializer.marshal(dataToBeSerialized);

		// Deserialize
		ComplexData deserializedData = (ComplexData) serializer.unmarshal(ComplexData.class, result);

		// Check
		assertTrue(deserializedData.equals(dataToBeSerialized));
	}
}
