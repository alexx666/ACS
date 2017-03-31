package com.alexx666.acs.runtime.managers;

public abstract class Manager<T> {

	protected T[] things;

	public T[] get() {
		return things;
	}

	@SuppressWarnings("unchecked")
	public void set(T...things) {
		this.things = things;
	}
		
	public abstract void createAll();
	public abstract void destroyAll();
}
