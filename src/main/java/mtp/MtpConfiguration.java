package mtp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import mtp.dataobjects.Entry;
import mtp.services.DatastoreService;
import mtp.services.DatastoreServiceImpl;
import mtp.services.Result;
import mtp.services.View;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MtpConfiguration {

	private static final String DATA_DIR = StringUtils.defaultIfBlank(
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
				datastore());
		View countries = new View("countries", (Entry entry) -> new Result(
				null, entry.getOriginatingCountry()), View.COUNT);

		datastoreServiceImpl.addView(countries);

		return datastoreServiceImpl;
	}
}
