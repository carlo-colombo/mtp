package mtp.services;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mtp.dataobjects.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class DatastoreServiceImpl implements DatastoreService {

	private Map<String, Entry> datastore;
	private Function<String, ConcurrentMap<String, Result>> builder;

	private ConcurrentMap<String, ConcurrentMap<String, Result>> views = new ConcurrentHashMap<>();

	@Autowired
	private ApplicationContext appContext;

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	public DatastoreServiceImpl(Map<String, Entry> concurrentMap,
			Function<String, ConcurrentMap<String, Result>> viewPersisterBuilder) {
		datastore = concurrentMap;
		builder = viewPersisterBuilder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtp.services.DatastoreService#put(mtp.dataobjects.Entry)
	 */
	@Override
	public String put(Entry entry) {
		// calculate id and put entry into datastore
		String id = String.format("%s-%s-%s", entry.getUserId(),
				entry.getTimePlaced(), UUID.randomUUID());
		entry.setId(id);
		datastore.put(id, entry);

		// update all the views already cached with this entry
		for (java.util.Map.Entry<String, ConcurrentMap<String, Result>> mapEntry : views
				.entrySet()) {
			View view = appContext.getBean(mapEntry.getKey(), View.class);
			mapEntry.getValue().put(id, view.getMap().apply(entry));
		}

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

	private Stream<Result> getCachedView(String viewName) {
		View view = appContext.getBean(viewName, View.class);
		if (views.containsKey(viewName)) {
			return views.get(viewName).values().stream();
		} else {

			// build a map to store views
			ConcurrentMap<String, Result> persistedMap = builder
					.apply(viewName);

			views.put(viewName, persistedMap);

			// calculate map values for already inserted entries
			datastore
					.entrySet()
					.parallelStream()
					.forEach(
							(java.util.Map.Entry<String, Entry> entry) -> persistedMap
									.put(entry.getKey(),
											view.getMap().apply(
													entry.getValue())));

			// return the persisted values
			return persistedMap.values().stream();
		}
	}

	@Override
	public Object getView(String viewName, Boolean group, Integer groupLevel) {
		View view = appContext.getBean(viewName, View.class);
		Stream<Result> mapped = getCachedView(viewName);

		//if no reduce is defined collect the stream into a list
		Collector<Result, ?, ?> collectFunction = group ? Collectors
				.groupingBy(getKey(groupLevel), view.getReduce()) : Collectors
				.toList();

		return mapped.collect(collectFunction);

	}

	private Function<Result, ? extends String> getKey(Integer groupLevel) {
		return (Result res) -> StringUtils.join(
				res.getKey().subList(
						0,
						groupLevel != null ? groupLevel
								: res.getKey().size() - 2), "/");
	}
}
