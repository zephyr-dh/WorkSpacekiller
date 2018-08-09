package io.oacy.designpatterns.abstractfactory.factory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public abstract class Page {
	protected String title;
	protected String author;
	protected ArrayList<Item> content = new ArrayList<Item>();

	public Page(String title, String author) {
		this.title = title;
		this.author = author;
	}

	public void add(Item item) {
		content.add(item);
	}

	public void output() {
		try {
			String filename = title + ".html";
			Writer writer = new FileWriter(filename);
			writer.write(this.makeHTML());
			System.out.println(this.makeHTML());
			writer.close();
			System.out.println(filename + " ±‡–¥ÕÍ≥…°£");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract String makeHTML();
}
