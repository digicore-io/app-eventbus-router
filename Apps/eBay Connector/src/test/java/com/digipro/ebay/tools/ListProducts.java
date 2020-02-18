package com.digipro.ebay.tools;

import java.util.Calendar;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.soap.eBLBaseComponents.ItemType;

public class ListProducts {

	private static String TOKEN = "AgAAAA**AQAAAA**aAAAAA**dnZHXQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ADmYWoAZaFpg+dj6x9nY+seQ**dBAGAA**AAMAAA**hp4PJaWG9U2Nyl5UqMrDLmBY6JfNNEsgMPjDjnNWoaF9bYQRdQRIyict4dWy++Z4zpgJ3sqXzxn1368/nboRcVIojtaf7Ot7vrWflnSInNvMN5vY5ar72f6TIibzVm6dpK0gPi3gNpwgqCoTt00QPlJ2IIDJAkXjAZMXizrq6IDaioPlygkUqHkVEfGH9ORblS2YjFrB4vk4diOYbsKz1uZ6s/fBfbnqb9y5VYvBYUVGv+IRkM7+rZdP+F+7B5JlGeqsdxD8BGKbdImkHwYLz2CYs4b00/GxuzIYT1hoczmJJl6AS/OnXp3a+cqL4E0lvsPNwKuRxtSRncRWxF94qj3JDCrDs8Om0oADb2Imlb0d8xcjryWKzbhkVHEVWNRmYiNOo1X9alsFz4jGfhAK3gOzEDVV3KK9PWYdr6h+/6IzzWqUf8e3qMr4vptKQ/24DIBw/otehcciI11m19MvpADqGdSK9z5Sc+xGyNCVy0SI4lgWV5yYPQYHvmBjQFOJTInGklPfsYRRN8fahqzHX6cFQlxzwkiqLYa30xrJmfW1BJhGH+mw1GHJlNRPEJ2V7+lqfXMlirg1MUC60YfxNztkhh1OEvXs6S/mmOCl5xdfDC7KgvKGZ9mhsS8TaqPUMNXUsSptOJQVQuRRm0mYRry4dwtWpYFeSeApCz3kD9bHodINhJjOCPbgu9ejc5YmPLkH7Io1eTG/Tlt0DcCcaNtILXipdojeWCkLuyCa+fEzV1kNucZVJarv+to8jzD5";

	public static void main(String[] args) {
		try {
			ApiContext apiContext = getApiContext();

			Calendar from = Calendar.getInstance();
			from.add(Calendar.DAY_OF_YEAR, -119);
			GetSellerListCall api = new GetSellerListCall(apiContext);
			api.setStartTimeFilter(new TimeFilter(from, Calendar.getInstance()));

			ItemType[] items = api.getSellerList();
			System.err.println(items.length);
			int i = 0;
			for (ItemType item : items) {
				try {
					GetItemCall api2 = new GetItemCall(apiContext);
					api2.setItemID(item.getItemID());
					ItemType item2 = api2.getItem();

					if (item2 != null) {
						System.err.println(item2.getTitle() + "  -  " + item2.getSKU());
						i++;
						if (i == 100)
							break;
					}
				} catch (Exception e) {
					System.err.println("DELETED");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ApiContext getApiContext() {
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken(TOKEN);
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");

		return apiContext;
	}
}
