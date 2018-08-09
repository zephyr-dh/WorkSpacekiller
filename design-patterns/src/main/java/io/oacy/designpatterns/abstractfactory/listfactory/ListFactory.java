package io.oacy.designpatterns.abstractfactory.listfactory;

import io.oacy.designpatterns.abstractfactory.factory.Factory;
import io.oacy.designpatterns.abstractfactory.factory.Link;
import io.oacy.designpatterns.abstractfactory.factory.Page;
import io.oacy.designpatterns.abstractfactory.factory.Tray;

public class ListFactory extends Factory {
    public Link createLink(String caption, String url) {
        return new ListLink(caption, url);
    }
    public Tray createTray(String caption) {
        return new ListTray(caption);
    }
    public Page createPage(String title, String author) {
        return new ListPage(title, author);
    }
}
