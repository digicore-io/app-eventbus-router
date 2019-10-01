package com.digipro.ebay.service;

import java.util.HashMap;
import java.util.Map;

import com.digipro.ebay.dao.ProductDao;
import com.ebay.soap.eBLBaseComponents.ItemType;

public class ProductService {

	static Map<String, String> familyIdMap = new HashMap<String, String>();

	public Product getBuildProductFromItem(ItemType item, String companyId, String defaultFamilyId, String schema, ProductDao dao) throws Exception {
		Product product = new Product();
		product.setProductFamilyId(getFamilyId(item, defaultFamilyId, schema, dao, companyId));
		product.setTitle(item.getTitle());
		product.setLocId(companyId);
		String strRegEx = "<[^>]*>";
		if (item.getDescription() != null)
			product.setDescription(item.getDescription().replaceAll(strRegEx, ""));

		product.setEbayItemId(item.getItemID());
		product.setPrice(String.valueOf(item.getSellingStatus().getCurrentPrice().getValue()));
		product.setQuantity(String.valueOf(item.getQuantity()));
		product.setSlug(product.getTitle().replaceAll("[^a-zA-Z0-9]", ""));

		product.setPrimaryImageAlt(item.getTitle());
		if (item.getPictureDetails() != null && item.getPictureDetails().getPictureURLLength() > 0)
			product.setPrimaryImage(item.getPictureDetails().getPictureURL()[0]);

		return product;
	}

	private String getFamilyId(ItemType item, String defaultFamilyId, String schema, ProductDao dao, String companyId) throws Exception {

		if (item.getPrimaryCategory() != null) {
			System.err.println("Category:    " + item.getPrimaryCategory().getCategoryName());

			String categoryName = item.getPrimaryCategory().getCategoryName();
			if (categoryName.indexOf(":") > -1)
				categoryName = categoryName.substring(0, categoryName.indexOf(":"));

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

		return defaultFamilyId;
	}
}
