package com.digipro.ebay.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.digipro.ebay.model.BaseEntity;

//import io.stix.aws.Environment;
//import io.stix.clients.redrox.constants.CacheKey;
//import io.stix.model.BaseEntity;
//import io.stix.model.Cacheable;
//import io.stix.model.EntityClearSetsI;
//import io.stix.model.EntityIdI;
//import io.stix.model.marshalling.StixReflector;

//import net.sf.ehcache.CacheManager;
//import net.sf.ehcache.Ehcache;
//import net.sf.ehcache.Element;

public abstract class BaseDao<T> implements IDaoBase<T> {

//	private static final String LOCAL_DB_ENDPOINT = "http://localhost:8001";
//
//	private static final String LOCAL_DEV_KEY = "AKIAJEYKM7RUHDZB65UA";
//	private static final String DYNAMODB_LOCAL_KEY = "AKIAJPWNIR2OLH3TZ6QA";
//	private static final String DYNAMODB_LOCAL_SECRET = "LOCAL_DEV_SECRET"; // This mirrors the filename
//	private static final String LOCAL_REGION = "local";
//
	protected static final String FIELD_COMPANY_ID = "companyId";
	protected static final String IDX_COMPANY_ID = "companyId-index";
	protected static final String FIELD_USER_ID = "userId";
	protected static final String IDX_USER_ID = "userId-index";

	protected static AmazonDynamoDB  client;
	protected static DynamoDBMapper mapper;

	//private final Ehcache cache = CacheManager.getInstance().getCache(CacheKey.ENTITY);
	//private StixReflector<T> reflector;

	private Class<T> clazz;

	protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	//private static Logger logger = LoggerFactory.getLogger(BaseDao.class);

	public BaseDao(Class<T> clazz) {
		getMapper();

		this.clazz = clazz;
	}

	public static DynamoDBMapper getMapper() {
		if (mapper == null) {
			AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(DefaultAWSCredentialsProviderChain.getInstance().getCredentials());
			client = AmazonDynamoDBClientBuilder.standard().withCredentials(provider).build();					
			mapper = new DynamoDBMapper(client);
		}

		return mapper;
	}

	@Override
	public T save(T entity) {

		mapper.save(entity);

	
		return entity;
	}

	public List<T> batchSave(List<T> entityList) {

		if (entityList == null)
			return entityList;

		mapper.batchSave(entityList);

		return entityList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T load(String hashkey) {
		T loadedEntity = mapper.load(clazz, hashkey);

			
		return loadedEntity;
	}

	public List<T> batchLoad(Set<String> hashKeySet) {
		return batchLoad(new ArrayList<>(hashKeySet));
	}

	public List<T> batchLoad(List<String> hashKeyList) {
		try {
			return batchLoadImp(hashKeyList);
		} catch (Exception e) {
			throw new DynamoDBMappingException("Class instantiation issues with " + clazz.getName(), e);
		}
	}

	public void batchDelete(List<T> entityList) {
		if (entityList == null || entityList.size() == 0)
			return;

		T entity = entityList.get(0);
//		boolean cacheable = entity.getClass().isAnnotationPresent(Cacheable.class);
//		if (cacheable && entity instanceof BaseEntity) {
//			for (T cachedEntity : entityList)
//				cache.remove(cachedEntity.getClass().getName() + ((BaseEntity) entity).getId());
//		}

		mapper.batchDelete(entityList);
	}

	public Map<String, T> batchLoadMap(List<String> hashKeyList) {
		Map<String, T> entityMap = new HashMap<>();
		List<T> list = batchLoad(hashKeyList);

		for (T entity : list)
			entityMap.put(((BaseEntity) entity).getId(), entity);

		return entityMap;
	}

	public Map<String, T> batchLoadMap(Set<String> hashKeySet) {
		Map<String, T> entityMap = new HashMap<>();
		List<T> list = batchLoad(hashKeySet);

		for (T entity : list)
			entityMap.put(((BaseEntity) entity).getId(), entity);

		return entityMap;
	}

	public List<T> findByCompanyId(String companyId) {
		try {
			return findByHashInIndex(FIELD_COMPANY_ID, companyId, getTableName(), IDX_COMPANY_ID);
		} catch (Exception e) {
			throw new DynamoDBMappingException(String.format("Table %s does not have %s", getTableName(), IDX_COMPANY_ID), e);
		}
	}

	public List<T> findByUserId(String userId) {
		try {
			return findByHashInIndex(FIELD_USER_ID, userId, getTableName(), IDX_USER_ID);
		} catch (Exception e) {
			throw new DynamoDBMappingException(String.format("Table %s does not have %s", getTableName(), IDX_USER_ID), e);
		}
	}

	public List<T> findAll(String hashKey) {

		ScanRequest scanRequest = new ScanRequest().withTableName(this.clazz.getAnnotation(DynamoDBTable.class).tableName())
				.withAttributesToGet(hashKey);

		ScanResult result = client.scan(scanRequest);

		List<String> hashKeyList = new ArrayList<>();
		Iterator<Map<String, AttributeValue>> resultIter = result.getItems().iterator();
		while (resultIter.hasNext()) {
			Map<String, AttributeValue> attribs = resultIter.next();

			for (Entry<String, AttributeValue> attrib : attribs.entrySet()) {
				if (attrib.getKey().equals(hashKey))
					hashKeyList.add(attrib.getValue().getS());
			}
		}

		return batchLoad(hashKeyList);
	}

	public List<T> findAll() {

		ScanRequest scanRequest = new ScanRequest().withTableName(this.clazz.getAnnotation(DynamoDBTable.class).tableName())
				.withAttributesToGet("id");

		ScanResult result = client.scan(scanRequest);

		List<String> hashKeyList = new ArrayList<>();
		Iterator<Map<String, AttributeValue>> resultIter = result.getItems().iterator();
		while (resultIter.hasNext()) {
			Map<String, AttributeValue> attribs = resultIter.next();

			for (Entry<String, AttributeValue> attrib : attribs.entrySet()) {
				if (attrib.getKey().equals("id"))
					hashKeyList.add(attrib.getValue().getS());
			}
		}

		return batchLoad(hashKeyList);
	}

	public List<T> findByHashInIndex(String hashKey, String id, String tableName, String indexName) {
		QueryRequest queryRequest = new QueryRequest().withTableName(tableName).withIndexName(indexName).withScanIndexForward(true);

		HashMap<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put(hashKey,
				new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(id)));

		queryRequest.setKeyConditions(keyConditions);
		QueryResult result = client.query(queryRequest);

		List<String> hashKeyList = new ArrayList<>();
		Iterator<Map<String, AttributeValue>> resultIter = result.getItems().iterator();
		while (resultIter.hasNext()) {
			Map<String, AttributeValue> attribs = resultIter.next();

			for (Entry<String, AttributeValue> attrib : attribs.entrySet()) {
				if (attrib.getKey().equals("id"))
					hashKeyList.add(attrib.getValue().getS());
			}
		}

		return batchLoad(hashKeyList);
	}

	public List<T> findByHashAndRange(String tableName, String indexName, String hashName, String hashKey, String rangeName, String rangeKey) {

		QueryRequest queryRequest = new QueryRequest().withTableName(tableName).withIndexName(indexName);

		HashMap<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put(hashName,
				new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(hashKey)));
		keyConditions.put(rangeName,
				new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(rangeKey)));

		queryRequest.setKeyConditions(keyConditions);
		QueryResult result = client.query(queryRequest);

		List<String> list = new ArrayList<String>();
		for (Map<String, AttributeValue> item : result.getItems()) {
			if (item.containsKey("id")) {
				list.add(item.get("id").getS());
			}
		}

		return batchLoad(list);
	}

	public List<T> findByHashAndDateRange(String tableName, String indexName, String hashName, String rangeName, String hashKey, Calendar dateFrom,
			Calendar dateTo) {

		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		QueryRequest queryRequest = new QueryRequest().withTableName(tableName).withIndexName(indexName);

		HashMap<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put(hashName,
				new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(hashKey)));

		String dateFromStr = dateFormatter.format(dateFrom.getTime());

		if (dateTo != null) {

			String dateToStr = dateFormatter.format(dateTo.getTime());
			keyConditions.put(rangeName, new Condition().withComparisonOperator(ComparisonOperator.BETWEEN)
					.withAttributeValueList(new AttributeValue().withS(dateFromStr), new AttributeValue().withS(dateToStr)));

		} else
			keyConditions.put(rangeName,
					new Condition().withComparisonOperator(ComparisonOperator.GT).withAttributeValueList(new AttributeValue().withS(dateFromStr)));

		queryRequest.setKeyConditions(keyConditions);
		QueryResult result = client.query(queryRequest);

		List<String> list = new ArrayList<String>();
		for (Map<String, AttributeValue> item : result.getItems()) {
			if (item.containsKey("id")) {
				list.add(item.get("id").getS());
			}
		}

		return batchLoad(list);
	}

	/**
	 * NOTE: This is doing a query and should only be used where there is expected to be only one result.
	 *
	 * @param hashKey
	 * @param id
	 * @param tableName
	 * @param indexName
	 * @return
	 */
	public T loadByHashInIndex(String hashKey, String id, String tableName, String indexName) {
		QueryRequest queryRequest = new QueryRequest().withTableName(tableName).withIndexName(indexName).withScanIndexForward(true);

		HashMap<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put(hashKey,
				new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(id)));

		queryRequest.setKeyConditions(keyConditions);
		QueryResult result = client.query(queryRequest);

		Iterator<Map<String, AttributeValue>> resultIter = result.getItems().iterator();
		while (resultIter.hasNext()) {
			Map<String, AttributeValue> attribs = resultIter.next();

			for (Entry<String, AttributeValue> attrib : attribs.entrySet()) {
				if (attrib.getKey().equals("id"))
					return load(attrib.getValue().getS());
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	protected T loadEntityByHashAndRangeInIndex(Class clazz, String table, String index, String hashKey, String hashValue, String rangeKey,
			String rangeValue) {

//		boolean cacheable = clazz.isAnnotationPresent(Cacheable.class);
//
//		Object entity = getFromCache(cacheable, clazz, hashValue + rangeValue);
//		if (entity != null)
//			return (T) entity;
		Object entity = null;
		QueryRequest queryRequest = new QueryRequest().withTableName(table).withIndexName(index);

		HashMap<String, Condition> keyConditions = new HashMap<String, Condition>();

		keyConditions.put(hashKey,
				new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(hashValue)));

		keyConditions.put(rangeKey,
				new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(rangeValue)));

		queryRequest.setKeyConditions(keyConditions);
		QueryResult result = client.query(queryRequest);

		for (Map<String, AttributeValue> item : result.getItems()) {
			if (item.containsKey("id")) {
				entity = load(item.get("id").getS());
			}
		}

//		if (entity != null) {
//			putInCache(cacheable, entity, hashValue + rangeValue);
//		}

		return (T) entity;
	}

	/**
	 * While AWS SDK supports loading of multiple tables, this is an implementation for single table with T
	 * NOTE: DynamoDB throws an exception if the hashKeys aren't unique. Seems pretty stupid. Converts list to unique
	 * set and back again to maintain order and repeated item
	 *
	 * @param cacheable
	 * @param hashKeyList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<T> batchLoadImp(List<String> hashKeyList) {
		try {
			//DynamoDB throughs an exception if they're not unique
			Set<String> uniqueIdSet = new HashSet<>();
			uniqueIdSet.addAll(hashKeyList);

			List<Object> objectList = new ArrayList<>();
			List<T> returnList = new ArrayList<>();

			for (String hashKey : uniqueIdSet) {
				BaseEntity entity = (BaseEntity) clazz.newInstance();
				entity.setId(hashKey);
				objectList.add(entity);
			}

			Map<String, List<Object>> items = mapper.batchLoad(objectList);
			List<Object> oList = items.get(getTableName());
			Map<String, BaseEntity> entityMap = new HashMap<>();
			if (oList != null) {
				for (Object object : oList) {
					//T entity = (T) object;
					BaseEntity entity = (BaseEntity) object;
					entityMap.put(entity.getId(), entity);
				}
			}

			//Now use the original hashKeyList to maintain order and repeated items
			for (String hashKey : hashKeyList) {
				BaseEntity entity = entityMap.get(hashKey);

				if (entity != null) {
					T entityT = (T) entity;

					returnList.add(entityT);
//
//					if (cacheable)
//						putInCache(cacheable, entity, entity.getId());
				}
			}
			return returnList;
		} catch (Exception e) {
			throw new DynamoDBMappingException("Class issues with " + clazz.getName(), e);
		}
	}

	private String getTableName() {
		DynamoDBTable anno = clazz.getAnnotation(DynamoDBTable.class);
		return anno.tableName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T load(String hashkey, String rangeKey) {

//		boolean cacheable = clazz.isAnnotationPresent(Cacheable.class);
//
//		Object entity = getFromCache(cacheable, clazz, hashkey + rangeKey);
//		if (entity != null)
//			return (T) entity;

		T loadedEntity = mapper.load(clazz, hashkey, rangeKey);

//
//		putInCache(cacheable, loadedEntity, hashkey + rangeKey);

		return loadedEntity;
	}

	public void batchDeleteEntities(List<T> entityList) {
		try {
			if (entityList.size() == 0)
				return;

//			T entity = entityList.get(0);
//			boolean cacheable = entity.getClass().isAnnotationPresent(Cacheable.class);
//			if (cacheable)
//				cache.removeAll(entityList);

			mapper.batchDelete(entityList);
		} catch (Exception e) {
			throw new DynamoDBMappingException("Class issues with " + clazz.getName(), e);
		}
	}

	@Override
	public T delete(T entity) {
//		boolean cacheable = entity.getClass().isAnnotationPresent(Cacheable.class);
//		if (cacheable) {
//			if (entity instanceof BaseEntity)
//				cache.remove(entity.getClass().getName() + ((BaseEntity) entity).getId());
//		}

		mapper.delete(entity);
		return entity;
	}

	
//	protected <T> T getFromCache(boolean cacheable, Class<T> clazz, String hashkey) {
//		if (!cacheable)
//			return null;
//
//		Element element = cache.get(clazz.getName() + hashkey);
//		if (element != null)
//			return (T) element.getObjectValue();
//
//		return null;
//	}
//
//	protected void putInCache(boolean cacheable, Object entity, String hashkey) {
//		if (!cacheable || entity == null)
//			return;
//
//		Element element = new Element(entity.getClass().getName() + hashkey, entity);
//		cache.put(element);
//	}
//
//	public void removeFromCache(Object entity) {
//		if (entity == null || !entity.getClass().isAnnotationPresent(Cacheable.class))
//			return;
//
//		if (entity instanceof BaseEntity)
//			cache.remove(entity.getClass().getName() + ((BaseEntity) entity).getId());
//	}
//
//	public void removeFromCache(Class clazz, String key) {
//		cache.remove(clazz.getName() + key);
//	}

//	public void clearCache() {
//		cache.removeAll();
//	}

	protected Calendar getCalendar(String date) {
		if (date == null)
			return null;

		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateFormatter.parse(date));
			return cal;
		} catch (Exception e) {
			System.err.println("Calendar format could not be parsed");
		}

		return null;
	}
}
