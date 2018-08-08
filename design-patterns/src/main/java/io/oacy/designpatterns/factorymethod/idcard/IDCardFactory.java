package io.oacy.designpatterns.factorymethod.idcard;

import java.util.ArrayList;
import java.util.List;

import io.oacy.designpatterns.factorymethod.framework.Factory;
import io.oacy.designpatterns.factorymethod.framework.Product;

public class IDCardFactory extends Factory {
    private List<String> owners = new ArrayList<String>();
    protected Product createProduct(String owner) {
        return new IDCard(owner);
    }
    protected void registerProduct(Product product) {
        owners.add(((IDCard)product).getOwner());
    }
    public List<String> getOwners() {
        return owners;
    }
}
