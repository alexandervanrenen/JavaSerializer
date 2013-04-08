package test;

import main.Attribute;

public class ComplexData {

	public ComplexData() {
	}

	@Attribute
	public int aInt;

	@Attribute
	public Integer aInteger = 2;

	@Attribute
	public String aString = "me dose rulse";

	@Attribute
	public SimpleData data = new SimpleData();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexData other = (ComplexData) obj;
		if (aInt != other.aInt)
			return false;
		if (aInteger == null) {
			if (other.aInteger != null)
				return false;
		} else if (!aInteger.equals(other.aInteger))
			return false;
		if (aString == null) {
			if (other.aString != null)
				return false;
		} else if (!aString.equals(other.aString))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

}
