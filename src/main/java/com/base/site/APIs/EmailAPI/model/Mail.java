package com.base.site.APIs.EmailAPI.model;

public class Mail {
    String recipient;
    String topic;
    String content;

    public Mail() {
    }

    public Mail(String recipient, String topic, String content) {
        this.recipient = recipient;
        this.content = content;
        this.topic = topic;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
