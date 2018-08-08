package io.oacy.java8FunctionPrograming.chapter2;

import java.util.stream.Stream;

public class Demo_03 {

	public static void main(String[] args) {
		int count = Stream.of(1, 2, 3).reduce(0, (acc, element) -> acc + element);
	}

}
