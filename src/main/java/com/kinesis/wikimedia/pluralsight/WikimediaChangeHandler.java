package com.kinesis.wikimedia.pluralsight;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kinesis.wikimedia.pluralsight.utils.AWSUtils;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

public class WikimediaChangeHandler implements EventHandler {
    private String topic;
    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());
    private KinesisAsyncClient kinesisClient;

    public WikimediaChangeHandler(String topic, String accessKey, String secretKey) {
        this.topic = topic;
        this.kinesisClient = AWSUtils.createKinesisClient(accessKey, secretKey);
    }

    @Override
    public void onClosed() throws Exception {
        this.kinesisClient.close();
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
            PutRecordRequest record = PutRecordRequest.builder()
            .streamName(topic)
            .partitionKey("partition_key")
            .data(SdkBytes.fromByteArray(arg1.getData().getBytes(StandardCharsets.UTF_8)))
            .build();
            CompletableFuture<PutRecordResponse> putRecordResult = kinesisClient.putRecord(record);
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
