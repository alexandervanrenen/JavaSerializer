package main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Serializer implements ISerializer {

	/* (non-Javadoc)
	 * @see main.ISerializer#marshal(java.lang.Object) */
	@Override
	public byte[] marshal(Object data) {
		// Prepare result structures =)
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(buffer);

		try {
			marshalRec(data, dos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toByteArray();
	}

	/** Writes the given object into the output stream. This method gets called
	 * recursively and does not create streams for writing data. */
	private void marshalRec(Object data, DataOutputStream dos) throws IOException {
		// Serialize each field 8)
		Field[] declaredFields = data.getClass().getDeclaredFields();
		Arrays.sort(declaredFields, comparator);
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(Attribute.class)) {
				try {
					field.setAccessible(true);
					serializeSingle(field.get(data), dos);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** Writes a single field into the stream. */
	private void serializeSingle(Object data, DataOutputStream dos) throws IOException {
		// Just write to out stream
		if (data.getClass() == Byte.class)
			dos.writeByte((Byte) data);
		else if (data.getClass() == Short.class)
			dos.writeInt((Short) data);
		else if (data.getClass() == Character.class)
			dos.writeChar((Character) data);
		else if (data.getClass() == Integer.class)
			dos.writeInt((Integer) data);
		else if (data.getClass() == Long.class)
			dos.writeLong((Long) data);
		else if (data.getClass() == Float.class)
			dos.writeFloat((Float) data);
		else if (data.getClass() == Double.class)
			dos.writeDouble((Double) data);
		else if (data.getClass() == String.class) {
			dos.writeInt(((String) data).length());
			dos.write(((String) data).getBytes());
		} else if (data.getClass().isArray()) {
			dos.writeInt(Array.getLength(data));
			for (int i = 0; i < Array.getLength(data); i++)
				serializeSingle(Array.get(data, i), dos);
		} else if (Collection.class.isAssignableFrom(data.getClass())) {
			Collection<?> collection = (Collection<?>) data;
			dos.writeInt(collection.size());
			for (Object object : collection)
				serializeSingle(object, dos);
		} else {
			marshalRec(data, dos);
		}
	}

	/* (non-Javadoc)
	 * @see main.ISerializer#unmarshal(java.lang.Class, byte[]) */
	@Override
	public Object unmarshal(Class<?> resultType, byte[] buffer) {
		// Prepare result structures =)
		ByteBuffer bb = ByteBuffer.wrap(buffer);

		return unmarshalRec(resultType, bb);
	}

	/** Reads the given buffer into a new object of type resultType. This method
	 * gets called recursively and does not create streams for reading data. */
	private Object unmarshalRec(Class<?> resultType, ByteBuffer bb) {
		// Create object
		Object result = createObject(resultType);

		// Deserialize into created object
		Field[] declaredFields = result.getClass().getDeclaredFields();
		Arrays.sort(declaredFields, comparator);
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(Attribute.class)) {
				try {
					field.setAccessible(true);
					field.set(result, deserializeSingle(field.getType(), bb));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/** Reads the specified field from the buffer. */
	private Object deserializeSingle(Class<?> type, ByteBuffer bb) {
		// Just read from stream
		if (type == Byte.class || type == byte.class)
			return bb.get();
		else if (type == Short.class || type == short.class)
			return bb.getShort();
		else if (type == Character.class || type == char.class)
			return bb.getChar();
		else if (type == Integer.class || type == int.class)
			return bb.getInt();
		else if (type == Long.class || type == long.class)
			return bb.getLong();
		else if (type == Float.class || type == float.class)
			return bb.getFloat();
		else if (type == Double.class || type == double.class)
			return bb.getDouble();
		else if (type == String.class) {
			byte[] nameBuffer = new byte[bb.getInt()];
			bb.get(nameBuffer);
			return new String(nameBuffer);
		} else if (type.isArray()) {
			int length = bb.getInt();
			Object buffer = Array.newInstance(type.getComponentType(), length);
			for (int i = 0; i < length; i++)
				Array.set(buffer, i, deserializeSingle(type.getComponentType(), bb));
			return buffer;
		} else if (Collection.class.isAssignableFrom(type)) {
			int length = bb.getInt();
			Collection collection = createCollection(type);
			TypeVariable<?>[] typeParameters = collection.getClass().getTypeParameters();
			for (int i = 0; i < length; i++)
				collection.add(deserializeSingle(Integer.class, bb)); // TODO: find type of collection
			return collection;
		}
		return unmarshalRec(type, bb);
	}

	private Collection<?> createCollection(Class<?> type) {
		if (!type.isInterface())
			return (Collection<?>) createObject(type);

		if (List.class.isAssignableFrom(type))
			return new ArrayList();

		if (Set.class.isAssignableFrom(type))
			return new TreeSet();

		throw new RuntimeException("unknown container");
	}

	private Object createObject(Class<?> resultType) {
		try {
			Constructor<?> constructor = resultType.getDeclaredConstructor();
			return constructor.newInstance((Object[]) null);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}

	private FieldComparator comparator = new FieldComparator();
}
