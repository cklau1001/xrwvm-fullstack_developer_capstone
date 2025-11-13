package io.cklau1001.capstone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The servlet filter to validate JWT token
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    private final String TARGET_URL_PATTERN = "/protected";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.info("[doFilterInternal]: entered: BEGIN");

        /*
         Move to next chain, if url path does not match TARGET_URL_PATTERN

         */
        if (!request.getRequestURI().contains(TARGET_URL_PATTERN)) {
            log.info("[doFilterIntenal]: URL [{}] not match TARGET_URL_PATTERN, move to next filter - END",
                    request.getRequestURI());
            filterChain.doFilter(request, response);
            return;

        }

        // Get access token from cookie
        String token = tokenService.getTokenFromCookie(request);
        token = token != null ? token :tokenService.getTokenFromAuthorizationHeader(request);

        /*
           Do not fail at once if token is not found. Let the next filter decide
         */
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = tokenService.getSubject(token);

            log.debug("[doFilterInternal]: username={}", username);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // UsernameNotFoundException thrown if user does not exist
            UsernamePasswordAuthenticationToken usertoken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            usertoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usertoken);
            log.debug("[doFilterInternal]: valid user, security context created, username={}", username);
        }

        log.info("[doFilterIntenal]: move to next filter - END");
        filterChain.doFilter(request, response);
    }
}
