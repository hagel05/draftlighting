package hagelbrand.draftlighting.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(ignoreInvalidFields = false, prefix = "espn")
public class ESPNConfig {
    private String url;
}
