package com.digipro.ebay.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import com.digipro.ebay.service.Product;
import com.digipro.ebay.service.ProductFamily;

public class ProductDao {

	Connection con;

	public ProductDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;
	}

	/**
	 * For testing VPN connectivity
	 * @return
	 * @throws Exception
	 */
	public String selectTest() throws Exception {

		System.err.println("Getting connection");

		Connection con = getConnection();
		System.err.println("Got connection");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM digipay_super_admin.api ");

		System.err.println(sql.toString());
		PreparedStatement stmt = con.prepareStatement(sql.toString());

		//stmt.setDate(9, product.getCreated().getTime());

		System.err.println("Executed");
		ResultSet rs = stmt.executeQuery();
		if (rs.next())
			System.err.println(rs.getString(2));

		con.close();
		System.err.println("Db Closed");
		return null;
	}

	public String selectFamilyId(String familyName, String schema) throws Exception {
		getConnection();

		String sql = "select family_id from " + schema + ".ip_families where family_name = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, familyName);
		ResultSet rs = stmt.executeQuery();
		String familyId = null;
		if (rs.next())
			familyId = rs.getString(1);

		rs.close();

		return familyId;
	}

	public String insertFamily(ProductFamily family, String schema) throws Exception {
		getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(schema);
		sql.append(".ip_families( ");
		sql.append("family_name, ");
		sql.append("category_type,  ");
		sql.append("family_slug, ");
		sql.append("family_page_title, ");
		sql.append("org_id) ");
		sql.append(" VALUES(?,?,?,?,?) ");

		PreparedStatement stmt = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, family.getFamilyName());
		stmt.setString(2, family.getCategory());
		stmt.setString(3, family.getFamilySlug());
		stmt.setString(4, family.getPageTitle());
		stmt.setInt(5, family.getOrgId());

		stmt.execute();

		ResultSet rs = stmt.getGeneratedKeys();
		String familyId = null;
		if (rs.next())
			familyId = rs.getString(1);

		rs.close();

		return familyId;
	}

	public String insertProductData(Product product, String schema, boolean closeConnection) throws Exception {

		getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(schema);
		sql.append(".ip_product_data ");
		sql.append("(product_id, ");
		sql.append("location, ");
		sql.append("mpn, ");
		sql.append("minimum_qty, ");
		sql.append("sort_order, ");
		sql.append("status) ");
		sql.append("VALUES (?,?,?,?,?,?) ");

		PreparedStatement stmt = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, product.getProductId());
		stmt.setString(2, product.getLocId());
		stmt.setString(3, "0");
		stmt.setString(4, "1");
		stmt.setString(5, "0");
		stmt.setString(6, "1");

		stmt.execute();

		ResultSet rs = stmt.getGeneratedKeys();
		String productId = null;
		if (rs.next())
			productId = rs.getString(1);

		rs.close();
		if (closeConnection)
			con.close();

		return productId;
	}

	public String insertProduct(Product product, String schema, boolean closeConnection) throws Exception {

		getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(schema);
		sql.append(".ip_products ");
		sql.append("(family_id, ");
		sql.append("product_name, ");
		sql.append("product_description, ");
		sql.append("product_slug, ");
		sql.append("product_qty, ");
		sql.append("product_price, ");
		sql.append("locID, ");
		sql.append("product_image, ");
		sql.append("product_image_alt, ");
		sql.append("product_sku,");
		sql.append("cost,");
		sql.append("tax_rate_id, purchase_price)");
		//sql.append("created_at) ");
		sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		PreparedStatement stmt = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, product.getProductFamilyId());
		stmt.setString(2, product.getTitle());
		stmt.setString(3, product.getDescription());
		stmt.setString(4, product.getSlug());
		stmt.setInt(5, product.getQuantity());
		stmt.setString(6, product.getPrice());
		stmt.setString(7, product.getLocId());
		stmt.setString(8, product.getPrimaryImage());
		stmt.setString(9, product.getPrimaryImageAlt());
		stmt.setString(10, "");
		stmt.setFloat(11, 0);
		stmt.setInt(12, 0);
		stmt.setInt(13, 0);
		stmt.execute();

		ResultSet rs = stmt.getGeneratedKeys();
		String productId = null;
		if (rs.next())
			productId = rs.getString(1);

		rs.close();
		if (closeConnection)
			con.close();

		return productId;
	}

	public void updateProduct(Product product, String schema, boolean closeConnection) throws Exception {
		getConnection();

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(schema);
		sql.append(".ip_products SET ");
		sql.append("product_name = ?, ");
		sql.append("product_description = ?, ");
		sql.append("product_slug = ?, ");
		sql.append("product_qty = ?, ");
		sql.append("product_price = ?, ");
		sql.append("locID = ?, ");
		sql.append("product_image = ?, ");
		sql.append("product_image_alt = ?, ");
		sql.append("status = ? ");
		sql.append(" WHERE product_id = ? ");

		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setString(1, product.getTitle());
		stmt.setString(2, product.getDescription());
		stmt.setString(3, product.getSlug());
		stmt.setInt(4, product.getQuantity());
		stmt.setString(5, product.getPrice());
		stmt.setString(6, product.getLocId());
		stmt.setString(7, product.getPrimaryImage());
		stmt.setString(8, product.getPrimaryImageAlt());
		stmt.setInt(9, product.getStatus());
		stmt.setString(10, product.getProductId());

		stmt.execute();
		if (closeConnection)
			con.close();

	}

	private Connection getConnection() {
		try {
			if (con != null)
				return con;

			AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();

			GetParameterRequest pr = new GetParameterRequest();
			pr.withName("liquidweb-mysql").setWithDecryption(true);
			GetParameterResult result = client.getParameter(pr);
			Parameter param = result.getParameter();

			System.err.println("Getting connection");
			con = DriverManager.getConnection(param.getValue());
			System.err.println("Got connection");
			return con;
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Couldn't get the db connection string from SSM");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
