package com.test.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoDatabase;
import com.test.service.Service;

@Configuration
public class JavaConfig {
	
	@Autowired
	private MongoDatabase database;
	
	@Bean
	public Service getService(){
		return new Service(database);
	}

}
