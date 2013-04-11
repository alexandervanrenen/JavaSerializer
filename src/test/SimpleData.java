package test;

import java.util.Arrays;

import main.Attribute;

public class SimpleData {

	@Attribute
	private int a = 666; // muhahaha

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	@Attribute
	public int[] myArray = new int[23];

	public int[] getMyArray() {
		return myArray;
	}

	@Attribute
	public Person[] personArray = new Person[3];

	public Person[] getPersonArray() {
		return personArray;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleData other = (SimpleData) obj;
		if (a != other.a)
			return false;
		if (!Arrays.equals(myArray, other.myArray))
			return false;
		return true;
	}
}
