package main.utils;

public interface RunnableTask {
	void sync(Object lock);
	void async();
}
