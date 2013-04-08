package test;

import main.Attribute;

public class SimpleData {

	@Attribute
	public int a = 666; // muhahaha

	public SimpleData() {
	}

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
		return true;
	}
}
