package com.digipro.ebay.tools;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import com.digipro.ebay.dao.ProductDao;
import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.digipro.ebay.service.CategoryConfig;
import com.digipro.ebay.service.EbayToDpmService;
import com.digipro.ebay.service.Product;
import com.digipro.ebay.service.ProductService;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.github.kevinsawicki.http.HttpRequest;

import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.dao.CompanyAppDao;
import io.digicore.lambda.model.CompanyApp;

public class ExportCategories {

	ProductDao dao;
	static Properties props;

	private static final String DEFAULT_FAMILY = "134";
	private static final String COMPANY_ID = "5447";
	private static final String SCHEMA = "5447_jaybemed2";
	private static final String APP_ID = "d21ee17b-c1cb-46fe-9ebb-182e27bd7075";

	public ExportCategories() {
		props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("dev.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		ExportCategories impProd = new ExportCategories();
		impProd.importProducts();
	}

	public void importProducts() {
		CompanyAppDao companyAppDao = new CompanyAppDao();
		CompanyApp companyApp = companyAppDao.load("5447", "d21ee17b-c1cb-46fe-9ebb-182e27bd7075");
		EbayToDpmService service = new EbayToDpmService(props);
		CategoryConfig config = service.getCategoryFilter(companyApp);

		try {
			List<String> lines = new ArrayList<String>();
			//lines.add("Start " + Calendar.getInstance().getTime());
			lines.add("Title\tCategory");

			Calendar from = Calendar.getInstance();
			Calendar to = Calendar.getInstance();
			from.add(Calendar.DAY_OF_YEAR, -119);
			exportForDateRange(lines, config, from, to);

			from.add(Calendar.DAY_OF_YEAR, -119);
			to.add(Calendar.DAY_OF_YEAR, -119);
			exportForDateRange(lines, config, from, to);

			from.add(Calendar.DAY_OF_YEAR, -119);
			to.add(Calendar.DAY_OF_YEAR, -119);
			exportForDateRange(lines, config, from, to);

			from.add(Calendar.DAY_OF_YEAR, -119);
			to.add(Calendar.DAY_OF_YEAR, -119);
			exportForDateRange(lines, config, from, to);

			from.add(Calendar.DAY_OF_YEAR, -119);
			to.add(Calendar.DAY_OF_YEAR, -119);
			exportForDateRange(lines, config, from, to);

			from.add(Calendar.DAY_OF_YEAR, -119);
			to.add(Calendar.DAY_OF_YEAR, -119);
			exportForDateRange(lines, config, from, to);

			from.add(Calendar.DAY_OF_YEAR, -119);
			to.add(Calendar.DAY_OF_YEAR, -119);
			exportForDateRange(lines, config, from, to);
			lines.add("End " + Calendar.getInstance().getTime());
			FileUtils.writeLines(new File("/tmp/eBayCategories.csv"), lines);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void exportForDateRange(List<String> lines, CategoryConfig config, Calendar from, Calendar to) throws Exception {
		GetSellerListCall api = new GetSellerListCall(getApiContext());

		api.setStartTimeFilter(new TimeFilter(from, to));

		DetailLevelCodeType[] types = { DetailLevelCodeType.RETURN_ALL };
		api.setDetailLevel(types);

		ItemType[] items = api.getEntireSellerList();

		System.err.println(items.length);

		for (ItemType item : items) {
			System.err.println(item.getTitle() + "\t" + item.getPrimaryCategory().getCategoryName());
			lines.add(item.getTitle() + "\t" + item.getPrimaryCategory().getCategoryName());
		}

		System.err.println("Finished Date Range " + items.length);
	}

	private static ApiContext getApiContext() {
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();

		//Needs to be an oAuth token
		//Dev Sandbox
		//		cred.seteBayToken(
		//				"AgAAAA**AQAAAA**aAAAAA**q4qCXQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4aiCZaGpQ2dj6x9nY+seQ**rAsFAA**AAMAAA**CkiDYkRh/RQiblT/Fd35ri0rNC3EL7SzRQ5Tt0YorwOG+aM7xbmI13zWv/BIHVPJwuK6eXbqaZPSMs5v3VP+2cV+hTriumtrRqvnmddqRboN9eiYMOKvFUSrg3nWEr70g6x5cJ4wZtEw//se3muMB5Ses20eWvCEuaA+zULDfx0WdY4xQWOO7IGtYCRLvdYo1KQmYHeWoHvWewsd1RDYQGfID5AnSHgrr0cV1seTQLSyFSbDAzspfNxXgnhw5z7F4IbfzEF08MRYZ1ZEBUPmlt6uszAtoW+1mktMqhxGUN8HE/M6Ev1IXiR4eSNDJgFBa7E720vL2adcDGVuDHRpeFpfq7V0NRMWB5zIYcQLvzwFmYYlVI+loZRuZnD4Tg6nRZLpgl84jKcLcx4SN5ma+mfzsQencnZgMI93GXLliQqR9G6kl5xUVrvl4bu5sN6br/ZBikgdApstIXm8sWvwiSy0CBKYWWMAED2Dw9nhTxkjjcUjk78kdD+jkP/1CrjoDil5WNXs7fo4GV57VQjTeg0dRQRHTBA86tciv7PiYekfxlW2OWsypaRtWr0akkoiLBgGaKhpLg0sUDnu4obRYU0jkyA1aczfx/97Z1grpW/hUOWjgaxa6MR+gN23i7yAXfhKj0OwEUfXdkWABihBGjKfnvGvFBICVhD6mOU50uD2zvyNZZ9nzfiz8PUYqSm03cTL0F0rG5DdOEyQkzyJfrNyQ33S/Ez24JGfMlfVYtHK/06j7167OmvxDUE+lJXT");
		//		apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");

		//Prod jay.brody@yahoo.com
		cred.seteBayToken(
				"AgAAAA**AQAAAA**aAAAAA**4JmLXQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6ADmYWoAZaFpg+dj6x9nY+seQ**dBAGAA**AAMAAA**hp4PJaWG9U2Nyl5UqMrDLmBY6JfNNEsgMPjDjnNWoaF9bYQRdQRIyict4dWy++Z4zpgJ3sqXzxn1368/nboRcVIojtaf7Ot7vrWflnSInNvMN5vY5ar72f6TIibzVm6dpK0gPi3gNpwgqCoTt00QPlJ2IIDJAkXjAZMXizrq6IDaioPlygkUqHkVEfGH9ORblS2YjFrB4vk4diOYbsKz1uZ6s/fBfbnqb9y5VYvBYUVGv+IRkM7+rZdP+F+7B5JlGeqsdxD8BGKbdImkHwYLz2CYs4b00/GxuzIYT1hoczmJJl6AS/OnXp3a+cqL4E0lvsPNwKuRxtSRncRWxF94qj3JDCrDs8Om0oADb2Imlb0d8xcjryWKzbhkVHEVWNRmYiNOo1X9alsFz4jGfhAK3gOzEDVV3KK9PWYdr6h+/6IzzWqUf8e3qMr4vptKQ/24DIBw/otehcciI11m19MvpADqGdSK9z5Sc+xGyNCVy0SI4lgWV5yYPQYHvmBjQFOJTInGklPfsYRRN8fahqzHX6cFQlxzwkiqLYa30xrJmfW1BJhGH+mw1GHJlNRPEJ2V7+lqfXMlirg1MUC60YfxNztkhh1OEvXs6S/mmOCl5xdfDC7KgvKGZ9mhsS8TaqPUMNXUsSptOJQVQuRRm0mYRry4dwtWpYFeSeApCz3kD9bHodINhJjOCPbgu9ejc5YmPLkH7Io1eTG/Tlt0DcCcaNtILXipdojeWCkLuyCa+fEzV1kNucZVJarv+to8jzD5");
		apiContext.setApiServerUrl("https://api.ebay.com/wsapi");

		return apiContext;
	}

	private String getApiKey() {
		try {
			AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();

			GetParameterRequest pr = new GetParameterRequest();
			pr.withName("digicore-api-key").setWithDecryption(true);
			GetParameterResult result = client.getParameter(pr);
			Parameter param = result.getParameter();

			return param.getValue();
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Couldn't get the API key from SSM");
		}
	}

}
