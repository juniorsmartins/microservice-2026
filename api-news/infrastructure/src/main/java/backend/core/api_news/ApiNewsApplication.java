package backend.core.api_news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class ApiNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiNewsApplication.class, args);
	}

}
