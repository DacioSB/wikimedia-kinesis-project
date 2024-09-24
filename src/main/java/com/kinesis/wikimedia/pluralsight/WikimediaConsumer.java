package com.kinesis.wikimedia.pluralsight;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;

//TODO: refactor so it can be used with WikimediaChangeHandler
public class WikimediaConsumer {
    public static void main(String[] args) {
        String accessKey = "";
        String secretKey = "";
        var kinesisClient = createKinesisClient(accessKey, secretKey);

        GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
        getShardIteratorRequest.setStreamName("wiki-stream");
        getShardIteratorRequest.setShardId("shardId-000000000001");
        getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON");

        GetShardIteratorResult getShardIteratorResult = kinesisClient.getShardIterator(getShardIteratorRequest);
        String shardIterator = getShardIteratorResult.getShardIterator();

        while (true) {
            GetRecordsRequest getRecordsRequest = new GetRecordsRequest();
            getRecordsRequest.setShardIterator(shardIterator);

            GetRecordsResult result = kinesisClient.getRecords(getRecordsRequest);

            List<Record> records = result.getRecords();

            for (Record record : records) {
                processRecord(record);
            }

            sleep(200);

            shardIterator = result.getNextShardIterator();
        }

    }

    private static AmazonKinesis createKinesisClient(String accessKey, String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCreds);
        return AmazonKinesisClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-east-1").build();
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
        ByteBuffer data = record.getData();
        String wikimediaJson = new String(data.array(), StandardCharsets.UTF_8);
        var wiki = parseWikimedia(wikimediaJson);
        System.out.println("Title: " + wiki.getTitle() + ", ParsedComment: " + wiki.getParsedcomment());
    }

    // <dependency>
    // <groupId>com.fasterxml.jackson.core</groupId>
    // <artifactId>jackson-databind</artifactId>
    // <version>2.13.0</version>
    // </dependency>

    private static WikimediaRepresentation parseWikimedia(String wikimediaJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(wikimediaJson, WikimediaRepresentation.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
