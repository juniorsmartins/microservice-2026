package backend.ia.infrastructure.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RefreshScope
@Component
@ConfigurationProperties(prefix = "ias")
@ToString
@Getter
@Setter
public class ContactInfoDto {

    private String message;

    private Map<String, String> contactDetail;

    private List<String> onCallSuport;
}

