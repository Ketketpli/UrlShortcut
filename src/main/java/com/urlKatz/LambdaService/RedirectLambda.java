package com.urlKatz.LambdaService;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

public class RedirectLambda implements RequestHandler<Map<String, Object>, Map<String, String>> {

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {

        String bodyString = (String) input.get("body");

        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> body =
                null;
        try {
            body = mapper.readValue(bodyString, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String shortUrl = body.get("shortUrl");

        DynamoDbClient dynamoDB = DynamoDbClient.builder()
                .region(Region.of("sa-east-1"))
                .build();

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("shortUrl", AttributeValue.builder().s(shortUrl).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("shortAWS")
                .key(key)
                .build();

        GetItemResponse response = dynamoDB.getItem(getItemRequest);
        Map<String, AttributeValue> item = response.item();
        String originalUrl = item.get("originalUrl").s();

        return Map.of(
                "statusCode","302",
                "originalUrl", originalUrl
        );
    }
}
