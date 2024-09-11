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
    public static void main(String[] args) throws InterruptedException {
        String accessKey = "";
        String secretKey = "";
        String topic = "wiki-stream";
        EventHandler handler = new WikimediaChangeHandler(topic, accessKey, secretKey);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(handler, URI.create(url));
        EventSource eventSource = builder.build();

        // start the producer in another thread
        eventSource.start();

        // we produce for 10 minutes and block the program until then
        TimeUnit.MINUTES.sleep(10);
    }
}
