package com.java.example.tanzu.hungryman.test;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.java.example.tanzu.hungryman.feign.AvailabilityClient;
import com.java.example.tanzu.hungryman.feign.SearchClient;
import com.java.example.tanzu.hungryman.springconfig.HungrymanTestConfiguration;

import lombok.extern.slf4j.Slf4j;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {HungrymanTestConfiguration.class}, properties = {"spring.cloud.config.enabled:true" })
@Slf4j
public abstract class SpringBaseTest 
{
	@Autowired
	protected SearchClient searchClient;
	
	@Autowired
	protected AvailabilityClient availClient;
	
	@AfterEach
	protected void cleanUp()
	{
		
		log.info("Cleaning up searches.");
		
		searchClient.getAllSearches()
		.flatMap(search -> 
		{
			return searchClient.deleteSearch(search.getId());
		})
		.delayElements(Duration.ofMillis(10))
		.then().block();
		
		
		log.info("Cleaning up completed.");
	}
}
