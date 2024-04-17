package hagelbrand.draftlighting.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Data
@NoArgsConstructor
@Configuration
@EnableAsync
@ConfigurationProperties(ignoreInvalidFields = false, prefix = "espn")
public class ESPNConfig {
    private String url;
}
