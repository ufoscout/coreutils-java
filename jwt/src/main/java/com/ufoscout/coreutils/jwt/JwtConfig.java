package com.ufoscout.coreutils.jwt;

public final class JwtConfig {

    private final String secret;
    private final String signatureAlgorithm;
    private final long tokenValidityMinutes;

    public JwtConfig(String secret, String signatureAlgorithm, long tokenValidityMinutes) {
        this.secret = secret;
        this.signatureAlgorithm = signatureAlgorithm;
        this.tokenValidityMinutes = tokenValidityMinutes;
    }

    public final String getSecret() {
        return this.secret;
    }

    public final String getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }

    public final long getTokenValidityMinutes() {
        return this.tokenValidityMinutes;
    }

}
