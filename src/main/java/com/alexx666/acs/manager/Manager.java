package main.java.com.alexx666.acs.manager;

public abstract class Manager<T> {

	protected T[] things;

	public T[] get() {
		return things;
	}

	@SuppressWarnings("unchecked")
	public void set(T...things) {
		this.things = things;
	}
		
	public abstract void createAll(boolean attr);
	public abstract void destroyAll(boolean attr);
}
