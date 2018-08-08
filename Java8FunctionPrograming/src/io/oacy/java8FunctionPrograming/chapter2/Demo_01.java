package io.oacy.java8FunctionPrograming.chapter2;

import java.awt.event.ActionListener;
import java.util.function.BinaryOperator;

public class Demo_01 {
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		// 1
		Runnable noArguments = () -> System.out.println("Hello World");
		// 2
		ActionListener oneArgument = event -> System.out.println("button clicked");
		// 3
		Runnable multiStatement = () -> {
			System.out.print("Hello");
			System.out.println(" World");
		};
		// 4
		BinaryOperator<Long> add = (x, y) -> x + y;
		// 5
		BinaryOperator<Long> addExplicit = (Long x, Long y) -> x + y;

		// 上下文推断的例子
		final String[] array = { "hello", "world" };
		
		//曾使用过匿名内部类，也许遇到过这样的情况：需要引用它所在方法里的变量，这时，需要将变量声明为 final。
	}
}
