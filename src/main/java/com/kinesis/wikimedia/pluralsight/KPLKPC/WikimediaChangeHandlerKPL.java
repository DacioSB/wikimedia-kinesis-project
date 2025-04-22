package com.kinesis.wikimedia.pluralsight.KPLKPC;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.kinesis.producer.Attempt;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.amazonaws.services.kinesis.producer.UserRecordFailedException;
import com.amazonaws.services.kinesis.producer.UserRecordResult;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.kinesis.wikimedia.pluralsight.utils.AWSUtils;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

public class WikimediaChangeHandlerKPL implements EventHandler {
    private String topic;
    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandlerKPL.class.getSimpleName());
    private KinesisProducer kinesisProducer;

    public WikimediaChangeHandlerKPL(String topic, String accessKey, String secretKey) {
        this.topic = topic;
        this.kinesisProducer = buildProducer(accessKey, secretKey);
    }

    @Override
    public void onClosed() throws Exception {
        //this.kinesisProducer.
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
            byte[] data = arg1.getData().getBytes(StandardCharsets.UTF_8);
            ListenableFuture<UserRecordResult> userRecord = 
                this.kinesisProducer.addUserRecord(this.topic, "partition_key", ByteBuffer.wrap(data));
            //futures add callback
            //Futures.addCallback, write
            Futures.addCallback(
                userRecord,
                new FutureCallback<UserRecordResult>() {
                    @Override
                    public void onSuccess(UserRecordResult userRecordResult) {
                        log.info("Put record result shardId: " + userRecordResult.getShardId()
                        + " sequenceNumber: " + userRecordResult.getSequenceNumber()
                        );
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        if (throwable instanceof UserRecordFailedException) {
                            UserRecordFailedException e = (UserRecordFailedException) throwable;
                            UserRecordResult userRecordResult = e.getResult();
                            Attempt attempt = Iterables.getLast(userRecordResult.getAttempts());
                            log.error("Put record failed shardId: " + userRecordResult.getShardId()
                            + " sequenceNumber: " + userRecordResult.getSequenceNumber()
                            + " errorCode: " + attempt.getErrorCode()
                            + " errorMessage: " + attempt.getErrorMessage()
                            );
                        } else {
                            log.error("Put record failed ", throwable.getMessage());
                        }
                    }
                },
                Executors.newSingleThreadExecutor()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private KinesisProducer buildProducer(String accessKey, String secretKey) {
        KinesisProducerConfiguration config = new KinesisProducerConfiguration()
            .setRequestTimeout(60000)
            .setRecordMaxBufferedTime(15000)
            .setCredentialsProvider(AWSUtils.getCredentialsProvider(accessKey, secretKey))
            .setRegion("us-east-1");

        return new KinesisProducer(config);
    }

    @Override
    public void onOpen() throws Exception {
        // nothing here
    }

    
}