package io.oacy.designpatterns.templatemethod;

public class CharDisplay extends AbstractDisplay { // CharDisplay��AbstractDisplay������
	private char ch; // ��Ҫ��ʾ���ַ�

	public CharDisplay(char ch) { // ���캯���н��յ��ַ���
		this.ch = ch; // �������ֶ���
	}

	public void open() { // �ڸ������ǳ��󷽷����˴���д�÷���
		System.out.print("<<"); // ��ʾ��ʼ�ַ�"<<"
	}

	public void print() { // ͬ������дprint�������÷�������display�б��ظ�����
		System.out.print(ch); // ��ʾ�������ֶ�ch�е��ַ�
	}

	public void close() { // ͬ������дclose����
		System.out.println(">>"); // ��ʾ�����ַ�">>"
	}
}
