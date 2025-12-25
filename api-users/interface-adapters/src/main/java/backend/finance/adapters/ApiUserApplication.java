package backend.finance.adapters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.resilience.annotation.EnableResilientMethods;

import java.util.TimeZone;

@SpringBootApplication(
        scanBasePackages = {
                "backend.finance.adapters",            // interface-adapters
                "backend.finance.application",        // application
                "backend.finance.enterprise"          // enterprise
        }
)
@RefreshScope
@EnableResilientMethods
public class ApiUserApplication {

    static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(ApiUserApplication.class, args);
    }
}
