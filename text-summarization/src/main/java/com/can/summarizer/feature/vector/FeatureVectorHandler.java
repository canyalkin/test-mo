package com.can.summarizer.feature.vector;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

@Component
public class FeatureVectorHandler {
	private static final Logger LOGGER = Logger.getLogger(FeatureVectorHandler.class);
	
	@Autowired
	ApplicationContext applicationContext;
	
	public void createFeatureVector(Document document){
		Map<String, ITextFeature> featureMap = applicationContext.getBeansOfType(ITextFeature.class);
		Collection<ITextFeature> features = featureMap.values();
		LOGGER.info("features:"+features);
		for (Iterator iterator = features.iterator(); iterator.hasNext();) {
			ITextFeature iTextFeature = (ITextFeature) iterator.next();
			LOGGER.info(""+iTextFeature.getClass().getCanonicalName());
			iTextFeature.calculateTextFeatureForDocument(document);
		}
		document.setFeatureVectorCreated(true);
		
		if(LOGGER.isDebugEnabled()){
			List<Sentence> sentences = document.getSentenceList();
			for (Sentence sentence : sentences) {
				LOGGER.debug("feature vector:"+sentence.getFeatureVector());
			}
		}
		
		
	}

}
