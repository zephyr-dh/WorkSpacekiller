package io.oacy.designpatterns.templatemethod;

public class Main {

	public static void main(String[] args) {
		AbstractDisplay d1 = new CharDisplay('H'); // ����һ������'H'��CharDisplay���ʵ��
		AbstractDisplay d2 = new StringDisplay("Hello, world."); // ����һ������"Hello, world."��StringDisplay���ʵ��
		AbstractDisplay d3 = new StringDisplay("��ã����硣"); // ����һ������"��ã����硣"��StringDisplay���ʵ��
		d1.display(); // ����d1��d2��d3����AbstractDisplay�������
		d2.display(); // ���Ե��ü̳е�display����
		d3.display(); // ʵ�ʵĳ�����Ϊȡ����CharDisplay���StringDisplay��ľ���ʵ��
	}

}
