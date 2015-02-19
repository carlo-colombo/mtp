package demo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import mtp.dataobjects.Entry;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DemoApplication {

	@RequestMapping(value = { "", "/" })
	public String index() {
		return "Running ...";
	}

	ChronicleMap<String, Entry> map;

	public DemoApplication() {
		try {
			map = ChronicleMapBuilder.of(String.class, Entry.class)
					.createPersistedTo(new File("/tmp/mtp"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public @ResponseBody String post(@RequestBody Entry entry) {
		map.put(UUID.randomUUID().toString(), entry);
		return null;
	}

	@RequestMapping(value = "/list")
	public @ResponseBody Collection<Entry> list() {
		return map.values();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
