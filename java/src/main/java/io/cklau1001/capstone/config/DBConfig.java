package io.cklau1001.capstone.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Profile({"!test"})
public class DBConfig {

    /*
    To enable @CreateDate and other audit aware fields
     */

    @Bean
    public AuditorAware<String> auditorAware() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("[auditorAware]: session is not authenticated, return SYSTEM");
            return () -> Optional.of("SYSTEM");
        }

        /*
          For non-protected pages, anonymousUser is returned.

         */
        log.debug("[auditorAware]: session is authenticated, return {}", authentication.getName());
        return () -> Optional.of(authentication.getName());
    }


}
