package main;

public interface ISerializer {

	/** Writes the given object into a byte array. */
	public byte[] marshal(Object data);

	/** Reads the given buffer into a new object of type resultType. */
	public Object unmarshal(Class<?> resultType, byte[] buffer);
}