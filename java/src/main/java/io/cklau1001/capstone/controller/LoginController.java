package io.cklau1001.capstone.controller;

import io.cklau1001.capstone.dto.LoginRequest;
import io.cklau1001.capstone.dto.LoginResponse;
import io.cklau1001.capstone.dto.RegistrationRequest;
import io.cklau1001.capstone.security.AuthUserDetailsService;
import io.cklau1001.capstone.security.JwtUtil;
import io.cklau1001.capstone.security.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")

/*
   CORS config in controller level
@CrossOrigin(origins = "http://localhost:5173",  allowCredentials = "true", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.DELETE, RequestMethod.PUT })

 */
public class LoginController {

    // Inject the following by constructor injection
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsService authUserDetailsService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        log.info("[login]: Received login request for user: {}", loginRequest.getUserName());
        // Authentication logic here
        UsernamePasswordAuthenticationToken userIdentity = new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(), loginRequest.getPassword()
        );
        userIdentity.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Get the authenticated identity for a successful login
        Authentication authentication = authenticationManager.authenticate(userIdentity);

        log.info("[login]: AFTER LOGIN: authentication={}", authentication);

        // Extract the list of roles from authentication object
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority ga: authentication.getAuthorities()) {
            roles.add(ga.getAuthority());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("[login]: Set authentication on security context, user={}, authorities={}",
                authentication.getName(), authentication.getAuthorities());

        log.info("[login]: Set authentication on security context, authentication={}",
                authentication);

        // On successful authentication, generate token
        String token = tokenService.createToken(loginRequest.getUserName(), roles);

        tokenService.createSessionCookieForToken(token, response, false);
        return ResponseEntity.ok(new LoginResponse(loginRequest.getUserName(), token, "Authenticated", null));

    }

    @RequestMapping(method = {RequestMethod.GET ,RequestMethod.POST}, value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        String token = tokenService.getTokenFromCookie(request);

        if (token != null) {
            tokenService.createSessionCookieForToken("", response, true);
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new LoginResponse("", "", "Unauthenticated", null));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest, HttpServletResponse response) {

        Map<String, String> result = authUserDetailsService.registerUser(registrationRequest);

        if (result.get("status").equals("ok")) {

            UserDetails userDetails = authUserDetailsService.loadUserByUsername(registrationRequest.getUserName());
            UsernamePasswordAuthenticationToken usertoken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usertoken);
            String token = tokenService.createToken(registrationRequest.getUserName(), List.of("USER"));
            tokenService.createSessionCookieForToken(token, response, false);

            return ResponseEntity.ok(new LoginResponse(registrationRequest.getUserName(), null,
                    result.get("message"), null ));

        } else {
            return ResponseEntity.ok(new LoginResponse(registrationRequest.getUserName(), null,
                    null, result.get("message") ));
        }

    }

    @GetMapping(value = "/protected/test")
    public String testProtected() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        authUserDetailsService.deleteUser("user12");

        return String.format("User [%s] logged in", authentication.getName());
    }

}
