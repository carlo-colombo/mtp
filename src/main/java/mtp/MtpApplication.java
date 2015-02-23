package mtp;

import java.util.Collection;

import mtp.dataobjects.Entry;
import mtp.services.DatastoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@Import(MtpConfiguration.class)
public class MtpApplication {

	@Autowired
	DatastoreService datastoreService;

	public MtpApplication() {

	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public @ResponseBody String post(@RequestBody Entry entry) {
		return datastoreService.put(entry);
	}

	@RequestMapping(value = "/list")
	public @ResponseBody Collection<Entry> list() {
		return datastoreService.list();
	}

	public static void main(String[] args) {
		SpringApplication.run(MtpApplication.class, args);
	}
}
