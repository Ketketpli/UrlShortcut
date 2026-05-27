package com.urlKatz.LambdaService;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateUrlLambda implements RequestHandler<Map<String, Object>, Map<String, String>> {

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {

        UUID id = UUID.randomUUID();
        String shortUrl = id.toString().substring(0, 8);

        String originalUrl = (String) input.get("originalUrl");
        String expirationTime = (String) input.get("expirationTime");

        DynamoDbClient dynamoDB = DynamoDbClient.builder()
                .region(Region.of("sa-east-1"))
                .build();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("shortUrl", AttributeValue.builder().s(shortUrl).build());
        item.put("originalUrl", AttributeValue.builder().s(originalUrl).build());
        item.put("expirationTime", AttributeValue.builder().n(expirationTime).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("shortAWS")
                .item(item)
                .build();

        dynamoDB.putItem(putItemRequest);

        return Map.of(
                "shortUrl", shortUrl
        );
    }
}
