package backend.finance.adapters.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "users")
@ToString
@Getter
@Setter
public class ContactInfoDto {

    private String message;

    private Map<String, String> contactDetail;

    private List<String> onCallSuport;
}

