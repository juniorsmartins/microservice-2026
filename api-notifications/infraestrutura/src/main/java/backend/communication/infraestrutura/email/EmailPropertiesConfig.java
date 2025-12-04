package backend.communication.infraestrutura.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class EmailPropertiesConfig {

    @Value("${spring.mail.username}")
    public String mailUsername;
}
