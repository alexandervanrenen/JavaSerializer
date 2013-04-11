package test;

import main.Attribute;

public class Person {

	public Person(int bubble, int tea) {
		this.bubble = bubble;
		this.tea = tea;
	}

	public Person() {
	}

	@Attribute
	public int bubble = 3;

	@Attribute
	public int tea = 4;

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
		Person other = (Person) obj;
		if (bubble != other.bubble)
			return false;
		if (tea != other.tea)
			return false;
		return true;
	}
}
