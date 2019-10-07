package com.digipro.ebay.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.digipro.ebay.dao.ProductDao;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ProductService {

	static Map<String, String> familyIdMap = new HashMap<String, String>();

	public Product getBuildProductFromItem(ItemType item, String companyId, String defaultFamilyId, String schema, ProductDao dao, Set<String> categoryFilter) throws Exception {

		String categoryName = null;
		if (item.getPrimaryCategory() != null)
			categoryName = item.getPrimaryCategory().getCategoryName();

		Product product = new Product();
		product.setProductFamilyId(getFamilyId(categoryName, defaultFamilyId, schema, dao, companyId, categoryFilter));

		if (product.getProductFamilyId() == null) //Category excluded
			return null;

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

	public Product getProductFromItemXML(String xml, String companyId, String defaultFamilyId, String schema, ProductDao dao, Set<String> categoryFilter) throws Exception {
		XmlMapper xmlMapper = new XmlMapper();
		xml = xml.substring(xml.indexOf("<soapenv:Body>") + 14, xml.length());
		xml = xml.substring(0, xml.indexOf("</soapenv:Body>"));

		JsonNode jsonNode = xmlMapper.readTree(xml.getBytes());
		JsonNode item = jsonNode.get("Item");

		String categoryName = item.get("PrimaryCategory").get("CategoryName").textValue();

		String strRegEx = "<[^>]*>";
		Product product = new Product();
		product.setEbayItemId(item.get("ItemID").textValue());
		product.setProductFamilyId(getFamilyId(categoryName, defaultFamilyId, schema, dao, companyId, categoryFilter));
		product.setTitle(item.get("Title").textValue().replaceAll("[^a-zA-Z0-9]", ""));
		product.setDescription(item.get("Description").textValue().replaceAll(strRegEx, ""));
		product.setSlug(product.getTitle().replace(" ", "-"));
		product.setPrice(item.get("SellingStatus").get("CurrentPrice").get("").textValue());
		product.setQuantity(item.get("Quantity").intValue());
		product.setLocId(companyId);
		product.setPrimaryImageAlt(product.getTitle());
		product.setCreated(Calendar.getInstance());

		if (item.get("PictureDetails").get("PictureURL") != null)
			product.setPrimaryImage(item.get("PictureDetails").get("PictureURL").textValue());

		if (item.get("Quantity").asInt() > 0)
			product.setStatus(1);
		else
			product.setStatus(0);

		return product;

	}

	/**
	 * Returns null if there is not category or if there is a categoryFilter and it doesn't match. Calling code then ignores this product
	 * 
	 */
	private String getFamilyId(String categoryName, String defaultFamilyId, String schema, ProductDao dao, String companyId, Set<String> categoryFilter) throws Exception {

		if (categoryName != null) {

			if (categoryName.indexOf(":") > 0) {
				categoryName = categoryName.substring(categoryName.indexOf(":") + 1, categoryName.length());
				if (categoryName.indexOf(":") > 0)
					categoryName = categoryName.substring(0, categoryName.indexOf(":"));

			} else
				return null;

			if (categoryFilter != null) {
				if (!categoryFilter.contains(categoryName))
					return null; //Ingore this product
			}

			if (familyIdMap.containsKey(categoryName))
				return defaultFamilyId + "," + familyIdMap.get(categoryName);

			String familyId = dao.selectFamilyId(categoryName, schema);
			if (familyId != null) {
				familyIdMap.put(categoryName, familyId);
				return defaultFamilyId + "," + familyId;
			}

			ProductFamily pf = new ProductFamily();
			pf.setOrgId(Integer.parseInt(companyId));
			pf.setFamilyName(categoryName);
			pf.setFamilySlug(categoryName.replaceAll("[^a-zA-Z0-9]", "").replace(" ", "-") + "/");
			pf.setPageTitle(categoryName);
			pf.setCategory("store");

			familyId = dao.insertFamily(pf, schema);
			familyIdMap.put(categoryName, familyId);

			return defaultFamilyId + "," + familyId;
		}

		return null;
	}

}
