package com.can.summarizer.feature.vector;

import java.util.List;

import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

@Component
public class SentenceLocationFeature implements ITextFeature{

	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		double score=1.0;
		
		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			if(score==0.0){
				sentence.getFeatureVector().add(0.0);
			}else{
				sentence.getFeatureVector().add(score);
				score=-0.2;
			}
		}
	}

}
