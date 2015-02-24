package mtp.services;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import mtp.dataobjects.Entry;

public class View {
	private static Double getDouble(Result res) {
		if (res.getValue() instanceof BigDecimal) {
			return ((BigDecimal) res.getValue()).doubleValue();
		}
		return 0d;
	}

	public static final Collector<Result, ?, Integer> COUNT = Collectors
			.summingInt((Result res) -> 1);

	public static final Collector<Result, ?, Double> SUM = Collectors
			.summingDouble(View::getDouble);

	public static final Collector<Result, ?, DoubleSummaryStatistics> STATS = Collectors
			.summarizingDouble(View::getDouble);

	private String name;

	private Function<Entry, Result> map;

	private Collector<Result, ?, ?> reduce;

	public View(String name, Function<Entry, Result> map) {
		this(name, map, Collectors.toList());
	}

	public View(String name, Function<Entry, Result> map,
			Collector<Result, ?, ?> reduce) {
		super();
		this.name = name;
		this.map = map;
		this.reduce = reduce;
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

	public Collector<Result, ?, ?> getReduce() {
		return reduce;
	}

	public void setReduce(Collector<Result, ?, List<Result>> reduce) {
		this.reduce = reduce;
	}
}
