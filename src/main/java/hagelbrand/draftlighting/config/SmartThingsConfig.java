package hagelbrand.draftlighting.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(ignoreInvalidFields = false, prefix = "smartthings")
public class SmartThingsConfig {
    @Value("${smartthings.url}")
    private String smartthingsUrl;
    @Value("${smartthings.pat}")
    private String smartthingsPat;
}
