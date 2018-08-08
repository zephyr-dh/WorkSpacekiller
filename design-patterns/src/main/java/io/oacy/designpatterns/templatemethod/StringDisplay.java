package io.oacy.designpatterns.templatemethod;

public class StringDisplay extends AbstractDisplay {    // StringDisplayҲ��AbstractDisplay������ 
    private String string;                              // ��Ҫ��ʾ���ַ���
    private int width;                                  // ���ֽ�Ϊ��λ��������ַ�������
    public StringDisplay(String string) {               // ���캯���н��յ��ַ�����
        this.string = string;                           // �������ֶ���
        this.width = string.getBytes().length;          // ͬʱ���ַ������ֽڳ���Ҳ�������ֶ��У��Թ�����ʹ�� 
    }
    public void open() {                                // ��д��open����
        printLine();                                    // ���ø����printLine��������
    }
    public void print() {                               // print����
        System.out.println("|" + string + "|");         // ���������ֶ��е��ַ���ǰ��ֱ����"|"����ʾ���� 
    }
    public void close() {                               // close����
        printLine();                                    // ��open����һ��������printLine��������
    }
    private void printLine() {                          // ��open��close�������á����ڿɼ�����private�����ֻ���ڱ����б����� 
        System.out.print("+");                          // ��ʾ��ʾ����Ľǵ�"+"
        for (int i = 0; i < width; i++) {               // ��ʾwidth��"-"
            System.out.print("-");                      // ��ɷ���ı߿�
        }
        System.out.println("+");                        // /��ʾ��ʾ����Ľǵ�"+"
    }
}
