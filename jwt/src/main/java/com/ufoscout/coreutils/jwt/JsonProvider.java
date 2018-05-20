package com.ufoscout.coreutils.jwt;

public interface JsonProvider {

    <T> String toJson(T payload);

    <T> T fromJson(Class<T> payloadClass, String json);
}
