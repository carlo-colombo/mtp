package mtp.services;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import mtp.dataobjects.Entry;

public class DatastoreServiceImpl implements DatastoreService {

	ConcurrentMap<String, Entry> datastore;

	public DatastoreServiceImpl(ConcurrentMap<String, Entry> concurrentMap) {
		datastore = concurrentMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtp.services.DatastoreService#put(mtp.dataobjects.Entry)
	 */
	@Override
	public void put(Entry entry) {
		datastore.put(
				String.format("%s-%s-%s", entry.getUserId(),
						entry.getTimePlaced(), UUID.randomUUID()), entry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mtp.services.DatastoreService#values()
	 */
	@Override
	public Collection<Entry> values() {
		return datastore.values();
	}
}
