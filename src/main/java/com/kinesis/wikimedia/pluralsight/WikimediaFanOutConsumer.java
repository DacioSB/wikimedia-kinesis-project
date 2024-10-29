package com.kinesis.wikimedia.pluralsight;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.plaf.synth.Region;

import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.Consumer;
import software.amazon.awssdk.services.kinesis.model.ConsumerStatus;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamConsumerRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamConsumerResponse;
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.Record;
import software.amazon.awssdk.services.kinesis.model.RegisterStreamConsumerRequest;
import software.amazon.awssdk.services.kinesis.model.RegisterStreamConsumerResponse;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;
import software.amazon.awssdk.services.kinesis.model.StartingPosition;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardEvent;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardRequest;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardResponseHandler;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.*;

public class WikimediaFanOutConsumer {
    public static void main(String[] args) {
        String accessKey = "";
        String secretKey = "";
        var kinesisClient = createKinesisClient(accessKey, secretKey);

        Consumer consumer = registerConsumer(kinesisClient);
        waitConsumerActive(kinesisClient, consumer);

        StartingPosition startingPosition = StartingPosition
                .builder()
                .type(ShardIteratorType.TRIM_HORIZON)
                .build();
        SubscribeToShardRequest subscribeToShardRequest
                = SubscribeToShardRequest.builder()
                .consumerARN(consumer.consumerARN())
                .shardId("shardId-000000000001")
                .startingPosition(startingPosition)
                .build();

        while (true) {
            System.out.println("Subscribing to a shard");
            SubscribeToShardResponseHandler recordsHandler
                    = SubscribeToShardResponseHandler
                    .builder()
                    .onError(t -> System.err.println("Subscribe to shard error: " + t.getMessage()))
                    .subscriber(new RecordsProcessor())
                    .build();

            CompletableFuture<Void> future = kinesisClient
                    .subscribeToShard(subscribeToShardRequest, recordsHandler);
            future.join();
        }

    }
    private static void waitConsumerActive(KinesisClient kinesisClient, Consumer consumer) {
        DescribeStreamConsumerResponse result;
        do {
            System.out.println("Waiting for enhanced consumer to become active");
            sleep(500);
            result = kinesisClient
                    .describeStreamConsumer(
                            DescribeStreamConsumerRequest
                                    .builder()
                                    .consumerARN(consumer.consumerARN())
                                    .build()
                    );
        } while (result.consumerDescription().consumerStatus()
                != ConsumerStatus.ACTIVE);
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
    private static Consumer registerConsumer(KinesisClient kinesisClient) {
        
        RegisterStreamConsumerRequest registerStreamConsumerRequest = RegisterStreamConsumerRequest.builder()
                .consumerName("fan-out-consumer")
                .streamARN("arn:aws:kinesis:us-east-1:000000000000:stream/wiki-stream")
                .build();

        RegisterStreamConsumerResponse response = kinesisClient
                .registerStreamConsumer(registerStreamConsumerRequest);
        Consumer consumer = response.consumer();
        System.out.println("Registered consumer: " + consumer);

        return consumer;
    }
    private static KinesisClient createKinesisClient(String accessKey, String secretKey) {
        KinesisClient kinesisClient = KinesisClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, accessKey)))
        .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
        return kinesisClient;
    }

    static class RecordsProcessor implements SubscribeToShardResponseHandler.Visitor {
        @Override
        public void visit(SubscribeToShardEvent event) {
            for (Record record : event.records()) {
                processRecord(record);
            }
        }
    }

    private static void processRecord(Record record) {
        //TODO:
    }
}
