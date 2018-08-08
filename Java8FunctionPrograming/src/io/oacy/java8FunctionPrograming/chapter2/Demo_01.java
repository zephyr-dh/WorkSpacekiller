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

		// �������ƶϵ�����
		final String[] array = { "hello", "world" };
		
		//��ʹ�ù������ڲ��࣬Ҳ���������������������Ҫ���������ڷ�����ı�������ʱ����Ҫ����������Ϊ final��
	}
}
