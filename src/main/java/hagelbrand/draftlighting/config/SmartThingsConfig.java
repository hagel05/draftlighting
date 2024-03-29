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
    private String url;
    private String pat;
}
