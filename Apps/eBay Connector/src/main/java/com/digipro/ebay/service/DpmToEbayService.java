package com.digipro.ebay.service;

import java.io.IOException;
import java.util.Properties;

import com.digipro.ebay.ro.DpmPayload;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.call.AddItemCall;
import com.ebay.sdk.call.ReviseItemCall;
import com.ebay.sdk.call.VerifyAddItemCall;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SellerPaymentProfileType;
import com.ebay.soap.eBLBaseComponents.SellerProfilesType;
import com.ebay.soap.eBLBaseComponents.SellerReturnProfileType;
import com.ebay.soap.eBLBaseComponents.SellerShippingProfileType;

import io.digicore.lambda.BaseService;
import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.model.LogStatus;
import io.digicore.lambda.ro.CompanyEventRo;

public class DpmToEbayService extends BaseService {

	Properties props;
	public static ApiContext context;

	//This is purely for testing purposes
	public DpmToEbayService(Properties props) {
		this.props = props;
		if (context == null)
			context = getApiContext();
	}

	public String createProductListing(CompanyEventRo event, DpmPayload payload) throws ApiException, Exception {

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
	public void updateProductListing(String itemId, CompanyEventRo event, DpmPayload payload) throws ApiException, Exception {

		ItemType item = buildItem(payload);
		item.setItemID(itemId);

		ReviseItemCall call = new ReviseItemCall(getApiContext());
		call.setItemToBeRevised(item);
		call.reviseItem();

		CoreService service = new CoreService();
		String log = String.format("Product Updated on eBay. Company ID %s Product ID %s eBay Item ID %s ", event.getCompanyId(), payload.getId(), itemId);
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

	//TODO: Duh. This needs to stored against companyApp 
	private ApiContext getApiContext() {
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		cred.seteBayToken(getParameter("app-ebayconnector-ebay-token"));
		apiContext.setApiServerUrl(props.getProperty("EBAY_API_URL"));

		return apiContext;
	}

}
