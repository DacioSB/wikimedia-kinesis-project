package com.kinesis.wikimedia.pluralsight;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//unknown ignore true
@JsonIgnoreProperties(ignoreUnknown = true)
public class WikimediaRepresentation {
    @JsonProperty("$schema")
    private String schema;
    private WikimediaMetaRepresentation meta;
    private long id;
    private String type;
    private int namespace;
    private String title;
    @JsonProperty("title_url")
    private URL titleURL;
    private String comment;
    private long timestamp;
    private String user;
    private boolean bot;
    // Assuming notify_url is also a URL.
    @JsonProperty("notify_url")
    private URL notifyURL;
    @JsonProperty("server_url")
    private URL serverURL;
    private String server_name;
    @JsonProperty("server_script_path")
    private String serverScriptPath;
    private String wiki;
    private String parsedcomment;

    // Getters and setters

    public WikimediaMetaRepresentation getMeta() {
        return meta;
    }

    public void setMeta(WikimediaMetaRepresentation meta) {
        this.meta = meta;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNamespace() {
        return namespace;
    }

    public void setNamespace(int namespace) {
        this.namespace = namespace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getTitleURL() {
        return titleURL;
    }

    public void setTitleURL(URL title_url) {
        this.titleURL = title_url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public URL getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(URL notify_url) {
        this.notifyURL = notify_url;
    }

    public URL getServerURL() {
        return serverURL;
    }

    public void setServerURL(URL server_url) {
        this.serverURL = server_url;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getServerScriptPath() {
        return serverScriptPath;
    }

    public void setServerScriptPath(String server_script_path) {
        this.serverScriptPath = server_script_path;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getParsedcomment() {
        return parsedcomment;
    }

    public void setParsedcomment(String parsedcomment) {
        this.parsedcomment = parsedcomment;
    }

    public WikimediaRepresentation(String schema, WikimediaMetaRepresentation meta, long id, String type, int namespace, String title,
            URL title_url, String comment, long timestamp, String user, boolean bot, URL notify_url, URL server_url,
            String server_name, String server_script_path, String wiki, String parsedcomment) {
        this.schema = schema;
        this.meta = meta;
        this.id = id;
        this.type = type;
        this.namespace = namespace;
        this.title = title;
        this.titleURL = title_url;
        this.comment = comment;
        this.timestamp = timestamp;
        this.user = user;
        this.bot = bot;
        this.notifyURL = notify_url;
        this.serverURL = server_url;
        this.server_name = server_name;
        this.serverScriptPath = server_script_path;
        this.wiki = wiki;
        this.parsedcomment = parsedcomment;
    }
}
