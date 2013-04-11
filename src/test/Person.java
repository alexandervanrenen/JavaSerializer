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
}
