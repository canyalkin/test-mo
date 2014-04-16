package com.can.summarizer.feature.vector;

import java.util.List;

import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

@Component
public class SentenceRelativeLengthFeature implements ITextFeature {

	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		List<Sentence> sentenceList = doc.getSentenceList();
		int maxLength=0;
		for (Sentence sentence : sentenceList) {
			if(sentence.getWords().size()>maxLength){
				maxLength=sentence.getWords().size();
			}
		}
		
		for (Sentence sentence : sentenceList) {
			if(maxLength==0){
				//TODO log ekle
				sentence.getFeatureVector().add(0.0);
			}else{
				sentence.getFeatureVector().add(sentence.getWords().size()/(double)maxLength);
			}
		}

	}

}
