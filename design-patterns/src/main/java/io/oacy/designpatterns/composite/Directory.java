package io.oacy.designpatterns.composite;

import java.util.ArrayList;
import java.util.Iterator;

public class Directory extends Entry {
    private String name;                    // �ļ��е�����
    private ArrayList<Entry> directory = new ArrayList<Entry>();      // �ļ�����Ŀ¼��Ŀ�ļ���
    public Directory(String name) {         // ���캯��
        this.name = name;
    }
    public String getName() {               // ��ȡ����
        return name;
    }
    public int getSize() {                  // ��ȡ��С
        int size = 0;
        Iterator<Entry> it = directory.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry)it.next();
            size += entry.getSize();
        }
        return size;
    }
    public Entry add(Entry entry) {         // ����Ŀ¼��Ŀ
        directory.add(entry);
        return this;
    }
    protected void printList(String prefix) {       // ��ʾĿ¼��Ŀһ��
        System.out.println(prefix + "/" + this);
        Iterator<Entry> it = directory.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry)it.next();
            entry.printList(prefix + "/" + name);
        }
    }
}
