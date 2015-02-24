package mtp.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mtp.dataobjects.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class DatastoreServiceImpl implements DatastoreService {

	Map<String, Entry> datastore;
	Map<String, View> views = new HashMap<>();

	@Autowired
	private ApplicationContext appContext;

	public DatastoreServiceImpl(Map<String, Entry> concurrentMap) {
		datastore = concurrentMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtp.services.DatastoreService#put(mtp.dataobjects.Entry)
	 */
	@Override
	public String put(Entry entry) {
		String id = String.format("%s-%s-%s", entry.getUserId(),
				entry.getTimePlaced(), UUID.randomUUID());
		entry.setId(id);
		datastore.put(id, entry);
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtp.services.DatastoreService#values()
	 */
	@Override
	public Collection<Entry> list() {
		return datastore.values();
	}

	@Override
	public Object getView(String viewName, Boolean group, Integer groupLevel) {

		View view = appContext.getBean(viewName, View.class);
		Stream<Result> map = datastore.values().parallelStream()
				.map(view.getMap());

		Collector<Result, ?, ?> collectFunction = group ? Collectors
				.groupingBy(getKey(groupLevel), view.getReduce()) : Collectors
				.toList();

		return map.collect(collectFunction);

	}

	private Function<Result, ? extends String> getKey(Integer groupLevel) {
		return (Result res) -> org.apache.commons.lang3.StringUtils.join(Arrays
				.asList(res.getKey()).subList(0, groupLevel), "/");
	}
}
