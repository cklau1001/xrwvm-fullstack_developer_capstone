package io.cklau1001.capstone.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwtutil")
@Getter
@Setter
@NoArgsConstructor
public class JwtUtilProperties {

    private String secretKey;
    private String signAlg;
}
