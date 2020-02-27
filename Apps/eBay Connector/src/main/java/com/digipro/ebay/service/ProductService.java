package com.digipro.ebay.service;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.digipro.ebay.dao.ProductDao;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.JsonObject;

import io.digicore.lambda.GsonUtil;

public class ProductService {

	static Map<String, String> familyIdMap = new HashMap<String, String>();

	public Product getBuildProductFromItem(ItemType item, String companyId, String defaultFamilyId, String schema, ProductDao dao, CategoryConfig config) throws Exception {

		String categoryName = null;
		if (item.getPrimaryCategory() != null)
			categoryName = item.getPrimaryCategory().getCategoryName();

		//TEST
		config.setCategoryLevel(2);

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
		product.setWeight(getWeightIfAvailable(item));
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

	/**
	 * TODO Should be able to consolidate this with above by converting it to Product bean and then both pass product into a method that calls family and other common stuff
	 */
	public Product getProductFromItemXML(String xml, String companyId, String defaultFamilyId, String schema, ProductDao dao, CategoryConfig config) throws UnsupportedCategoryException, Exception {

		saveXmlToS3(xml);

		XmlMapper xmlMapper = new XmlMapper();
		xml = xml.substring(xml.indexOf("<soapenv:Body>") + 14, xml.length());
		xml = xml.substring(0, xml.indexOf("</soapenv:Body>"));

		JsonNode jsonNode = xmlMapper.readTree(xml.getBytes());
		JsonNode item = jsonNode.get("Item");

		System.err.println("\n\neBay JSON:\n\n" + GsonUtil.gson.toJson(item));

		String categoryName = item.get("PrimaryCategory").get("CategoryName").textValue();

		String strRegEx = "<[^>]*>";
		Product product = new Product();
		product.setEbayItemId(item.get("ItemID").textValue());
		product.setTitle(item.get("Title").textValue());
		product.setProductFamilyId(getFamilyId(categoryName, defaultFamilyId, schema, dao, companyId, config));

		if (product.getProductFamilyId() == null) { //Category excluded
			throw new UnsupportedCategoryException(String.format("Item not included in category filter. Item ID %s : Title: %s ", product.getEbayItemId(), product.getTitle()));
		}

		product.setDescription(item.get("Description").textValue().replaceAll(strRegEx, ""));
		product.setSlug(product.getTitle().replaceAll("[^a-zA-Z0-9]", ""));
		product.setPrice(item.get("SellingStatus").get("CurrentPrice").get("").textValue());

		//eBay's Quantity field displays an aggregate of available & sold. So to get the true SOH is the following
		product.setQuantity(item.get("Quantity").asInt() - item.get("SellingStatus").get("QuantitySold").asInt() - item.get("SellingStatus").get("QuantitySoldByPickupInStore").asInt());
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
	 * This was put in place due to not being able to parse some of the XMLs that eBay send us. 
	 * Manually search the xml for ItemID to name the file while uploading
	 *  
	 * @param xml
	 */
	protected void saveXmlToS3(String xml) {
		String itemId = null;
		try {
			itemId = xml.substring(xml.indexOf("<ItemID>") + 8, xml.indexOf("</ItemID>"));
			String fileName = itemId + "-" + Calendar.getInstance().getTimeInMillis() + ".xml";

			System.err.println("Uploading to S3 devops bucket: " + fileName);
			String stage = System.getenv("STAGE");
			if (stage == null || stage.equals("local"))
				stage = "dev";

			File tmpFolder = new File("/tmp");
			if (!tmpFolder.isDirectory())
				tmpFolder.mkdir();

			File xmlFile = new File(tmpFolder.getAbsolutePath() + File.separator + fileName);
			FileUtils.write(xmlFile, xml, "UTF-8");

			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
			PutObjectRequest request = new PutObjectRequest("devops-digicore.io-" + stage, "event-bus/apps/eBayConnector/" + fileName, xmlFile);
			s3Client.putObject(request);

			System.err.println("Uploaded to S3 devops bucket: " + fileName);

		} catch (Exception e) {
			throw new RuntimeException("Could not save XML to S3. Item ID: " + itemId, e);
		}
	}

	private String getFamilyId(String eBayCategory, String defaultFamilyId, String schema, ProductDao dao, String companyId, CategoryConfig config) throws Exception {

		/**
		 * TBC: Do we want to do this or should we just allow all categories?
		 */
		if (config.getCategoryFilter().size() == 0)
			return defaultFamilyId;

		String parentFamilyId = "0";
		String finalFamilyId = null;
		for (String filter : config.getCategoryFilter()) {
			if (eBayCategory.contains(filter))
				finalFamilyId = createCategories(eBayCategory, filter, schema, dao, companyId, config, parentFamilyId, 1);
		}

		if (finalFamilyId == null)
			return null;
		else if (finalFamilyId.equals("0"))
			return defaultFamilyId;
		else
			return defaultFamilyId + "," + finalFamilyId;

	}

	private String createCategories(String eBayCategory, String filter, String schema, ProductDao dao, String companyId, CategoryConfig config, String familyId, int depth) throws Exception {

		if (depth > config.getCategoryLevel())
			return familyId;

		String categoryPart = null;

		if (depth == 1)
			categoryPart = eBayCategory.substring(eBayCategory.indexOf(filter), eBayCategory.indexOf(filter) + filter.length());
		else {
			if (eBayCategory.contains(":"))
				categoryPart = eBayCategory.substring(0, eBayCategory.indexOf(":"));
			else
				categoryPart = eBayCategory;
		}

		System.err.println(categoryPart);

		if (categoryPart.trim().equals(""))
			return familyId;

		if (!familyIdMap.containsKey(categoryPart)) {

			String existingFamilyId = dao.selectFamilyId(categoryPart, schema);

			if (existingFamilyId == null) {
				ProductFamily pf = new ProductFamily();
				pf.setOrgId(Integer.parseInt(companyId));
				pf.setFamilyName(categoryPart.replace(":", "-"));
				pf.setFamilySlug(categoryPart.replaceAll("[^a-zA-Z0-9]", "").replace(" ", "-") + "/");
				pf.setPageTitle(categoryPart);
				pf.setCategory("store");
				pf.setParentId(familyId);
				familyId = dao.insertFamily(pf, schema);
			} else
				familyId = existingFamilyId;

			familyIdMap.put(categoryPart, familyId);
		} else
			familyId = familyIdMap.get(categoryPart);

		int index = eBayCategory.indexOf(categoryPart);
		eBayCategory = eBayCategory.substring(index + categoryPart.length(), eBayCategory.length());
		if (eBayCategory.startsWith(":"))
			eBayCategory = eBayCategory.replaceFirst(":", "");

		return createCategories(eBayCategory, filter, schema, dao, companyId, config, familyId, ++depth);
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
	 * TODO: Hack to clean up
	 * @param item
	 * @return
	 */
	private String getWeightIfAvailable(ItemType item) {
		String weight = null;
		try {
			if (item.getShippingPackageDetails() == null)
				return null;

			//First check ShippingDetails.CalculatedShippingRate
			weight = item.getShippingPackageDetails().getWeightMajor().getValue().toString();
			if (!"0".equals(weight))
				weight += " " + item.getShippingPackageDetails().getWeightMajor().getUnit();
			else
				weight = null;

			if (!"0".equals(item.getShippingPackageDetails().getWeightMinor().getValue().toString())) {
				if (weight != null)
					weight += " ";
				else
					weight = "";

				weight += item.getShippingPackageDetails().getWeightMinor().getValue().toString() + item.getShippingPackageDetails().getWeightMinor().getUnit();

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not get weight from field");
		}
		return weight;
	}

	//	/**
	//	 * Returns null if there is not category or if there is a categoryFilter and it doesn't match. Calling code then ignores this product
	//	 */
	//	private String getFamilyIdOldMethod(String categories, String defaultFamilyId, String schema, ProductDao dao, String companyId, CategoryConfig config) throws Exception {
	//
	//		if (config.getCategoryFilter().size() == 0)
	//			return defaultFamilyId;
	//
	//		if (categories != null) {
	//			System.err.println("eBay Cat: " + categories);
	//			String finalFamilyId = null;
	//			for (String categoryFilter : config.getCategoryFilter()) {
	//				if (!categories.contains(categoryFilter)) {
	//					if (finalFamilyId != null)
	//						return defaultFamilyId + "," + finalFamilyId;
	//					//					else
	//					//						return defaultFamilyId;
	//				}
	//
	//				if (!categories.contains(categoryFilter))
	//					continue;
	//
	//				//Business & Industrial:Healthcare, Lab & Dental:Other Healthcare, Lab & Dental
	//				String parentFamilyId = "0";
	//				String category = categories.substring(categories.indexOf(categoryFilter), categories.indexOf(categoryFilter) + categoryFilter.length());
	//
	//				for (int i = 0; i < config.getCategoryLevel(); i++) {
	//					String familyId = dao.selectFamilyId(category.replace(":", "-"), schema);
	//
	//					if (familyId == null) {
	//						//category = category.replace(":", "-");
	//						ProductFamily pf = new ProductFamily();
	//						pf.setOrgId(Integer.parseInt(companyId));
	//						pf.setFamilyName(category.replace(":", "-"));
	//						pf.setFamilySlug(category.replaceAll("[^a-zA-Z0-9]", "").replace(" ", "-") + "/");
	//						pf.setPageTitle(category);
	//						pf.setCategory("store");
	//						pf.setParentId(parentFamilyId);
	//						familyId = dao.insertFamily(pf, schema);
	//					}
	//
	//					finalFamilyId = familyId;
	//					parentFamilyId = familyId;
	//					//categories = categories.resubstring(category.indexOf(":") + 1, categories.length());
	//					//categories = categories.replace(category, "");//(category.indexOf(":") + 1, categories.length());
	//					categories = categories.replace(category, "");//(category.indexOf(":") + 1, categories.length());
	//
	//					if (categories.startsWith(":"))
	//						categories = categories.replaceFirst(":", "");
	//
	//					if (categories.indexOf(":") > 0)
	//						category = categories.substring(0, categories.indexOf(":"));
	//					else
	//						category = categories;
	//
	//					if (StringUtils.isEmpty(category))
	//						break;
	//
	//					//					System.err.println(categories);
	//					//					System.err.println(category);
	//				}
	//
	//			}
	//
	//			return defaultFamilyId + "," + finalFamilyId;
	//		}
	//
	//		return null;
	//	}

}
