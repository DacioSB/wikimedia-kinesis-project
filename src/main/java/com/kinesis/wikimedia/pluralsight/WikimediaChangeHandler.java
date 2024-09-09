package com.kinesis.wikimedia.pluralsight;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

public class WikimediaChangeHandler implements EventHandler {
    private String topic;
    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());
    private AmazonKinesis kinesisClient;

    public WikimediaChangeHandler(String topic, String accessKey, String secretKey) {
        this.topic = topic;
        // accesskey and secretkey provided by application.properties as aws.secret.key
        // and aws.access.key
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCreds);
        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard()
                .setCredentials(credentialsProvider);
        this.kinesisClient = clientBuilder.build();
    }

    @Override
    public void onClosed() throws Exception {
        this.kinesisClient.shutdown();
    }

    @Override
    public void onComment(String arg0) throws Exception {
        // nothing here
    }

    @Override
    public void onError(Throwable arg0) {
        log.error("Error", arg0);
    }

    @Override
    public void onMessage(String arg0, MessageEvent arg1) throws Exception {
        try {
            PutRecordRequest record = new PutRecordRequest();
            byte[] msgBytes = arg1.getData().getBytes(StandardCharsets.UTF_8);
            record.setStreamName(topic);
            record.setPartitionKey("1");
            record.setData(ByteBuffer.wrap(msgBytes));
            PutRecordResult putRecordResult = kinesisClient.putRecord(record);
            log.info("Put record result: " + putRecordResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen() throws Exception {
        // nothing here
    }

}
