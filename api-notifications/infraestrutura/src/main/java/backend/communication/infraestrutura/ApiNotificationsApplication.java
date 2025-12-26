package backend.communication.infraestrutura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

import java.util.TimeZone;

@SpringBootApplication
@EnableResilientMethods
public class ApiNotificationsApplication {

	static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(ApiNotificationsApplication.class, args);
	}

}
