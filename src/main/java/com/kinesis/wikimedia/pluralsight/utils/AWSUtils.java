package com.kinesis.wikimedia.pluralsight.utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

public class AWSUtils {
    
    public static KinesisAsyncClient createKinesisClient(String accessKey, String secretKey) {
        KinesisAsyncClient kinesisClient = KinesisAsyncClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
        return kinesisClient;
    }

    public static AWSCredentialsProvider getCredentialsProvider(String accessKey, String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(awsCreds);
    }
}
