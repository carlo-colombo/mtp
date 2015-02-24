package mtp.services;

public class Result {
	private String[] key;
	private Object value;

	public Result(Object value, String... key) {
		this.setKey(key);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String[] getKey() {
		return key;
	}

	public void setKey(String[] key) {
		this.key = key;
	}
}
