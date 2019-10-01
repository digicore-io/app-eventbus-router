package com.digipro.ebay.ro;

public class AppEntity {
	private String companyId;
	private String entityId;
	private Data data = new Data();

	public class Data {
		String itemId; //EbayID set if doing DPM -> Ebay
		String productId; //DPM ID set if doing Ebay -> DPM

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
