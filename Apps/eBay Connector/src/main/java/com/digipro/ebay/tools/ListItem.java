/*
Copyright (c) 2013 eBay, Inc.
This program is licensed under the terms of the eBay Common Development and
Distribution License (CDDL) Version 1.0 (the "License") and any subsequent  version 
thereof released by eBay.  The then-current version of the License can be found 
at http://www.opensource.org/licenses/cddl1.php and in the eBaySDKLicense file that 
is under the eBay SDK ../docs directory.
*/

package com.digipro.ebay.tools;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.AddItemCall;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.sdk.helper.ConsoleUtil;
import com.ebay.sdk.util.eBayUtil;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.BuyerPaymentMethodCodeType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.FeesType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PictureDetailsType;
import com.ebay.soap.eBLBaseComponents.ReturnPolicyType;
import com.ebay.soap.eBLBaseComponents.SellerPaymentProfileType;
import com.ebay.soap.eBLBaseComponents.SellerProfilesType;
import com.ebay.soap.eBLBaseComponents.SellerReturnProfileType;
import com.ebay.soap.eBLBaseComponents.SellerShippingProfileType;
import com.ebay.soap.eBLBaseComponents.ShippingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShippingServiceCodeType;
import com.ebay.soap.eBLBaseComponents.ShippingServiceOptionsType;
import com.ebay.soap.eBLBaseComponents.ShippingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.StorefrontType;

/**
 * 
 * A simple item listing sample,
 * show basic flow to list an item to eBay Site using eBay SDK.
 * 
 * UserToken
 * AgAAAA**AQAAAA**aAAAAA**RVo1XQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4aiCZaBpg2dj6x9nY+seQ**rAsFAA**AAMAAA**9GQ4PI/YXNGrSz3Y+xs4TVuuV9ZOeXr/DleljNCOLEr/3dJlAOtn/o2NaCheBRhTi1yVJY6BnKNybKRqkoeT2usz6BMqn9pEmmFN4+s/A5/X5kQFUQXHSIEdb4vBboeRQsMg5B+XbheLnRk4S4xEtPdYiWtsHz9yjjxE3EoIW1ZjfSwYk83KDTub5arQ1er0PREAPzgKnOr+OtH/9Iwl8SXnqXqLO1x+n95PPg3tDRQ/6DPHWkq6cl1s668TCTTjGvsGMDmjJ/a4E0ca86q8SvnHTUM7KtRDnNfGki7LbQnxQfLy+CnuNXxTvJL3nlVTaG6nduGMYFCD+dHGSXkagKXo3WtcoKKMetkMk20mJoeS0bC4NspiJHqI0kFIdjbUjiVMLGV3VahHxZXRC30Q2reO6KlPQSAtOdSSECwSiOx+x3Hc9AZo++I+9hU7Y0eEhMh8qzWz3ZdHO31bIJENQblAY9rXyHXdorBY+R2VKRSqnCI3GbgCg+4zLVsiL+jRtWVfiKeFwdURR+aEWRZ+RFnSsnWUiuPsjL6hgV1jJY0XgQJHJRMbkx/rPWTsN5bClD7+iZc7IAaJigWVRYv+3KNTQu/o8rzDyDTYtSNOiJ345Nfdhj1K8gIFHiExnVg512PtczVTdMzDHoDUuLL0J1M4Hx9g8l9Pl2ty739j9Mvc9dJEHrufx8I3EvIpw30bD2Y9YtWobybRD291Oj3PWBLzocDGqdza63z5GDQBA64zeTvM1IR1H1NflhYH6TK3
 *
 *
 * http://cgi.sandbox.ebay.com/ws/eBayISAPI.dll?ViewItem&item=110509987479&ssPageName=STRK:MESELX:IT&_trksid=p3984.m1555.l2649#ht_500wt_1156
 */
public class ListItem {

	// sample category ids supporting custom item specifics
	private static Set<String> sampleCatIDSet = new HashSet<String>();
	static {
		sampleCatIDSet.add("162140");
	}

	public static void main(String[] args) {

		try {

			System.out.print("\n");
			System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
			System.out.print("+ Welcome to eBay SDK for Java Sample +\n");
			System.out.print("+  - ConsoleAddItem                   +\n");
			System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
			System.out.print("\n");

			// [Step 1] Initialize eBay ApiContext object
			System.out.println("===== [1] Account Information ====");
			ApiContext apiContext = getApiContext();

			// [Step 2] Create a new item object.
			System.out.println("===== [2] Item Information ====");
			ItemType item = buildItem();

			// [Step 3] Create call object and execute the call.
			System.out.println("===== [3] Execute the API call ====");
			System.out.println("Begin to call eBay API, please wait ...");

			//			Calendar from = Calendar.getInstance();
			//			from.add(Calendar.DAY_OF_YEAR, -119);
			//			GetSellerListCall api = new GetSellerListCall(apiContext);
			//			api.setStartTimeFilter(new TimeFilter(from, Calendar.getInstance()));

			//			ItemType[] items = api.getSellerList();
			//			System.err.println(items.length);
			//
			//			for (ItemType item : items) {
			//				try {
			//					GetItemCall api2 = new GetItemCall(apiContext);
			//					api2.setItemID(item.getItemID());
			//					ItemType item2 = api2.getItem();
			//					GsonUtil.gson.toJson(item2);
			//					if (item2 != null)
			//						break;
			//				} catch (Exception e) {
			//					System.err.println("DELTETD");
			//				}
			//			}

			//110431328271
			//			      /110431309439
			AddItemCall api = new AddItemCall(apiContext);
			api.setItem(item);
			FeesType fees = api.addItem();
			System.out.println("End to call eBay API, show call result ...");
			System.out.println();

			// [Setp 4] Display results.
			System.out.println("The list was listed successfully!");

			double listingFee = eBayUtil.findFeeByName(fees.getFee(), "ListingFee").getFee().getValue();
			System.out.println("Listing fee is: " + new Double(listingFee).toString());
			System.out.println("Listed Item ID: " + item.getItemID());
		} catch (Exception e) {
			System.out.println("Fail to list the item.");
			e.printStackTrace();
		}
	}

	/**
	 * Build a sample item
	 * @return ItemType object
	 */
	private static ItemType buildItem() throws IOException {

		String input;
		ItemType item = new ItemType();

		// item title
		item.setTitle("Test - JBMed");
		// item description
		item.setDescription("Description");

		// listing type
		item.setListingType(ListingTypeCodeType.FIXED_PRICE_ITEM);
		// listing price
		item.setCurrency(CurrencyCodeType.USD);
		//input = ConsoleUtil.readString("10.0");
		AmountType amount = new AmountType();
		amount.setValue(Double.valueOf("10.0"));
		item.setStartPrice(amount);
		//      item.setStorefront(StorefrontType.);
		// listing duration
		item.setListingDuration(ListingDurationCodeType.GTC.value());

		// item location and country
		item.setLocation("Pensacola");
		item.setCountry(CountryCodeType.US);
		//item.set
		// listing category
		CategoryType cat = new CategoryType();
		//      String inputString = "Primary Category (e.g.";
		//      for(String catId : sampleCatIDSet) {
		//    	  inputString = inputString + "," + catId;
		//      }
		//      inputString += "): ";
		String catID = "162140";
		cat.setCategoryID(catID);
		item.setPrimaryCategory(cat);
		//      item.setPictureDetails(new PictureDetailsType());
		//      item.getPictureDetails().setPictureURL(values);

		// item quantity
		item.setQuantity(new Integer(5));

		// item condition, New
		// item.setConditionID(1000);  

		// item specifics
		if (sampleCatIDSet.contains(catID)) {
			item.setItemSpecifics(buildItemSpecifics());
		}

		/*
		 * The Business Policies API and related Trading API fields are
		 * available in sandbox. It will be available in production for a
		 * limited number of sellers with Version 775. 100 percent of sellers
		 * will be ramped up to use Business Polcies in July 2012
		 */

		//Create Seller Profile container
		SellerProfilesType sellerProfile = new SellerProfilesType();

		//Set Payment ProfileId https://www.bizpolicy.sandbox.ebay.com/businesspolicy/payment?pageNumber=1&profileId=6065700000&totalPages=1&source=manage
		input = "1"; //ConsoleUtil.readString("Enter your Seller Policy Payment ProfileId : ");
		SellerPaymentProfileType sellerPaymentProfile = new SellerPaymentProfileType();
		sellerPaymentProfile.setPaymentProfileID(Long.valueOf("6065700000"));
		sellerProfile.setSellerPaymentProfile(sellerPaymentProfile);

		//Set Shipping ProfileId https://www.bizpolicy.sandbox.ebay.com/businesspolicy/shipping?pageNumber=1&profileId=6065706000&totalPages=1&source=manage
		SellerShippingProfileType sellerShippingProfile = new SellerShippingProfileType();
		//input = ConsoleUtil.readString("Enter your Seller Policy Shipping ProfileId : ");
		sellerShippingProfile.setShippingProfileID(Long.valueOf("6065706000"));
		sellerProfile.setSellerShippingProfile(sellerShippingProfile);

		//Set Return Policy ProfileId  https://www.bizpolicy.sandbox.ebay.com/businesspolicy/return?pageNumber=1&profileId=6065708000&totalPages=1&source=manage
		SellerReturnProfileType sellerReturnProfile = new SellerReturnProfileType();
		//input = ConsoleUtil.readString("Enter your Seller Policy Return ProfileId : ");
		sellerReturnProfile.setReturnProfileID(Long.valueOf("6065708000" + ""));
		sellerProfile.setSellerReturnProfile(sellerReturnProfile);

		//Add Seller Profile to Item
		item.setSellerProfiles(sellerProfile);
		return item;
	}

	/**
	 * Populate eBay SDK ApiContext object with data input from user
	 * @return ApiContext object
	 */
	private static ApiContext getApiContext() throws IOException {

		String input;
		ApiContext apiContext = new ApiContext();

		//set Api Token to access eBay Api Server
		ApiCredential cred = apiContext.getApiCredential();

		//Prod
		//		cred.seteBayToken(
		//				"AgAAAA**AQAAAA**aAAAAA**4Hs+XQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ADmYWoAZaFpg+dj6x9nY+seQ**dBAGAA**AAMAAA**hp4PJaWG9U2Nyl5UqMrDLmBY6JfNNEsgMPjDjnNWoaF9bYQRdQRIyict4dWy++Z4zpgJ3sqXzxn1368/nboRcVIojtaf7Ot7vrWflnSInNvMN5vY5ar72f6TIibzVm6dpK0gPi3gNpwgqCoTt00QPlJ2IIDJAkXjAZMXizrq6IDaioPlygkUqHkVEfGH9ORblS2YjFrB4vk4diOYbsKz1uZ6s/fBfbnqb9y5VYvBYUVGv+IRkM7+rZdP+F+7B5JlGeqsdxD8BGKbdImkHwYLz2CYs4b00/GxuzIYT1hoczmJJl6AS/OnXp3a+cqL4E0lvsPNwKuRxtSRncRWxF94qj3JDCrDs8Om0oADb2Imlb0d8xcjryWKzbhkVHEVWNRmYiNOo1X9alsFz4jGfhAK3gOzEDVV3KK9PWYdr6h+/6IzzWqUf8e3qMr4vptKQ/24DIBw/otehcciI11m19MvpADqGdSK9z5Sc+xGyNCVy0SI4lgWV5yYPQYHvmBjQFOJTInGklPfsYRRN8fahqzHX6cFQlxzwkiqLYa30xrJmfW1BJhGH+mw1GHJlNRPEJ2V7+lqfXMlirg1MUC60YfxNztkhh1OEvXs6S/mmOCl5xdfDC7KgvKGZ9mhsS8TaqPUMNXUsSptOJQVQuRRm0mYRry4dwtWpYFeSeApCz3kD9bHodINhJjOCPbgu9ejc5YmPLkH7Io1eTG/Tlt0DcCcaNtILXipdojeWCkLuyCa+fEzV1kNucZVJarv+to8jzD5");
		//		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");

		//DEV
		cred.seteBayToken(
				"AgAAAA**AQAAAA**aAAAAA**sThGXg**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4aiCZaGpQ2dj6x9nY+seQ**rAsFAA**AAMAAA**CkiDYkRh/RQiblT/Fd35ri0rNC3EL7SzRQ5Tt0YorwOG+aM7xbmI13zWv/BIHVPJwuK6eXbqaZPSMs5v3VP+2cV+hTriumtrRqvnmddqRboN9eiYMOKvFUSrg3nWEr70g6x5cJ4wZtEw//se3muMB5Ses20eWvCEuaA+zULDfx0WdY4xQWOO7IGtYCRLvdYo1KQmYHeWoHvWewsd1RDYQGfID5AnSHgrr0cV1seTQLSyFSbDAzspfNxXgnhw5z7F4IbfzEF08MRYZ1ZEBUPmlt6uszAtoW+1mktMqhxGUN8HE/M6Ev1IXiR4eSNDJgFBa7E720vL2adcDGVuDHRpeFpfq7V0NRMWB5zIYcQLvzwFmYYlVI+loZRuZnD4Tg6nRZLpgl84jKcLcx4SN5ma+mfzsQencnZgMI93GXLliQqR9G6kl5xUVrvl4bu5sN6br/ZBikgdApstIXm8sWvwiSy0CBKYWWMAED2Dw9nhTxkjjcUjk78kdD+jkP/1CrjoDil5WNXs7fo4GV57VQjTeg0dRQRHTBA86tciv7PiYekfxlW2OWsypaRtWr0akkoiLBgGaKhpLg0sUDnu4obRYU0jkyA1aczfx/97Z1grpW/hUOWjgaxa6MR+gN23i7yAXfhKj0OwEUfXdkWABihBGjKfnvGvFBICVhD6mOU50uD2zvyNZZ9nzfiz8PUYqSm03cTL0F0rG5DdOEyQkzyJfrNyQ33S/Ez24JGfMlfVYtHK/06j7167OmvxDUE+lJXT");
		apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");

		return apiContext;
	}

	// build sample item specifics
	public static NameValueListArrayType buildItemSpecifics() {

		//create the content of item specifics
		NameValueListArrayType nvArray = new NameValueListArrayType();
		NameValueListType nv1 = new NameValueListType();
		nv1.setName("Origin");
		nv1.setValue(new String[] { "US" });
		NameValueListType nv2 = new NameValueListType();
		nv2.setName("Year");
		nv2.setValue(new String[] { "2010" });
		nvArray.setNameValueList(new NameValueListType[] { nv1, nv2 });

		return nvArray;
	}

}
