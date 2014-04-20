package com.can.summarizer.feature.vector;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

@Component
public class SentenceRelativeLengthFeature implements ITextFeature {
	private static final Logger LOGGER = Logger.getLogger(SentenceRelativeLengthFeature.class);
	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		List<Sentence> sentenceList = doc.getSentenceList();
		int maxLength=0;
		for (Sentence sentence : sentenceList) {
			if(sentence.getWords().size()>maxLength){
				maxLength=sentence.getWords().size();
			}
		}
		LOGGER.trace("maxLength:"+maxLength);
		
		for (Sentence sentence : sentenceList) {
			if(maxLength==0){
				LOGGER.error("maxLength==0");
				sentence.getFeatureVector().add(0.0);
			}else{
				LOGGER.trace("sentence.getWords().size()"+sentence.getWords().size());
				sentence.getFeatureVector().add(sentence.getWords().size()/(double)maxLength);
			}
		}

	}

}
