
package com.digipro.ebay.ro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("ApproximateReceiveCount")
    @Expose
    private String approximateReceiveCount;
    @SerializedName("SentTimestamp")
    @Expose
    private String sentTimestamp;
    @SerializedName("SenderId")
    @Expose
    private String senderId;
    @SerializedName("ApproximateFirstReceiveTimestamp")
    @Expose
    private String approximateFirstReceiveTimestamp;

    public String getApproximateReceiveCount() {
        return approximateReceiveCount;
    }

    public void setApproximateReceiveCount(String approximateReceiveCount) {
        this.approximateReceiveCount = approximateReceiveCount;
    }

    public String getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(String sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getApproximateFirstReceiveTimestamp() {
        return approximateFirstReceiveTimestamp;
    }

    public void setApproximateFirstReceiveTimestamp(String approximateFirstReceiveTimestamp) {
        this.approximateFirstReceiveTimestamp = approximateFirstReceiveTimestamp;
    }

}
