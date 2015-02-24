package mtp.services;

import java.util.Arrays;
import java.util.List;

public class Result {
	private List<String> key;
	private Object value;

	public Result(Object value, String... key) {
		this.value = value;
		this.setKey(Arrays.asList(key));
	}

	public Result(Object value, List<String> key) {
		this.value = value;
		this.setKey(key);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<String> getKey() {
		return key;
	}

	public void setKey(List<String> key) {
		this.key = key;
	}

}
