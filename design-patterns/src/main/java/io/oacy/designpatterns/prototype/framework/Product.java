package io.oacy.designpatterns.prototype.framework;

public interface Product extends Cloneable {
	public abstract void use(String s);

	public abstract Product createClone();
}
