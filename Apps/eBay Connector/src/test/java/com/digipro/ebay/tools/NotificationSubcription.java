package com.digipro.ebay.tools;

import java.io.IOException;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.call.SetNotificationPreferencesCall;
import com.ebay.soap.eBLBaseComponents.ApplicationDeliveryPreferencesType;
import com.ebay.soap.eBLBaseComponents.ApplicationDeviceTypeCodeType;
import com.ebay.soap.eBLBaseComponents.DeviceTypeCodeType;
import com.ebay.soap.eBLBaseComponents.EnableCodeType;
import com.ebay.soap.eBLBaseComponents.NotificationEnableArrayType;
import com.ebay.soap.eBLBaseComponents.NotificationEnableType;
import com.ebay.soap.eBLBaseComponents.NotificationEventTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SetNotificationPreferencesRequestType;
import com.ebay.soap.eBLBaseComponents.WarningLevelCodeType;

public class NotificationSubcription {
	//Dev sandbox token
	private static final String TOKEN = "AgAAAA**AQAAAA**aAAAAA**sThGXg**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4aiCZaGpQ2dj6x9nY+seQ**rAsFAA**AAMAAA**CkiDYkRh/RQiblT/Fd35ri0rNC3EL7SzRQ5Tt0YorwOG+aM7xbmI13zWv/BIHVPJwuK6eXbqaZPSMs5v3VP+2cV+hTriumtrRqvnmddqRboN9eiYMOKvFUSrg3nWEr70g6x5cJ4wZtEw//se3muMB5Ses20eWvCEuaA+zULDfx0WdY4xQWOO7IGtYCRLvdYo1KQmYHeWoHvWewsd1RDYQGfID5AnSHgrr0cV1seTQLSyFSbDAzspfNxXgnhw5z7F4IbfzEF08MRYZ1ZEBUPmlt6uszAtoW+1mktMqhxGUN8HE/M6Ev1IXiR4eSNDJgFBa7E720vL2adcDGVuDHRpeFpfq7V0NRMWB5zIYcQLvzwFmYYlVI+loZRuZnD4Tg6nRZLpgl84jKcLcx4SN5ma+mfzsQencnZgMI93GXLliQqR9G6kl5xUVrvl4bu5sN6br/ZBikgdApstIXm8sWvwiSy0CBKYWWMAED2Dw9nhTxkjjcUjk78kdD+jkP/1CrjoDil5WNXs7fo4GV57VQjTeg0dRQRHTBA86tciv7PiYekfxlW2OWsypaRtWr0akkoiLBgGaKhpLg0sUDnu4obRYU0jkyA1aczfx/97Z1grpW/hUOWjgaxa6MR+gN23i7yAXfhKj0OwEUfXdkWABihBGjKfnvGvFBICVhD6mOU50uD2zvyNZZ9nzfiz8PUYqSm03cTL0F0rG5DdOEyQkzyJfrNyQ33S/Ez24JGfMlfVYtHK/06j7167OmvxDUE+lJXT";

	public static void main(String[] args) {
		try {

			SetNotificationPreferencesCall api = new SetNotificationPreferencesCall(getApiContext());
			api.setWarningLevel(WarningLevelCodeType.HIGH);
			NotificationEnableArrayType at = new NotificationEnableArrayType();
			NotificationEnableType t = new NotificationEnableType();
			t.setEventType(NotificationEventTypeCodeType.ITEM_LISTED);
			t.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType t2 = new NotificationEnableType();
			t2.setEventType(NotificationEventTypeCodeType.ITEM_REVISED);
			t2.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType t3 = new NotificationEnableType();
			t3.setEventType(NotificationEventTypeCodeType.ITEM_SOLD);
			t3.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType t4 = new NotificationEnableType();
			t4.setEventType(NotificationEventTypeCodeType.FIXED_PRICE_TRANSACTION);
			t4.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType t5 = new NotificationEnableType();
			t5.setEventType(NotificationEventTypeCodeType.ITEM_OUT_OF_STOCK);
			t5.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType t6 = new NotificationEnableType();
			t6.setEventType(NotificationEventTypeCodeType.ITEM_CLOSED);
			t6.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType[] types = { t, t2, t3, t4, t5, t6 };
			at.setNotificationEnable(types);

			api.setUserDeliveryPreferenceArray(at);

			ApplicationDeliveryPreferencesType pref = new ApplicationDeliveryPreferencesType();
			pref.setApplicationEnable(EnableCodeType.ENABLE);
			pref.setApplicationURL(
					"https://dev-api.digicore.io/eb-Router/event?companyId=5447&event=EXTERNAL_EBAY_PRODUCT_CHANGE&applicationId=d21ee17b-c1cb-46fe-9ebb-182e27bd7075&token=104e34b3-01af-44d6-9d53-450528902d48");
			pref.setDeviceType(DeviceTypeCodeType.PLATFORM);

			SetNotificationPreferencesRequestType rt = new SetNotificationPreferencesRequestType();
			rt.setApplicationDeliveryPreferences(pref);
			rt.setUserDeliveryPreferenceArray(at);
			api.execute(rt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ApiContext getApiContext() throws IOException {
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken(TOKEN);
		apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");

		return apiContext;
	}
}
