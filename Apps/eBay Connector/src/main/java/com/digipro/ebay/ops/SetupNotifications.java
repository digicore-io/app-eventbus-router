package com.digipro.ebay.ops;

import java.io.IOException;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.call.SetNotificationPreferencesCall;
import com.ebay.soap.eBLBaseComponents.ApplicationDeliveryPreferencesType;
import com.ebay.soap.eBLBaseComponents.DeviceTypeCodeType;
import com.ebay.soap.eBLBaseComponents.EnableCodeType;
import com.ebay.soap.eBLBaseComponents.NotificationEnableArrayType;
import com.ebay.soap.eBLBaseComponents.NotificationEnableType;
import com.ebay.soap.eBLBaseComponents.NotificationEventTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SetNotificationPreferencesRequestType;
import com.ebay.soap.eBLBaseComponents.WarningLevelCodeType;

public class SetupNotifications {

	private static final String TOKEN = "AgAAAA**AQAAAA**aAAAAA**4JmLXQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ADmYWoAZaFpg+dj6x9nY+seQ**dBAGAA**AAMAAA**hp4PJaWG9U2Nyl5UqMrDLmBY6JfNNEsgMPjDjnNWoaF9bYQRdQRIyict4dWy++Z4zpgJ3sqXzxn1368/nboRcVIojtaf7Ot7vrWflnSInNvMN5vY5ar72f6TIibzVm6dpK0gPi3gNpwgqCoTt00QPlJ2IIDJAkXjAZMXizrq6IDaioPlygkUqHkVEfGH9ORblS2YjFrB4vk4diOYbsKz1uZ6s/fBfbnqb9y5VYvBYUVGv+IRkM7+rZdP+F+7B5JlGeqsdxD8BGKbdImkHwYLz2CYs4b00/GxuzIYT1hoczmJJl6AS/OnXp3a+cqL4E0lvsPNwKuRxtSRncRWxF94qj3JDCrDs8Om0oADb2Imlb0d8xcjryWKzbhkVHEVWNRmYiNOo1X9alsFz4jGfhAK3gOzEDVV3KK9PWYdr6h+/6IzzWqUf8e3qMr4vptKQ/24DIBw/otehcciI11m19MvpADqGdSK9z5Sc+xGyNCVy0SI4lgWV5yYPQYHvmBjQFOJTInGklPfsYRRN8fahqzHX6cFQlxzwkiqLYa30xrJmfW1BJhGH+mw1GHJlNRPEJ2V7+lqfXMlirg1MUC60YfxNztkhh1OEvXs6S/mmOCl5xdfDC7KgvKGZ9mhsS8TaqPUMNXUsSptOJQVQuRRm0mYRry4dwtWpYFeSeApCz3kD9bHodINhJjOCPbgu9ejc5YmPLkH7Io1eTG/Tlt0DcCcaNtILXipdojeWCkLuyCa+fEzV1kNucZVJarv+to8jzD5";

	public static void main(String[] args) {
		try {

			SetNotificationPreferencesCall api = new SetNotificationPreferencesCall(getApiContext());
			api.setWarningLevel(WarningLevelCodeType.HIGH);

			NotificationEnableArrayType at = new NotificationEnableArrayType();

			NotificationEnableType listed = new NotificationEnableType();
			listed.setEventType(NotificationEventTypeCodeType.ITEM_LISTED);
			listed.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType revised = new NotificationEnableType();
			revised.setEventType(NotificationEventTypeCodeType.ITEM_REVISED);
			revised.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType sold = new NotificationEnableType();
			sold.setEventType(NotificationEventTypeCodeType.ITEM_SOLD);
			sold.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType oos = new NotificationEnableType();
			oos.setEventType(NotificationEventTypeCodeType.ITEM_OUT_OF_STOCK);
			oos.setEventEnable(EnableCodeType.ENABLE);

			NotificationEnableType[] types = { listed, revised, listed, sold, oos };
			at.setNotificationEnable(types);

			api.setUserDeliveryPreferenceArray(at);

			ApplicationDeliveryPreferencesType pref = new ApplicationDeliveryPreferencesType();
			pref.setApplicationEnable(EnableCodeType.ENABLE);
			pref.setApplicationURL(
					"https://api.digicoretemp.de/router-api/event?companyId=5447&event=EXTERNAL_EBAY_PRODUCT_CHANGE&applicationId=d21ee17b-c1cb-46fe-9ebb-182e27bd7075&token=104e34b3-01af-44d6-9d53-450528902d48");
			pref.setDeviceType(DeviceTypeCodeType.PLATFORM);

			SetNotificationPreferencesRequestType rt = new SetNotificationPreferencesRequestType();
			rt.setApplicationDeliveryPreferences(pref);
			rt.setUserDeliveryPreferenceArray(at);
			api.execute(rt);

			System.err.println("Notifications are setup");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ApiContext getApiContext() throws IOException {
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken(TOKEN);
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");

		return apiContext;
	}

}
