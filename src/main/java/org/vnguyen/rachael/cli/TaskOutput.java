package org.vnguyen.rachael.cli;

public abstract class TaskOutput<T> implements TaskOutputIF<T> {

	protected T result;
	
	public TaskOutput(T result) {
		this.result = result;
	}	
	
	public T rawResult() {
		return result;
	}
}
