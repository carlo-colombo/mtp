package mtp;

import mtp.services.DatastoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@Import(MtpConfiguration.class)
public class MtpApplication {

	public MtpApplication() {

	}

	public static void main(String[] args) {
		SpringApplication.run(MtpApplication.class, args);
	}
}
