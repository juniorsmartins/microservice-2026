package backend.ia.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@SpringBootApplication
public class ApiIasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiIasApplication.class, args);
    }

}
