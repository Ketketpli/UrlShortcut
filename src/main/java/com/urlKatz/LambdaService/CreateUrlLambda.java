package com.urlKatz.LambdaService;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;
import java.util.UUID;

public class CreateUrlLambda implements RequestHandler<Map<String, Object>, Map<String, String>> {

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {

        UUID id = UUID.randomUUID();
        id.toString().substring(0, 8);

        String originalUrl = (String) input.get("originalUrl");
        String expirationTime = (String) input.get("expirationTime");

        return Map.of(
                "originalUrl", originalUrl,
                "expirationTime", expirationTime
        );
    }
}
