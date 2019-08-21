package com.digipro.ebay.service;

import java.util.Properties;

import com.digipro.ebay.ro.Event;
import com.digipro.ebay.ro.ebay.EbayResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class EbayToDpmService {

	Properties props;

	public EbayToDpmService(Properties props) {
		this.props = props;
	}

	public void processEbayProductChange(Event event) {

		try {
			XmlMapper xmlMapper = new XmlMapper();

			String xml = event.getPayload();
			xml = xml.substring(xml.indexOf("<soapenv:Body>") + 14, xml.length());
			xml = xml.substring(0, xml.indexOf("</soapenv:Body>"));

			JsonNode jsonNode = xmlMapper.readTree(xml.getBytes());
			//EbayResponse response = xmlMapper.readValue(xml, EbayResponse.class);
			System.err.print(GsonUtil.gson.toJson(jsonNode));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
