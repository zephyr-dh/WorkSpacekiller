package io.oacy.designpatterns.builder;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
//		if (args.length != 1) {
//			usage();
//			System.exit(0);
//		}
//		if (args[0].equals("plain")) {
//			TextBuilder textbuilder = new TextBuilder();
//			Director director = new Director(textbuilder);
//			director.construct();
//			String result = textbuilder.getResult();
//			System.out.println(result);
//		} else if (args[0].equals("html")) {
//			HTMLBuilder htmlbuilder = new HTMLBuilder();
//			Director director = new Director(htmlbuilder);
//			director.construct();
//			String filename = htmlbuilder.getResult();
//			System.out.println(filename + "文件编写完成。");
//		} else {
//			usage();
//			System.exit(0);
//		}
		usage();
	Scanner scanner=new Scanner(System.in);
	String in=scanner.nextLine();
	if (in.equals("plain")) {
		TextBuilder textbuilder = new TextBuilder();
		Director director = new Director(textbuilder);
		director.construct();
		String result = textbuilder.getResult();
		System.out.println(result);
	} else if (in.equals("html")) {
		HTMLBuilder htmlbuilder = new HTMLBuilder();
		Director director = new Director(htmlbuilder);
		director.construct();
		String filename = htmlbuilder.getResult();
		System.out.println(filename + "文件编写完成。");
	} else {
		usage();
		System.exit(0);
	}
	scanner.close();
}
	

	public static void usage() {
		System.out.println("Usage: java Main plain      编写纯文本文档");
		System.out.println("Usage: java Main html       编写HTML文档");
	}
}
