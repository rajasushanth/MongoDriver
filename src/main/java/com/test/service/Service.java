package com.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.test.constants.Constants;
import com.test.model.Properties;
import com.test.util.ServiceUtil;

public class Service {

	private MongoDatabase database;

	private Properties loadProperties(ObjectId propRef) {
		Properties properties = new Properties();
		if (database != null) {
			Map<String, String> props = new HashMap<String, String>();
			try {
				MongoCollection<Document> collection = database.getCollection(Constants.PROPERTIES_COLLECTION);
				BasicDBObject query = new BasicDBObject(Constants._ID, propRef);
				List<Document> documents = collection.find(query).into(new ArrayList<Document>());
				for (Map.Entry<String, Object> entry : documents.get(0).entrySet()) {
					if (!entry.getKey().equals(Constants._ID)) {
						props.put(ServiceUtil.replaceColonSeparator(entry.getKey()), entry.getValue().toString());
						System.out.println(
								ServiceUtil.replaceColonSeparator(entry.getKey()) + "=" + entry.getValue().toString());
					}
				}
			} catch (Exception e) {
				System.out.println("Exception in properties load:: " + e.getMessage());
			}
			properties.setProps(props);
		}
		return properties;
	}

	public void getPropertiesfromService(String name, String version) {
		MongoCollection<Document> collection = database.getCollection(Constants.SERVICES_COLLECTION);
		BasicDBObject query = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		query.put(Constants.NAME, name);
		query.put(Constants.VERSION, version);
		fields.put(Constants._ID , "0");
		fields.put(Constants.PROP_REF, "1");
		ObjectId propRef = null;
		List<Document> documents = collection.find(query).into(new ArrayList<Document>());
		propRef = (ObjectId) documents.get(0).get(Constants.PROP_REF);
		loadProperties(propRef);
	}

	public Service(MongoDatabase database) {
		super();
		this.database = database;
	}

	public Service() {
		super();
	}

}
