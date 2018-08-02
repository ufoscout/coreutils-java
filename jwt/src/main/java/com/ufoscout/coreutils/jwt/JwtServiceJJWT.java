package com.ufoscout.coreutils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.util.Date;

/**
 * Implementation of the {@link JwtService} based on JJWT
 *
 * @author Francesco Cina'
 *
 */
public class JwtServiceJJWT implements JwtService {

    final static String PAYLOAD_CLAIM_KEY = "payload";
    private final SignatureAlgorithm signatureAlgorithm;
    private String secret;
    private long tokenValidityMinutes;
    private final JsonProvider jsonProvider;

    public JwtServiceJJWT(
            JwtConfig jwtConfig,
            final JsonProvider jsonSerializerService) {
        this.secret = jwtConfig.getSecret();
        this.signatureAlgorithm = SignatureAlgorithm.forName(jwtConfig.getSignatureAlgorithm());
        this.tokenValidityMinutes = jwtConfig.getTokenValidityMinutes();
        this.jsonProvider = jsonSerializerService;

    }

    @Override
    public <T> String generate(final T payload) {
        return generate("", payload);
    }

    @Override
    public <T> String generate(final String subject, final T payload) {
        final Date createdDate = new Date();
        return generate(subject, payload, createdDate, calculateExpirationDate(createdDate));
    }

    @Override
    public <T> String generate(final String subject, final T payload, Date createdDate, Date expirationDate) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(PAYLOAD_CLAIM_KEY, jsonProvider.toJson(payload))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

    @Override
    public <T> T parse(final String jwt, final Class<T> payloadClass) {
        final Claims claims = getAllClaimsFromToken(jwt);
        return jsonProvider.fromJson(payloadClass, (String) claims.get(PAYLOAD_CLAIM_KEY));
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + tokenValidityMinutes * 60 * 1000);
    }

    Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(e);
        }
    }

}
