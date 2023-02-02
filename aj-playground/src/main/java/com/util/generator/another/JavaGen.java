package com.util.generator.another;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

abstract class JavaGen implements IJavaGen {
	protected List<String> calledMethods = new LinkedList<>(); // property

	public int getRandomInt() { // Get a random integer
		return new Random().nextInt(10000);
	}

	public abstract void printClassInfo();
}