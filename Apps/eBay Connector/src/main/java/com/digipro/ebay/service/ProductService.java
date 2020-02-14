package com.digipro.ebay.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.digipro.ebay.dao.ProductDao;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ProductService {

	static Map<String, String> familyIdMap = new HashMap<String, String>();

	public Product getBuildProductFromItem(ItemType item, String companyId, String defaultFamilyId, String schema, ProductDao dao, CategoryConfig config) throws Exception {

		String categoryName = null;
		if (item.getPrimaryCategory() != null)
			categoryName = item.getPrimaryCategory().getCategoryName();

		Product product = new Product();
		product.setProductFamilyId(getFamilyId(categoryName, defaultFamilyId, schema, dao, companyId, config));

		if (product.getProductFamilyId() == null) { //Category excluded
			System.err.println("Ingoring product due to category. Title: " + item.getTitle());
			return null;
		}

		if (item.getTitle().contains("Halyard"))
			System.err.println("Test");
		product.setTitle(item.getTitle());
		product.setLocId(companyId);
		String strRegEx = "<[^>]*>";
		if (item.getDescription() != null)
			product.setDescription(item.getDescription().replaceAll(strRegEx, ""));

		product.setEbayItemId(item.getItemID());
		product.setPrice(String.valueOf(item.getSellingStatus().getCurrentPrice().getValue()));
		product.setQuantity(item.getQuantity());
		product.setSlug(product.getTitle().replaceAll("[^a-zA-Z0-9]", ""));

		product.setPrimaryImageAlt(item.getTitle());
		if (item.getPictureDetails() != null && item.getPictureDetails().getPictureURLLength() > 0)
			product.setPrimaryImage(item.getPictureDetails().getPictureURL()[0]);
		else {
			System.err.println("Skipping product due to no image " + item.getItemID());
			return null;
		}
		if (product.getQuantity() > 0)
			product.setStatus(1);
		else
			product.setStatus(0);
		return product;
	}

	public Product getProductFromItemXML(String xml, String companyId, String defaultFamilyId, String schema, ProductDao dao, CategoryConfig config) throws Exception {
		XmlMapper xmlMapper = new XmlMapper();
		xml = xml.substring(xml.indexOf("<soapenv:Body>") + 14, xml.length());
		xml = xml.substring(0, xml.indexOf("</soapenv:Body>"));

		JsonNode jsonNode = xmlMapper.readTree(xml.getBytes());
		JsonNode item = jsonNode.get("Item");

		String categoryName = item.get("PrimaryCategory").get("CategoryName").textValue();

		String strRegEx = "<[^>]*>";
		Product product = new Product();
		product.setEbayItemId(item.get("ItemID").textValue());
		product.setTitle(item.get("Title").textValue());
		product.setProductFamilyId(getFamilyId(categoryName, defaultFamilyId, schema, dao, companyId, config));

		if (product.getProductFamilyId() == null) { //Category excluded
			if (product.getProductFamilyId() == null) { //Category excluded
				System.err.println("Ingoring product due to category. Title: " + product.getTitle());
				return null;
			}
			return null;
		}

		product.setDescription(item.get("Description").textValue().replaceAll(strRegEx, ""));
		product.setSlug(product.getTitle().replaceAll("[^a-zA-Z0-9]", ""));
		product.setPrice(item.get("SellingStatus").get("CurrentPrice").get("").textValue());
		product.setQuantity(Integer.parseInt(item.get("Quantity").asText()));
		product.setLocId(companyId);
		product.setPrimaryImageAlt(product.getTitle());
		product.setCreated(Calendar.getInstance());
		product.setWeight(getWeightIfAvailable(item));

		if (item.get("PictureDetails").get("PictureURL") != null)
			product.setPrimaryImage(item.get("PictureDetails").get("PictureURL").textValue());

		System.err.println("Item Quantity: " + item.get("Quantity").asText());
		if (product.getQuantity() > 0)
			product.setStatus(1);
		else
			product.setStatus(0);

		return product;

	}

	/**
	 * Set weight if it's available. I'm not sure where the eBay admin user will be setting this so check all of them (if it's local pickup it will be nothing)
	 * @param item
	 * @return
	 */
	private String getWeightIfAvailable(JsonNode item) {
		String weight = null;
		try {

			//First check ShippingDetails.CalculatedShippingRate
			weight = item.get("ShippingDetails").get("CalculatedShippingRate").get("WeightMajor").findValue("").asText();
			if (!"0".equals(weight))
				weight += " " + item.get("ShippingDetails").get("CalculatedShippingRate").get("WeightMajor").get("unit").asText(); //TODO. Get the attribute to find out measurement uint
			else
				weight = null;

			if (!"0".equals(item.get("ShippingDetails").get("CalculatedShippingRate").get("WeightMinor").findValue("").asText())) {
				if (weight != null)
					weight += " ";
				else
					weight = "";

				weight += item.get("ShippingDetails").get("CalculatedShippingRate").get("WeightMinor").findValue("").asText();

				if (!"0".equals(item.get("ShippingDetails").get("CalculatedShippingRate").get("WeightMinor").findValue("").asText()))
					weight += " " + item.get("ShippingDetails").get("CalculatedShippingRate").get("WeightMinor").get("unit").asText(); //TODO. Get the attribute to find out measurement uint
			}

			//Now check ShippingPackageDetails 
			if (weight == null) {
				weight = item.get("ShippingPackageDetails").get("WeightMajor").findValue("").asText();
				if (!"0".equals(weight))
					weight += " " + item.get("ShippingPackageDetails").get("WeightMajor").get("unit").asText(); //TODO. Get the attribute to find out measurement uint
				else
					weight = null;

				if (!"0".equals(item.get("ShippingPackageDetails").get("WeightMinor").findValue("").asText())) {
					if (weight != null)
						weight += " ";
					else
						weight = "";

					weight += item.get("ShippingPackageDetails").get("WeightMinor").findValue("").asText();

					if (!"0".equals(item.get("ShippingPackageDetails").get("WeightMinor").findValue("").asText()))
						weight += " " + item.get("ShippingPackageDetails").get("WeightMinor").get("unit").asText(); //TODO. Get the attribute to find out measurement uint
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not get weight from field");
		}
		return weight;
	}

	/**
	 * Returns null if there is not category or if there is a categoryFilter and it doesn't match. Calling code then ignores this product
	 */
	private String getFamilyId(String categories, String defaultFamilyId, String schema, ProductDao dao, String companyId, CategoryConfig config) throws Exception {

		if (config.getCategoryFilter().size() == 0)
			return defaultFamilyId;

		if (categories != null) {
			String finalFamilyId = null;
			for (String categoryFilter : config.getCategoryFilter()) {
				if (!categories.contains(categoryFilter)) {
					if (finalFamilyId != null)
						return defaultFamilyId + "," + finalFamilyId;
					else
						return defaultFamilyId;
				}

				if (!config.getCategoryFilter().contains(categoryFilter))
					continue;

				String parentFamilyId = "0";
				String category = categories.substring(categories.indexOf(categoryFilter), categoryFilter.length());

				for (int i = 0; i < config.getCategoryLevel(); i++) {
					String familyId = dao.selectFamilyId(category.replace(":", "-"), schema);

					if (familyId == null) {
						//category = category.replace(":", "-");
						ProductFamily pf = new ProductFamily();
						pf.setOrgId(Integer.parseInt(companyId));
						pf.setFamilyName(category.replace(":", "-"));
						pf.setFamilySlug(category.replaceAll("[^a-zA-Z0-9]", "").replace(" ", "-") + "/");
						pf.setPageTitle(category);
						pf.setCategory("store");
						pf.setParentId(parentFamilyId);
						familyId = dao.insertFamily(pf, schema);
					}

					finalFamilyId = familyId;
					parentFamilyId = familyId;
					//categories = categories.resubstring(category.indexOf(":") + 1, categories.length());
					categories = categories.replace(category, "");//(category.indexOf(":") + 1, categories.length());
					if (categories.startsWith(":"))
						categories = categories.replaceFirst(":", "");

					if (categories.indexOf(":") > 0)
						category = categories.substring(0, categories.indexOf(":"));
					else
						category = categories;

					if (StringUtils.isEmpty(category))
						break;

					System.err.println(categories);
					System.err.println(category);
				}

			}

			return defaultFamilyId + "," + finalFamilyId;
		}

		return null;
	}

}
