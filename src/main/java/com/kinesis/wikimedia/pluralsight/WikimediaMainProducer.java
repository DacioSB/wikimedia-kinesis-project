package com.kinesis.wikimedia.pluralsight;

import java.net.URI;

import com.launchdarkly.eventsource.EventHandler;

/**
 * Hello world!
 *
 */
public class WikimediaMainProducer {
    public static void main(String[] args) {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        String topic = "tweets-stream";
        EventHandler handler = new WikimediaChangeHandler(topic, accessKey, secretKey);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        // start the producer in another thread
        eventSource.start();

        // we produce for 10 minutes and block the program until then
        TimeUnit.MINUTES.sleep(10);
    }
}
