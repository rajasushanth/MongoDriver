package com.test.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.test.service.Service;

public class Main {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext("com.test.configuration");
		Service service = appContext.getBean(Service.class);
		System.out.println("Processing properties request");
		service.getPropertiesfromService("CNS", "16.08");
		appContext.close();
	}

}
