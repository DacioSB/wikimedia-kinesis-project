package com.kinesis.wikimedia.pluralsight.utils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

public class AWSUtils {
    
    public static KinesisAsyncClient createKinesisClient(String accessKey, String secretKey) {
        KinesisAsyncClient kinesisClient = KinesisAsyncClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, accessKey)))
        .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
        return kinesisClient;
    }
}
