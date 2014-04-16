package com.can.summarizer.feature.vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.can.summarizer.model.Document;

@Component
public class FeatureVectorHandler {
	
	
	@Autowired
	ApplicationContext applicationContext;
	
	public void createFeatureVector(Document document){
		applicationContext.getBeansOfType(null);
	}

}
