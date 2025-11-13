package io.cklau1001.capstone.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * To capture all settings required for DealerService
 *
 * If environment variable is injected, please use uppercase and underscore, e.g.
 *   export APPLICATION_DEALER_BASEURL=https://abc.com
 *
 */
@Component
@ConfigurationProperties(prefix = "application.dealer")
@Getter
@Setter
@NoArgsConstructor
public class DealerProperties {

    private String baseUrl;

}
