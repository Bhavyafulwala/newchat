package com.example.newchat;

public class Msgmodelclass {
    String message;
    String senderid;
    long Timestamp;

    public Msgmodelclass() {
    }

    public Msgmodelclass(String message, String senderid, long timestamp) {
        this.message = message;
        this.senderid = senderid;
        Timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }
}
