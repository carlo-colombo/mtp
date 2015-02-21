package mtp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import mtp.dataobjects.Entry;
import mtp.services.DatastoreService;
import mtp.services.DatastoreServiceImpl;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class MtpConfiguration {

	public ConcurrentMap<String, Entry> datastore() {
		try {
			return ChronicleMapBuilder.of(String.class, Entry.class)
					.createPersistedTo(new File("/tmp/mtp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public DatastoreService datastoreService() {
		return new DatastoreServiceImpl(datastore());
	}
}
