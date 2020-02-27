package com.digipro.ebay.ro.ebay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetItemResponse {
	private String timestamp;
	private String Ack;
	private String CorrelationID;
	private String Version;
	private String Build;
	private String NotificationEventName;
	private String RecipientUserID;
	private String EIASToken;
	Item ItemObject;
	private String _xmlns;

	// Getter Methods 

	public String getTimestamp() {
		return timestamp;
	}

	public String getAck() {
		return Ack;
	}

	public String getCorrelationID() {
		return CorrelationID;
	}

	public String getVersion() {
		return Version;
	}

	public String getBuild() {
		return Build;
	}

	public String getNotificationEventName() {
		return NotificationEventName;
	}

	public String getRecipientUserID() {
		return RecipientUserID;
	}

	public String getEIASToken() {
		return EIASToken;
	}

	public Item getItem() {
		return ItemObject;
	}

	public String get_xmlns() {
		return _xmlns;
	}

	// Setter Methods 

	public void setTimestamp(String Timestamp) {
		this.timestamp = Timestamp;
	}

	public void setAck(String Ack) {
		this.Ack = Ack;
	}

	public void setCorrelationID(String CorrelationID) {
		this.CorrelationID = CorrelationID;
	}

	public void setVersion(String Version) {
		this.Version = Version;
	}

	public void setBuild(String Build) {
		this.Build = Build;
	}

	public void setNotificationEventName(String NotificationEventName) {
		this.NotificationEventName = NotificationEventName;
	}

	public void setRecipientUserID(String RecipientUserID) {
		this.RecipientUserID = RecipientUserID;
	}

	public void setEIASToken(String EIASToken) {
		this.EIASToken = EIASToken;
	}

	public void setItem(Item ItemObject) {
		this.ItemObject = ItemObject;
	}

	public void set_xmlns(String _xmlns) {
		this._xmlns = _xmlns;
	}
}