package com.ufoscout.coreutils.jwt;

import java.util.Date;

public class Token {

    private static Date DATE = new Date(0);

    private String token;
    private Date createdDate;
    private Date expirationDate;

    public Token() {
        this("",DATE,DATE);
    }
    public Token(String token, Date createdDate, Date expirationDate) {
        this.token = token;
        this.createdDate = createdDate;
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
