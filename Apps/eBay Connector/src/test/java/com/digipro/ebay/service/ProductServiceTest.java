package com.digipro.ebay.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.util.IOUtils;

class ProductServiceTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSaveXmlToS3() {
		try {
			ProductService service = new ProductService();
			service.saveXmlToS3(IOUtils.toString(getClass().getClassLoader().getResourceAsStream("product-listed-ebay.xml")));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e);
		}
	}

}
