package com.ufoscout.coreutils.jwt;

import com.ufoscout.coreutils.json.JacksonJsonSerializerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JWTServiceJJWTTest extends BaseTest {

	private final long expireMinutes = 2;
	private JwtServiceJJWT jwtService;

	@BeforeEach
	public void setUp() {
		jwtService = new JwtServiceJJWT(new JwtConfig("secretKey1234567891234secretKey1234567891234secretKey1234567891234", SignatureAlgorithm.HS512.getValue(), expireMinutes),
                new CoreJsonProvider(new JacksonJsonSerializerService()));
	}

    @Test
    public void shouldGenerateAndParseCustomBeans() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.from = "from-" + UUID.randomUUID();
        message.subject = "sub-" + UUID.randomUUID();
        message.sentDate = new Date();

        final String jwt = jwtService.generate(message);
        getLogger().info("Generated JWT:\n{}", jwt);

        final String parsed = jwtService.getAllClaimsFromToken(jwt).get(JwtServiceJJWT.PAYLOAD_CLAIM_KEY, String.class);
        getLogger().info("Parsed JWT:\n{}", parsed);
        assertNotNull(parsed);
        assertFalse(parsed.isEmpty());

        final SimpleMailMessage parsedMessage = jwtService.parse(jwt, SimpleMailMessage.class);
        assertNotNull(parsedMessage);
        assertEquals( message.from, parsedMessage.from );
        assertEquals( message.subject, parsedMessage.subject );
        assertEquals( message.sentDate, parsedMessage.sentDate );
    }

    @Test
    public void shouldSetTheExpirationDate() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.from = "from-" + UUID.randomUUID();
        message.subject = "sub-" + UUID.randomUUID();
        message.sentDate = new Date();

        long beforeTime = new Date().getTime() - 1000;
        final String jwt = jwtService.generate(message);
        getLogger().info("Generated JWT:\n{}", jwt);

        long afterTime = new Date().getTime() + 1000;
        Claims claims = jwtService.getAllClaimsFromToken(jwt);

        long issuedTime = claims.getIssuedAt().getTime();
        assertTrue( issuedTime >= beforeTime );
        assertTrue( issuedTime <= afterTime );

        long expireTime = claims.getExpiration().getTime();
        assertEquals( issuedTime + (expireMinutes * 60 * 1000), expireTime );
    }


    @Test
    public void shouldFailParsingTamperedJwt() {
        assertThrows(io.jsonwebtoken.security.SecurityException.class,
                ()->{
                    final SimpleMailMessage message = new SimpleMailMessage();
                    message.from = "from-" + UUID.randomUUID();
                    message.subject = "sub-" + UUID.randomUUID();
                    message.sentDate = new Date();

                    final String jwt = jwtService.generate(message);
                    getLogger().info("Generated JWT:\n{}", jwt);

                    jwtService.parse(jwt + 1, String.class);
                });
    }

    @Test
    public void shouldFailParsingExpiredBeans() {
        assertThrows(TokenExpiredException.class,
                ()->{
                    final SimpleMailMessage userContext = new SimpleMailMessage();
                    final String JWT = jwtService.generate("", userContext, new Date(), new Date(System.currentTimeMillis() -1 ));
                    jwtService.parse(JWT, SimpleMailMessage.class);
                });
    }

    @Test
    public void shouldAcceptNotExpiredBeans() {
        final SimpleMailMessage userContext = new SimpleMailMessage();
        final String jwt = jwtService.generate(userContext);
        assertNotNull(jwtService.parse(jwt, SimpleMailMessage.class));
    }


    public static class SimpleMailMessage {

        public String from;
        public Date sentDate;
        public String subject;

    }

}
