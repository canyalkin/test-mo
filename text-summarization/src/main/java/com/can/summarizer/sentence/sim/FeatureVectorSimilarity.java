package com.can.summarizer.sentence.sim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.feature.vector.FeatureVectorHandler;
import com.can.summarizer.interfaces.ISentenceSimilarity;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.CosineSimilarity;

@Component("FeatureVectorSimilarity")
public class FeatureVectorSimilarity implements ISentenceSimilarity {
	
	@Autowired
	private FeatureVectorHandler featureVectorHandler;

	@Override
	public double calculate(int i, int j, Document document) {
		if(!document.isFeatureVectorCreated()){
			featureVectorHandler.createFeatureVector(document);
		}
		return CosineSimilarity.calculate(document.getSentenceList().get(i).getFeatureVector(), document.getSentenceList().get(j).getFeatureVector());
	}

}
