
package com.digipro.ebay.ro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.digicore.lambda.ro.CompanyEventRo;

public class Record {

    @SerializedName("messageId")
    @Expose
    private String messageId;
    @SerializedName("receiptHandle")
    @Expose
    private String receiptHandle;
    @SerializedName("body")
    @Expose
    private CompanyEventRo body;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("messageAttributes")
    @Expose
    private MessageAttributes messageAttributes;
    @SerializedName("md5OfBody")
    @Expose
    private String md5OfBody;
    @SerializedName("eventSource")
    @Expose
    private String eventSource;
    @SerializedName("eventSourceARN")
    @Expose
    private String eventSourceARN;
    @SerializedName("awsRegion")
    @Expose
    private String awsRegion;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }

    public CompanyEventRo getBody() {
        return body;
    }

    public void setBody(CompanyEventRo body) {
        this.body = body;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public MessageAttributes getMessageAttributes() {
        return messageAttributes;
    }

    public void setMessageAttributes(MessageAttributes messageAttributes) {
        this.messageAttributes = messageAttributes;
    }

    public String getMd5OfBody() {
        return md5OfBody;
    }

    public void setMd5OfBody(String md5OfBody) {
        this.md5OfBody = md5OfBody;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public String getEventSourceARN() {
        return eventSourceARN;
    }

    public void setEventSourceARN(String eventSourceARN) {
        this.eventSourceARN = eventSourceARN;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

}
