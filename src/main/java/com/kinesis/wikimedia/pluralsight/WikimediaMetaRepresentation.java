package com.kinesis.wikimedia.pluralsight;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class WikimediaMetaRepresentation {
    private URL uri;
    @JsonProperty("request_id")
    private String requestId;
    private String id;
    private String dt;
    private String domain;
    private String stream;
    private String topic;
    private int partition;
    private long offset;

    public WikimediaMetaRepresentation() {
    }
    public WikimediaMetaRepresentation(URL uri, String request_id, String id, String dt, String domain, String stream,
            String topic, int partition, long offset) {
        this.uri = uri;
        this.requestId = request_id;
        this.id = id;
        this.dt = dt;
        this.domain = domain;
        this.stream = stream;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }
    public URL getUri() {
        return uri;
    }
    public void setUri(URL uri) {
        this.uri = uri;
    }
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String request_id) {
        this.requestId = request_id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDt() {
        return dt;
    }
    public void setDt(String dt) {
        this.dt = dt;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getStream() {
        return stream;
    }
    public void setStream(String stream) {
        this.stream = stream;
    }
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public int getPartition() {
        return partition;
    }
    public void setPartition(int partition) {
        this.partition = partition;
    }
    public long getOffset() {
        return offset;
    }
    public void setOffset(long offset) {
        this.offset = offset;
    }

    
}
