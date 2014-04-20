package com.can.summarizer.feature.vector;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

@Component
public class NameEntityFeature implements ITextFeature {
	private static final Logger LOGGER = Logger.getLogger(NameEntityFeature.class);
	
	@Autowired
	private IPOSTagger tagger;
	
	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		if(!doc.isHasPosTag()){
			tagger.createPosTags(doc);
		}
		List<Sentence> sentenceList = doc.getSentenceList();
		int i=0;
		int cnt=0;
		for (Sentence sentence : sentenceList) {
			
			List<String> posTagList = sentence.getPosTags();
			cnt=Collections.frequency(posTagList, "NN");
			if(posTagList.size()!=0){
				sentence.getFeatureVector().add(cnt/(double)posTagList.size());
			}else{
				LOGGER.error("wordList.size()=0");
				sentence.getFeatureVector().add(0.0);
			}
			
		}

	}

}
