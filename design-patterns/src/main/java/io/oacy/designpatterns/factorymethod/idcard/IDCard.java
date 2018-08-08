package io.oacy.designpatterns.factorymethod.idcard;

import io.oacy.designpatterns.factorymethod.framework.Product;

public class IDCard extends Product {
    private String owner;
    IDCard(String owner) {
        System.out.println("����" + owner + "��ID����");
        this.owner = owner;
    }
    public void use() {
        System.out.println("ʹ��" + owner + "��ID����");
    }
    public String getOwner() {
        return owner;
    }
}
