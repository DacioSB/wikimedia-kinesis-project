package com.kinesis.wikimedia.pluralsight;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;

/**
 * Hello world!
 *
 */
public class WikimediaMainProducer {
    private static final String HTTPS_STREAM_WIKIMEDIA_ORG_V2_STREAM_RECENTCHANGE = "https://stream.wikimedia.org/v2/stream/recentchange";
    private static final String WIKI_STREAM = "wiki-stream";

    public static void main(String[] args) throws InterruptedException {
        String accessKey = "";
        String secretKey = "";
        String topic = WIKI_STREAM;
        EventHandler handler = new WikimediaChangeHandler(topic, accessKey, secretKey);
        String url = HTTPS_STREAM_WIKIMEDIA_ORG_V2_STREAM_RECENTCHANGE;
        EventSource.Builder builder = new EventSource.Builder(handler, URI.create(url));
        EventSource eventSource = builder.build();

        // start the producer in another thread
        eventSource.start();

        // we produce for 10 minutes and block the program until then
        TimeUnit.MINUTES.sleep(10);
    }
}
