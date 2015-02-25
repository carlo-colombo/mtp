package mtp.services;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mtp.MtpConfiguration;
import mtp.dataobjects.Entry;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class DatastoreServiceImpl implements DatastoreService {

	Map<String, Entry> datastore;
	ConcurrentMap<String, ChronicleMap<String, Result>> views = new ConcurrentHashMap<>();

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

		for (java.util.Map.Entry<String, ChronicleMap<String, Result>> mapEntry : views
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
			ChronicleMap<String, Result> persistedMap;
			try {
				persistedMap = ChronicleMapBuilder.of(String.class,
						Result.class).createPersistedTo(
						new File(MtpConfiguration.DATA_DIR + "/view-"
								+ viewName));
				views.put(viewName, persistedMap);

				datastore
						.entrySet()
						.parallelStream()
						.forEach(
								(java.util.Map.Entry<String, Entry> entry) -> persistedMap
										.put(entry.getKey(), view.getMap()
												.apply(entry.getValue())));

				return persistedMap.values().stream();
			} catch (IOException e) {
			}

		}
		// fallback
		return datastore.values().parallelStream().map(view.getMap());
	}

	@Override
	public Object getView(String viewName, Boolean group, Integer groupLevel) {
		View view = appContext.getBean(viewName, View.class);
		Stream<Result> mapped = getCachedView(viewName);

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
