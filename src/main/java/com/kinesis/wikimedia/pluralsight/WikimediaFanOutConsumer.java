package com.kinesis.wikimedia.pluralsight;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.Consumer;
import com.amazonaws.services.kinesis.model.DescribeStreamConsumerRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamConsumerResult;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.RegisterStreamConsumerRequest;
import com.amazonaws.services.kinesis.model.RegisterStreamConsumerResult;

public class WikimediaFanOutConsumer {
    public static void main(String[] args) {
        String accessKey = "";
        String secretKey = "";
        var kinesisClient = createKinesisClient(accessKey, secretKey);

        Consumer consumer = registerConsumer(kinesisClient);
        waitConsumerActive(kinesisClient, consumer);

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
    private static void waitConsumerActive(AmazonKinesis kinesisClient, Consumer consumer) {
        DescribeStreamConsumerResult result;
        do {
            System.out.println("Waiting for consumer to be active");
            sleep(1000);
            result = kinesisClient
                    .describeStreamConsumer(new DescribeStreamConsumerRequest().withConsumerARN(consumer.getConsumerARN()));
        } while (!result.getConsumerDescription().getConsumerStatus().equals("ACTIVE"));
        System.out.println("Consumer active");
    }
    private static void sleep(long ms) {
        System.out.println("Sleeping");
        try {
            Thread.sleep(ms);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
    private static Consumer registerConsumer(AmazonKinesis kinesisClient) {
        
        RegisterStreamConsumerRequest registerStreamConsumerRequest = new RegisterStreamConsumerRequest();
        registerStreamConsumerRequest.setConsumerName("fan-out-consumer");
        registerStreamConsumerRequest.setStreamARN("arn:aws:kinesis:us-east-1:000000000000:stream/wiki-stream");

        RegisterStreamConsumerResult response = kinesisClient
                .registerStreamConsumer(registerStreamConsumerRequest);
        Consumer consumer = response.getConsumer();
        System.out.println("Registered consumer: " + consumer);

        return consumer;
    }
    private static AmazonKinesis createKinesisClient(String accessKey, String secretKey) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCreds);
        return AmazonKinesisClientBuilder.standard().withCredentials(credentialsProvider).withRegion("us-east-1").build();
    }
}
