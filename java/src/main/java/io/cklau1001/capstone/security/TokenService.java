package io.cklau1001.capstone.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    // Inject through constructor injection
    final JwtUtil jwtUtil;

    public String getTokenFromCookie(HttpServletRequest request) {

        log.debug("[getTokenFromCookie]: entered: cookies={}", Arrays.toString(request.getCookies()));

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;

    }

    /**
     * Get the token from Authorization Header with following syntax
     *   Authorization: Bearer <token>
     *
     * @param request
     * @return
     */
    public String getTokenFromAuthorizationHeader(HttpServletRequest request) {

        log.debug("[getTokenFromAuthorizationHeader]: entered: authorization={}", request.getHeader("Authorization"));

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer", "").trim();
            log.debug("[getTokenFromAuthorizatoinHeader]: token={}", token);
            return token;
            // return authHeader.substring(7);
        }

        return null;
    }

    public void createSessionCookieForToken(String token, HttpServletResponse response, boolean invalidate) {

        int cookieAge = invalidate ? 0 : 30 * 60;

        // add the token into the cookie so that client can return back
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);  // Prevent JavaScript access
        cookie.setPath("/");
        cookie.setMaxAge(cookieAge);
        cookie.setAttribute("SameSite", "Strict");

        /*
          Set the following if React and endpoints are hosted in different domains.
          cookie.setAttribute("SameSite", "None");
          cookie.setDomain("endpoint-domain");

          CorsConfiguration is also needed
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(List.of("React-domain"));
          config.setAllowCredential(true);
          config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
          config.setExposedHeaders(List.of("Set-Cookie"));
          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", config);

         */

        log.info("[createSessionCookieForToken]: access token created: maxAge={}", cookieAge);
        response.addCookie(cookie);

    }

    public String createToken(String username, List<String> roles) {

        return jwtUtil.createToken(username, roles);
    }

    public String getSubject(String token) {
        return jwtUtil.getClaimAttribute(token, Claims::getSubject);
    }

}
