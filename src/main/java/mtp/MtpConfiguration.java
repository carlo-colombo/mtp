package mtp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import mtp.dataobjects.Entry;
import mtp.services.DatastoreService;
import mtp.services.DatastoreServiceImpl;
import mtp.services.Result;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MtpViews.class)
public class MtpConfiguration {

	public static final String DATA_DIR = StringUtils.defaultIfBlank(
			System.getenv("OPENSHIFT_DATA_DIR"), "/tmp");

	private ConcurrentMap<String, Entry> datastore() {
		try {
			return ChronicleMapBuilder.of(String.class, Entry.class)
					.createPersistedTo(new File(DATA_DIR + "/datastore"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public DatastoreService datastoreService() {
		DatastoreServiceImpl datastoreServiceImpl = new DatastoreServiceImpl(
				datastore(), this::viewPersister);

		return datastoreServiceImpl;
	}

	private ConcurrentMap<String, Result> viewPersister(String viewName) {
		try {
			return ChronicleMapBuilder.of(String.class, Result.class)
					.createPersistedTo(
							new File(String.format("%s/view-%s", DATA_DIR,
									viewName)));
		} catch (IOException e) {
			// fallback to in memory hash map
			return new ConcurrentHashMap<String, Result>();
		}
	}
}
