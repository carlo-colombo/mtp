package mtp.services;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import mtp.dataobjects.Entry;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DatastoreServiceImpl implements DatastoreService {

	Map<String, Entry> datastore;

	public DatastoreServiceImpl() {
		try {
			datastore = ChronicleMapBuilder.of(String.class, Entry.class)
					.createPersistedTo(new File("/tmp/mtp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
