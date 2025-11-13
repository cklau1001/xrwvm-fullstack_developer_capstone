package io.cklau1001.capstone.security;

import io.cklau1001.capstone.dto.RegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthUserDetailsService implements UserDetailsService {

    // use InMemoryUserDetailsManager for temp purpose
    // private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final UserRepositoryService userRepositoryService;
    // private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // For checking only
    private final SecurityProperties securityProperties;

    public AuthUserDetailsService(UserRepositoryService userRepositoryService, SecurityProperties securityProperties) {
        this.userRepositoryService = userRepositoryService;

        this.securityProperties = securityProperties;
/*
        this.inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        // Create the users from UserDetails
        inMemoryUserDetailsManager.createUser(
                User.withUsername("user1")
                        .password(passwordEncoder.encode("password"))
                        .authorities("USER", "ADMIN")
                        .build());

        inMemoryUserDetailsManager.createUser(
                User.withUsername("user2")
                        .password(passwordEncoder.encode("password"))
                        .authorities("USER")
                        .build());

        userRepositoryService.loadUserByusername("admin");

        log.info("InMemoryUserDetailsManager initialized with 2 users.");

 */
        log.info("[AuthUserDetailsService]: initialized with DB user repository");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        log.debug("[loadUserByUsername]: username=" + username);
        log.debug("[loadUserByUsername]: configured user=" + securityProperties.getUser().getName());
        log.debug("[loadUserByUsername]: configured password=" + securityProperties.getUser().getPassword());

        // return inMemoryUserDetailsManager.loadUserByUsername(username);
        return userRepositoryService.loadUserByusername(username);
    }

    /**
     * Register new user to this system.
     *
     * @param registrationRequest
     * @return
     */
    public Map<String, String> registerUser(RegistrationRequest registrationRequest) throws UsernameNotFoundException {

        Map<String, String> result;

        if (registrationRequest.getUserName() == null) {
            throw new UsernameNotFoundException("No userName found from registration request");
        }

        log.info("[registerUser]: entered: user=[{}]", registrationRequest.getUserName());


        if (userRepositoryService.userExists(registrationRequest.getUserName())) {
            result = Map.of("status", "noop",
                    "message", registrationRequest.getUserName() + " already registered");
            log.info("[registerUser]: user [{}] already created in user repository - SKIP", registrationRequest.getUserName());

        } else {
/*
            inMemoryUserDetailsManager.createUser(
                    User.withUsername(registrationRequest.getUserName())
                            .password(passwordEncoder.encode(registrationRequest.getPassword()))
                            .authorities("USER")
                            .build()

            );

*/
            // To save into database
            userRepositoryService.createUser(registrationRequest.createAppUser());
            result = Map.of("status", "ok",
                    "message", registrationRequest.getUserName() + " registered");

            log.info("[registerUser]: user [{}] created in user repository", registrationRequest.getUserName());
        }

        return result;
    }

    public void deleteUser(String username) {
        log.info("[deleteUser]: Going to delete {}", username);
        userRepositoryService.deleteUser(username);
    }
}
