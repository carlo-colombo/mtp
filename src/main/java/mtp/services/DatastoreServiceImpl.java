package mtp.services;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import mtp.dataobjects.Entry;

public class DatastoreServiceImpl implements DatastoreService {

	Map<String, Entry> datastore;

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
	public void addView(View view) {

	}
}
