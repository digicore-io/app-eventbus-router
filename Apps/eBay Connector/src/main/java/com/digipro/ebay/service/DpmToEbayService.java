package com.digipro.ebay.service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.digipro.ebay.ro.DpmPayload;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.call.AddItemCall;
import com.ebay.sdk.call.ReviseInventoryStatusCall;
import com.ebay.sdk.call.VerifyAddItemCall;
import com.ebay.soap.eBLBaseComponents.AbstractRequestType;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.InventoryStatusType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SellerPaymentProfileType;
import com.ebay.soap.eBLBaseComponents.SellerProfilesType;
import com.ebay.soap.eBLBaseComponents.SellerReturnProfileType;
import com.ebay.soap.eBLBaseComponents.SellerShippingProfileType;

import io.digicore.lambda.BaseService;
import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.model.CompanyApp;
import io.digicore.lambda.model.LogStatus;
import io.digicore.lambda.ro.CompanyEventRo;

public class DpmToEbayService extends BaseService {

	Properties props;
	public static ApiContext context;

	//This is purely for testing purposes
	public DpmToEbayService(Properties props) {
		this.props = props;
	}

	public String createProductListing(CompanyEventRo event, DpmPayload payload, CompanyApp companyApp) throws ApiException, Exception {
		ApiContext context = getApiContext(companyApp);

		CoreService service = new CoreService();

		if (!payload.isEbayPublish()) {
			System.err.println("Not publishing. Ebay Publish = false");
			service.log(event, "Ebay Publish = false. Not creating product on eBay", LogStatus.OK);
			return null;
		}

		ItemType item = buildItem(payload);

		System.err.println("Verifying Product " + payload.getId() + " " + payload.getName());
		VerifyAddItemCall verifyCall = new VerifyAddItemCall(context);
		verifyCall.verifyAddItem(item);

		System.err.println("Listing Product " + payload.getId() + " " + payload.getName());
		AddItemCall addCall = new AddItemCall(context);
		addCall.setItem(item);
		addCall.addItem();
		String itemId = addCall.getReturnedItemID();

		String log = String.format("Product Created on eBay. Company ID %s Product ID %s eBay Item ID %s ", event.getCompanyId(), payload.getId(), itemId);
		System.err.println(log);
		service.log(event, log + "\n\n" + GsonUtil.gson.toJson(addCall.getResponseObject()), LogStatus.OK);

		return itemId;

	}

	/**
	 * If status = 0 then just disable the product
	 * @param body
	 */
	public void updateStockOnHand(String itemId, CompanyEventRo event, DpmPayload product, CompanyApp companyApp) throws ApiException, Exception {

		//		System.err.println("EXITING TO BLOCK REPLAYS");
		//		if (true)
		//			return;

		ApiContext context = getApiContext(companyApp);

		System.err.println("Updating QTY: " + product.getQty());
		InventoryStatusType item = new InventoryStatusType();
		item.setItemID(itemId);
		item.setQuantity(product.getQty());

		//TEMP
		//		AmountType value = new AmountType();
		//		value.setValue(new Double(0.99));
		//		item.setStartPrice(value);

		ReviseInventoryStatusCall call = new ReviseInventoryStatusCall(context);
		InventoryStatusType[] types = { item };
		//call.setMessageID("JAMESMESSAGEID");

		//Check to see if I get this back in the notification response......"
		//				 See <CorrelationID>137541140</CorrelationID>
		//				 https://developer.ebay.com/DevZone/guides/features-guide/default.html#notifications/Notif-ItemSold.html

		call.setEndUserIP("TEST1234");
		call.setInventoryStatus(types);
		Object o = call.reviseInventoryStatus();
		System.err.println("eBay reviseInventoryStatus() (Note: Quantity also includes sold) " + GsonUtil.gson.toJson(o));

		CoreService service = new CoreService();
		String log = String.format("Product Updated on eBay. Company ID %s Product ID %s eBay Item ID %s ", event.getCompanyId(), product.getId(), itemId);
		System.err.println(log);
		service.log(event, log + "\n\n" + GsonUtil.gson.toJson(call.getResponseObject()), LogStatus.OK);
	}

	private static ItemType buildItem(DpmPayload product) throws IOException {
		ItemType item = new ItemType();
		item.setTitle(product.getName());
		item.setDescription(product.getDescription());

		//TODO: TBC If we need to pass this in from CMS
		item.setListingType(ListingTypeCodeType.FIXED_PRICE_ITEM);
		item.setCurrency(CurrencyCodeType.USD);

		AmountType amount = new AmountType();
		amount.setValue(Double.valueOf(product.getPurchasePrice()));
		item.setStartPrice(amount);
		item.setListingDuration(ListingDurationCodeType.GTC.value());

		item.setLocation(product.getEbayProductLocation());
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

	private ApiContext getApiContext(CompanyApp companyApp) {

		EbayConfig config = GsonUtil.gson.fromJson(companyApp.getConfig(), EbayConfig.class);
		if (config.geteBayToken() == null)
			throw new RuntimeException("eBayToken (of type Auth'n'Auth) needs to be configured in companyApp DDB table");

		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken(config.geteBayToken());
		apiContext.setApiServerUrl(props.getProperty("EBAY_API_URL"));

		return apiContext;

	}

	public class EbayConfig {
		private List<String> categoryFilter;
		private int categoryLevel;
		private String eBayToken;

		public List<String> getCategoryFilter() {
			return categoryFilter;
		}

		public void setCategoryFilter(List<String> categoryFilter) {
			this.categoryFilter = categoryFilter;
		}

		public int getCategoryLevel() {
			return categoryLevel;
		}

		public void setCategoryLevel(int categoryLevel) {
			this.categoryLevel = categoryLevel;
		}

		public String geteBayToken() {
			return eBayToken;
		}

		public void seteBayToken(String eBayToken) {
			this.eBayToken = eBayToken;
		}

	}

}
