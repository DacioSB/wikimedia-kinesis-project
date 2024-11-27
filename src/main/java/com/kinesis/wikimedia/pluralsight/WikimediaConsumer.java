package com.kinesis.wikimedia.pluralsight;

import java.nio.charset.StandardCharsets;
import java.util.List;

import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest;
import software.amazon.awssdk.services.kinesis.model.Record;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;

//TODO: refactor so it can be used with WikimediaChangeHandler
public class WikimediaConsumer {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String accessKey = "";
        String secretKey = "";
        KinesisAsyncClient kinesisClient = createKinesisClient(accessKey, secretKey);

        GetShardIteratorRequest getShardIteratorRequest = GetShardIteratorRequest.builder()
            .streamName("wiki-stream")
            .shardId("shardId-000000000000")
            .shardIteratorType(ShardIteratorType.TRIM_HORIZON)
            .build();

            String shardIterator = kinesisClient.getShardIterator(getShardIteratorRequest).get().shardIterator();

        while (true) {
            GetRecordsRequest getRecordsRequest = GetRecordsRequest.builder()
            .shardIterator(shardIterator)
            .limit(25) // limit on the maximum number of records to return
            .build();

            GetRecordsResponse result = kinesisClient.getRecords(getRecordsRequest).get();

            List<Record> records = result.records();

            for (Record record : records) {
                processRecord(record);
            }

            sleep(200);

            shardIterator = result.nextShardIterator();
        }

    }

    private static KinesisAsyncClient createKinesisClient(String accessKey, String secretKey) {
        KinesisAsyncClient kinesisClient = KinesisAsyncClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, accessKey)))
        .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
        return kinesisClient;
    }

    private static void sleep(long ms) {
        System.out.println("Sleeping");
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void processRecord(Record record) {
        SdkBytes data = record.data();
        String wikimediaJson = new String(data.asByteArray(), StandardCharsets.UTF_8);
        var wiki = parseWikimedia(wikimediaJson);
        System.out.println("Title: " + wiki.getTitle() + ", ParsedComment: " + wiki.getParsedcomment());
    }


    private static WikimediaRepresentation parseWikimedia(String wikimediaJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(wikimediaJson, WikimediaRepresentation.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
