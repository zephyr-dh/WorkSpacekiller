package io.oacy.designpatterns.abstractfactory.tablefactory;

import java.util.Iterator;

import io.oacy.designpatterns.abstractfactory.factory.Item;
import io.oacy.designpatterns.abstractfactory.factory.Tray;

public class TableTray extends Tray {
    public TableTray(String caption) {
        super(caption);                     // 使用super(...)表达式  
    }
    public String makeHTML() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<td>");
        buffer.append("<table width=\"100%\" border=\"1\"><tr>");
        buffer.append("<td bgcolor=\"#cccccc\" align=\"center\" colspan=\""+ tray.size() + "\"><b>" + caption + "</b></td>");
        buffer.append("</tr>\n");
        buffer.append("<tr>\n");
        Iterator<Item> it = tray.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            buffer.append(item.makeHTML());
        }
        buffer.append("</tr></table>");
        buffer.append("</td>");
        return buffer.toString();
    }
}
