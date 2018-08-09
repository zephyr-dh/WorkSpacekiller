package io.oacy.designpatterns.abstractfactory.listfactory;

import java.util.Iterator;

import io.oacy.designpatterns.abstractfactory.factory.Item;
import io.oacy.designpatterns.abstractfactory.factory.Tray;

public class ListTray extends Tray {
    public ListTray(String caption) {
        super(caption);
    }
    public String makeHTML() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<li>\n");
        buffer.append(caption + "\n");
        buffer.append("<ul>\n");
        Iterator<?> it = this.tray.iterator();
        while (it.hasNext()) {
            Item item = (Item)it.next();
            buffer.append(item.makeHTML());
        }
        buffer.append("</ul>\n");
        buffer.append("</li>\n");
        return buffer.toString();
    }
}
