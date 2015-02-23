package mtp.services;

import java.util.function.Function;

import mtp.dataobjects.Entry;

public class View {
	private String name;

	private Function<Entry, Result> map;

	public View(String name, Function<Entry, Result> map) {
		super();
		this.name = name;
		this.setMap(map);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Function<Entry, Result> getMap() {
		return map;
	}

	public void setMap(Function<Entry, Result> map) {
		this.map = map;
	}
}
