package com.digipro.ebay.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.Body;
import com.digipro.ebay.ro.Payload;
import com.digipro.ebay.ro.api.Data;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.AddItemCall;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.sdk.call.ReviseItemCall;
import com.ebay.sdk.call.VerifyAddItemCall;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.FeesType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SellerPaymentProfileType;
import com.ebay.soap.eBLBaseComponents.SellerProfilesType;
import com.ebay.soap.eBLBaseComponents.SellerReturnProfileType;
import com.ebay.soap.eBLBaseComponents.SellerShippingProfileType;

public class EbayService {

	Properties props;

	public EbayService(Properties props) {
		this.props = props;
	}

	public String createProductListing(Body body) throws ApiException, Exception {

		CoreService service = new CoreService();
		ApiContext context = getApiContext();

		if (!body.getPayload().isEbayPublish()) {
			System.err.println("Not publishing. Ebay Publish = false");
			service.log(body, "Ebay Publish = false. Not creating product on eBay", LogStatus.OK);
			return null;
		}
		ItemType item = buildItem(body.getPayload());

		System.err.println("Verifying Product " + body.getPayload().getId() + " " + body.getPayload().getName());
		VerifyAddItemCall verifyCall = new VerifyAddItemCall(context);
		verifyCall.verifyAddItem(item);

		System.err.println("Listing Product " + body.getPayload().getId() + " " + body.getPayload().getName());
		AddItemCall addCall = new AddItemCall(context);
		addCall.setItem(item);
		addCall.addItem();
		String itemId = addCall.getReturnedItemID();

		System.err.println("Added Item " + itemId);

		service.log(body,
				String.format("Product Created on eBay. Company ID %s Product ID %s eBay Item ID %s ", body.getCompanyId(), body.getPayload().getId(), itemId) + "\n\n" + GsonUtil.gson.toJson(addCall.getResponseObject()),
				LogStatus.OK);

		return itemId;

	}

	/**
	 * If status = 0 then just disable the product
	 * @param body
	 */
	public void updateProductListing(String itemId, Body body) throws ApiException, Exception {

		ItemType item = buildItem(body.getPayload());
		item.setItemID(itemId);

		ReviseItemCall call = new ReviseItemCall(getApiContext());
		call.setItemToBeRevised(item);
		call.reviseItem();

		CoreService service = new CoreService();
		service.log(body,
				String.format("Product Created on eBay. Company ID %s Product ID %s eBay Item ID %s ", body.getCompanyId(), body.getPayload().getId(), itemId) + "\n\n" + GsonUtil.gson.toJson(call.getResponseObject()),
				LogStatus.OK);
	}

	private static ItemType buildItem(Payload product) throws IOException {
		ItemType item = new ItemType();
		item.setTitle(product.getName());
		item.setDescription(product.getDescription());

		//TODO: TBC If we need to pass this in
		item.setListingType(ListingTypeCodeType.FIXED_PRICE_ITEM);
		item.setCurrency(CurrencyCodeType.USD);

		AmountType amount = new AmountType();
		amount.setValue(Double.valueOf(product.getPurchasePrice()));
		item.setStartPrice(amount);
		item.setListingDuration(ListingDurationCodeType.GTC.value());

		item.setLocation("Pensacola"); //TODO Add this to companyApp OR do we need to handle multiple??? 
		item.setCountry(CountryCodeType.US);
		item.setQuantity(product.getQty());
		item.setConditionID(product.getEbayConditionId());
		item.setSKU(product.getSku());

		CategoryType cat = new CategoryType();
		cat.setCategoryID(product.getEbayCategoryId());
		item.setPrimaryCategory(cat);

		item.setCurrency(CurrencyCodeType.USD);

		SellerProfilesType sellerProfile = new SellerProfilesType();
		SellerPaymentProfileType sellerPaymentProfile = new SellerPaymentProfileType();
		sellerPaymentProfile.setPaymentProfileID(Long.valueOf(product.getEbayPaymentPolicyId()));
		sellerProfile.setSellerPaymentProfile(sellerPaymentProfile);

		SellerShippingProfileType sellerShippingProfile = new SellerShippingProfileType();
		sellerShippingProfile.setShippingProfileID(Long.valueOf(product.getEbayFulfillmentPolicyId()));
		sellerProfile.setSellerShippingProfile(sellerShippingProfile);

		SellerReturnProfileType sellerReturnProfile = new SellerReturnProfileType();
		sellerReturnProfile.setReturnProfileID(Long.valueOf(product.getEbayReturnPolicyId()));
		sellerProfile.setSellerReturnProfile(sellerReturnProfile);

		item.setSellerProfiles(sellerProfile);
		return item;
	}

	/**
	 * TODO: Move token to DB
	 * @return
	 */
	private ApiContext getApiContext() {
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();

		//Prod
		//cred.seteBayToken(
		//	"AgAAAA**AQAAAA**aAAAAA**4Hs+XQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ADmYWoAZaFpg+dj6x9nY+seQ**dBAGAA**AAMAAA**hp4PJaWG9U2Nyl5UqMrDLmBY6JfNNEsgMPjDjnNWoaF9bYQRdQRIyict4dWy++Z4zpgJ3sqXzxn1368/nboRcVIojtaf7Ot7vrWflnSInNvMN5vY5ar72f6TIibzVm6dpK0gPi3gNpwgqCoTt00QPlJ2IIDJAkXjAZMXizrq6IDaioPlygkUqHkVEfGH9ORblS2YjFrB4vk4diOYbsKz1uZ6s/fBfbnqb9y5VYvBYUVGv+IRkM7+rZdP+F+7B5JlGeqsdxD8BGKbdImkHwYLz2CYs4b00/GxuzIYT1hoczmJJl6AS/OnXp3a+cqL4E0lvsPNwKuRxtSRncRWxF94qj3JDCrDs8Om0oADb2Imlb0d8xcjryWKzbhkVHEVWNRmYiNOo1X9alsFz4jGfhAK3gOzEDVV3KK9PWYdr6h+/6IzzWqUf8e3qMr4vptKQ/24DIBw/otehcciI11m19MvpADqGdSK9z5Sc+xGyNCVy0SI4lgWV5yYPQYHvmBjQFOJTInGklPfsYRRN8fahqzHX6cFQlxzwkiqLYa30xrJmfW1BJhGH+mw1GHJlNRPEJ2V7+lqfXMlirg1MUC60YfxNztkhh1OEvXs6S/mmOCl5xdfDC7KgvKGZ9mhsS8TaqPUMNXUsSptOJQVQuRRm0mYRry4dwtWpYFeSeApCz3kD9bHodINhJjOCPbgu9ejc5YmPLkH7Io1eTG/Tlt0DcCcaNtILXipdojeWCkLuyCa+fEzV1kNucZVJarv+to8jzD5");
		//apiContext.setApiServerUrl("https://api.ebay.com/wsapi");

		//DEV
		cred.seteBayToken(
				"AgAAAA**AQAAAA**aAAAAA**M24+XQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4aiCZaGpQ2dj6x9nY+seQ**rAsFAA**AAMAAA**CkiDYkRh/RQiblT/Fd35ri0rNC3EL7SzRQ5Tt0YorwOG+aM7xbmI13zWv/BIHVPJwuK6eXbqaZPSMs5v3VP+2cV+hTriumtrRqvnmddqRboN9eiYMOKvFUSrg3nWEr70g6x5cJ4wZtEw//se3muMB5Ses20eWvCEuaA+zULDfx0WdY4xQWOO7IGtYCRLvdYo1KQmYHeWoHvWewsd1RDYQGfID5AnSHgrr0cV1seTQLSyFSbDAzspfNxXgnhw5z7F4IbfzEF08MRYZ1ZEBUPmlt6uszAtoW+1mktMqhxGUN8HE/M6Ev1IXiR4eSNDJgFBa7E720vL2adcDGVuDHRpeFpfq7V0NRMWB5zIYcQLvzwFmYYlVI+loZRuZnD4Tg6nRZLpgl84jKcLcx4SN5ma+mfzsQencnZgMI93GXLliQqR9G6kl5xUVrvl4bu5sN6br/ZBikgdApstIXm8sWvwiSy0CBKYWWMAED2Dw9nhTxkjjcUjk78kdD+jkP/1CrjoDil5WNXs7fo4GV57VQjTeg0dRQRHTBA86tciv7PiYekfxlW2OWsypaRtWr0akkoiLBgGaKhpLg0sUDnu4obRYU0jkyA1aczfx/97Z1grpW/hUOWjgaxa6MR+gN23i7yAXfhKj0OwEUfXdkWABihBGjKfnvGvFBICVhD6mOU50uD2zvyNZZ9nzfiz8PUYqSm03cTL0F0rG5DdOEyQkzyJfrNyQ33S/Ez24JGfMlfVYtHK/06j7167OmvxDUE+lJXT");
		apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");

		return apiContext;
	}
}
