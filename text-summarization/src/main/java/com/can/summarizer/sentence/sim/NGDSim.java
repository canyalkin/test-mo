package com.can.summarizer.sentence.sim;

import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ISentenceSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.NormalisedGoogleDistance;

@Component("SentenceNGD")
public class NGDSim implements ISentenceSimilarity {

	@Override
	public double calculate(int i, int j, Document document) {
		if(document.getStructuralProperties()==null){
			document.createStructuralProperties();
		}
		Sentence s1=document.getSentenceList().get(i);
		Sentence s2=document.getSentenceList().get(j);
		
		return NormalisedGoogleDistance.ngd(s1, s2, document);
	}

}
