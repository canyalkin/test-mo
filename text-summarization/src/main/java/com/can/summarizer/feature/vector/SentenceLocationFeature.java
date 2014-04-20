package com.can.summarizer.feature.vector;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

@Component
public class SentenceLocationFeature implements ITextFeature{

	private static final Logger LOGGER = Logger.getLogger(SentenceLocationFeature.class);
	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		double score=1.0;
		
		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LOGGER.trace("new sentence:"+sentence);
			if(score<=0.0){
				LOGGER.trace("sentence score:"+score);
				sentence.getFeatureVector().add(0.0);
			}else{
				LOGGER.trace("sentence score:"+score);
				sentence.getFeatureVector().add(score);
				score=-0.2;
			}
		}
	}

}
