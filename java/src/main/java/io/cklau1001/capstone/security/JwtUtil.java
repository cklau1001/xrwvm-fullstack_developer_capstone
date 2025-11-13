package io.cklau1001.capstone.security;

import io.cklau1001.capstone.config.JwtUtilProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.UnsupportedKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtUtilProperties jwtUtilProperties;

    /**
     * Generate the key to encrypt the token
     *
     * Please ensure that the key from ${application.jwtutil.secretKey} has 43 characters (bytes) at least for HmacSHA256,
     * so that the decoded bytes is 32 bytes at least.
     *
     * @return
     */
    private SecretKeySpec getKey() throws UnsupportedKeyException {

        String secret = jwtUtilProperties.getSecretKey();
        String signAlg = jwtUtilProperties.getSignAlg();

        log.debug("[getKey]: secret={}, signAlg={}", secret, signAlg);
        if (secret == null || secret.length() < 43) {
            throw new UnsupportedKeyException("secret length is too small");
        }

        return new SecretKeySpec(Base64.getDecoder().decode(secret), signAlg);

    }

    public String createToken(String subject, List<String> roles) {

        log.debug("[createToken]: subject={}, roles={}", subject, roles);

        String token = Jwts.builder()
                .subject(subject)
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES)))
                .signWith(getKey())
                .compact();

        log.debug("[createToken]: Generated token={}", token);

        return token;
    }

    public Claims parseToken(String token) {


        Claims claims = Jwts.parser().verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Just for demo, we only use the 1st one
        Jws<Claims> jwsClaims = Jwts.parser().verifyWith(getKey())
                .build()
                .parseSignedClaims(token);

        return claims;
    }

    /**
     * Get the target attribute from a token by using a get function of Claims, e.g. Claims.getSubject()
     *
     * @param <T>
     * @param token
     * @param getFunction
     * @return
     */
    public <T> T getClaimAttribute(String token, Function<Claims, T> getFunction) {

        Claims claims = parseToken(token);
        return getFunction.apply(claims);
    }

    public List<?> getRoles(String token) {

        Claims claims = parseToken(token);
        return claims.get("roles", List.class);
    }

}
