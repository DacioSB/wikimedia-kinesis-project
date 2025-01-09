package com.kinesis.wikimedia.pluralsight;

import java.nio.charset.StandardCharsets;
import java.util.List;

import java.util.concurrent.ExecutionException;

import com.kinesis.wikimedia.pluralsight.utils.AWSUtils;
import com.kinesis.wikimedia.pluralsight.utils.WikimediaParserUtils;

import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest;
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse;
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest;
import software.amazon.awssdk.services.kinesis.model.Record;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;

import software.amazon.awssdk.core.SdkBytes;

public class WikimediaConsumer {
    private static final String SHARD_ID_000000000001 = "shardId-000000000001";
    private static final String WIKI_STREAM = "wiki-stream";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String accessKey = "";
        String secretKey = "";
        KinesisAsyncClient kinesisClient = AWSUtils.createKinesisClient(accessKey, secretKey);

        GetShardIteratorRequest getShardIteratorRequest = GetShardIteratorRequest.builder()
            .streamName(WIKI_STREAM)
            .shardId(SHARD_ID_000000000001)
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
        var wiki = WikimediaParserUtils.parseWikimedia(wikimediaJson);
        System.out.println("Title: " + wiki.getTitle() + ", ParsedComment: " + wiki.getParsedcomment());
    }

}
