package io.oacy.designpatterns.builder;

public class Director {
	private Builder builder;

	public Director(Builder builder) { // ��Ϊ���յĲ�����Builder�������
		this.builder = builder; // ���Կ��Խ��䱣����builder�ֶ���
	}

	public void construct() { // ��д�ĵ�
		builder.makeTitle("Greeting"); // ����
		builder.makeString("������������"); // �ַ���
		builder.makeItems(new String[] { // ��Ŀ
				"���Ϻá�", "����á�", });
		builder.makeString("����"); // �����ַ���
		builder.makeItems(new String[] { // ������Ŀ
				"���Ϻá�", "����", "�ټ���", });
		builder.close(); // ����ĵ�
	}
}
