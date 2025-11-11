package backend.finance.adapters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "backend.finance.adapters",            // interface-adapters
                "backend.finance.application",        // application
                "backend.finance.enterprise"          // enterprise
        }
)
public class ApiUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUserApplication.class, args);
    }
}
