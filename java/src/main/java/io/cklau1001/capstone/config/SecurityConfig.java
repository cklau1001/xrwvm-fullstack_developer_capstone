package io.cklau1001.capstone.config;

import io.cklau1001.capstone.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@EnableMethodSecurity  // To enable PreAuthorize()
public class SecurityConfig {

    // inject components by constructor injection
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /* Uncomment this if CSRF is used

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/protected/**").authenticated() // /protected/** requires authentication
                        .anyRequest().permitAll() // all other requests are permitted without authentication
                )
                //.csrf(csrf -> csrf.disable()); // Turn on CSRF protection
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/login")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())); // use raw csrf token
        */


        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/protected/**",
                                        "/api/v1/carinfo/protected/**",
                                        "/api/v1/dealerinfo/protected/**").authenticated()
                                .anyRequest().permitAll()
        )
        .csrf(csrf -> csrf.disable())
        .formLogin(form -> form.disable())
        .logout(logout -> logout.disable())   // use custom one
        .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
       .headers(header -> header
           .frameOptions(frameOptions -> frameOptions.disable())) // to allow frame display in h2 console
                .cors(c -> c.configurationSource(corsConfigurationSource()))
//                .cors(c -> c.disable())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


/*
    To turn off spring security and allow h2 console to work

        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .headers(header -> header
                        .frameOptions(frameOptions -> frameOptions.disable()));


 */
        return http.build();
    }



    /**
     * Configure the AuthenticationManager for this application.
     *
     * @param http - HttpSecurity
     * @param userDetailsService - The custom UserDetailsService of this application
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService)
            throws Exception {

        AuthenticationManagerBuilder authBuilder =  http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

        return authBuilder.build();
    }

    /**
     * Define CORS settings in global level below. Can be combined with @CrossOrigin in controller level
     *
     * @return
     */


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://your-frontend.com"));
        // configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed methods
        configuration.setAllowedHeaders(List.of("*")); // Allowed headers
        configuration.setAllowCredentials(true); // Allow sending cookies/auth headers
        configuration.setMaxAge(3600L); // Max age of pre-flight cache


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }


}
