package com.can.graph.similarities;

import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.GraphSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.NormalisedGoogleDistance;


public class NGDSimilarity implements GraphSimilarity {

	
	@Override
	public double calculateSimilarity(int index1, int index2, Document document) {
		return NormalisedGoogleDistance.ngd(document.getSentenceList().get(index1), document.getSentenceList().get(index2), document);
	}

}
