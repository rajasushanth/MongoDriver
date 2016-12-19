package com.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.test.constants.Constants;
import com.test.model.PersonVO;
import com.test.util.ServiceUtil;

public class Service {

	private MongoDatabase database;

	private void loadProperties(ObjectId propRef) {
		Properties properties = new Properties();
		if (database != null) {
			try {
				MongoCollection<Document> collection = database.getCollection(Constants.PROPERTIES_COLLECTION);
				BasicDBObject query = new BasicDBObject(Constants._ID, propRef);
				List<Document> documents = collection.find(query).into(new ArrayList<Document>());
				for (Map.Entry<String, Object> entry : documents.get(0).entrySet()) {
					if (!entry.getKey().equals(Constants._ID)) {
						properties.setProperty(ServiceUtil.replaceColonSeparator(entry.getKey()), entry.getValue().toString());
						//System.out.println(ServiceUtil.replaceColonSeparator(entry.getKey()) + "=" + entry.getValue().toString());
					}
				}
			} catch (Exception e) {
				System.out.println("Exception in properties load:: " + e.getMessage());
			}
		}
		System.setProperties(properties);
	}

	public void getPropertiesfromService(String name, String version) {
		MongoCollection<Document> collection = database.getCollection(Constants.SERVICES_COLLECTION);
		BasicDBObject query = new BasicDBObject();
		ObjectId propRef = null;
		List<Document> documents = collection.find(query)
				.projection(Projections.fields(Projections.include(Constants.PROP_REF), Projections.excludeId()))
				.into(new ArrayList<Document>());
		propRef = (ObjectId) documents.get(0).get(Constants.PROP_REF);
		loadProperties(propRef);
		for(Map.Entry<Object, Object> entry: System.getProperties().entrySet()){
			System.out.println(entry.getKey().toString()+"="+entry.getValue().toString());
		}
	}
	
	/*public void getRules(){
		MongoCollection<Document> collection = database.getCollection("rules");
		List<Document> documents = collection.find().projection(Projections.excludeId()).into(new ArrayList<Document>());
		GsonBuilder gsonBuilder = new GsonBuilder();
	    Gson gson = gsonBuilder.create();
	    
	    PersonVO personVO = gson.fromJson(documents.get(0).toJson(), PersonVO.class);
	    System.out.println(personVO.toString());
	}*/

	public Service(MongoDatabase database) {
		super();
		this.database = database;
	}

	public Service() {
		super();
	}

}
