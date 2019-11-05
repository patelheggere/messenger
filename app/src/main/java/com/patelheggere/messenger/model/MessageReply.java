package com.patelheggere.messenger.model;

public class MessageReply {
    private long count;

    public MessageReply(long count) {
        this.count = count;
    }

    public MessageReply() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
