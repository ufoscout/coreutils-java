package com.ufoscout.coreutils.jwt;

import com.ufoscout.coreutils.json.JsonSerializerService;

public class CoreJsonProvider implements JsonProvider {

    private JsonSerializerService jjService;

    public CoreJsonProvider(JsonSerializerService jjService) {
        this.jjService = jjService;
    }

    @Override
    public <T> String toJson(T payload) {
        return jjService.toJson(payload);
    }

    @Override
    public <T> T fromJson(Class<T> payloadClass, String json) {
        return jjService.fromJson(payloadClass, json);
    }
}
