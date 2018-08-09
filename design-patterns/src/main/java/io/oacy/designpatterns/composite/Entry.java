package io.oacy.designpatterns.composite;

public abstract class Entry {
	public abstract String getName(); // ��ȡ����

	public abstract int getSize(); // ��ȡ��С

	public Entry add(Entry entry) throws FileTreatmentException { // ����Ŀ¼��Ŀ
		throw new FileTreatmentException();
	}

	public void printList() { // Ϊһ������ǰ׺����ʾĿ¼��Ŀһ��
		printList("");
	}

	protected abstract void printList(String prefix); // Ϊһ������ǰ׺

	public String toString() { // ��ʾ�����������
		return getName() + " (" + getSize() + ")";
	}
}
