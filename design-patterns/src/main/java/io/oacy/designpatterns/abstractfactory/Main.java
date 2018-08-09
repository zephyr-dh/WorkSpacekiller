package io.oacy.designpatterns.abstractfactory;

import java.util.Scanner;

import io.oacy.designpatterns.abstractfactory.factory.Factory;
import io.oacy.designpatterns.abstractfactory.factory.Link;
import io.oacy.designpatterns.abstractfactory.factory.Page;
import io.oacy.designpatterns.abstractfactory.factory.Tray;

public class Main {
    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java Main class.name.of.ConcreteFactory");
//            System.out.println("Example 1: java Main listfactory.ListFactory");
//            System.out.println("Example 2: java Main tablefactory.TableFactory");
//            System.exit(0);
//        }
        Scanner scanner=new Scanner(System.in);
    	String in=scanner.nextLine();
        scanner.close();
        Factory factory = Factory.getFactory(in);

        Link people = factory.createLink("人民日报", "http://www.people.com.cn/");
        Link gmw = factory.createLink("光明日报", "http://www.gmw.cn/");

        Link us_yahoo = factory.createLink("Yahoo!", "http://www.yahoo.com/");
        Link jp_yahoo = factory.createLink("Yahoo!Japan", "http://www.yahoo.co.jp/");
        Link excite = factory.createLink("Excite", "http://www.excite.com/");
        Link google = factory.createLink("Google", "http://www.google.com/");

        Tray traynews = factory.createTray("日报");
        traynews.add(people);
        traynews.add(gmw);

        Tray trayyahoo = factory.createTray("Yahoo!");
        trayyahoo.add(us_yahoo);
        trayyahoo.add(jp_yahoo);

        Tray traysearch = factory.createTray("检索引擎");
        traysearch.add(trayyahoo);
        traysearch.add(excite);
        traysearch.add(google);

        Page page = factory.createPage("LinkPage", "杨文轩");
        page.add(traynews);
        page.add(traysearch);
        page.output();
    }
}
