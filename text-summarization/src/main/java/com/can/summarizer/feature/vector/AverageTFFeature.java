package com.can.summarizer.feature.vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

@Component
public class AverageTFFeature implements ITextFeature {
	private static final Logger LOGGER = Logger.getLogger(AverageTFFeature.class);
	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		if(doc.getStructuralProperties()==null){
			doc.createStructuralProperties();
		}
		HashMap<String, Integer> freqTable = doc.getStructuralProperties().getFreqTable();
		Collection<Integer> freqValues = freqTable.values();
		Iterator<Integer> it = freqValues.iterator();
		int maxFreq=0;
		while(it.hasNext()){
			Integer val = it.next();
			if(val>maxFreq){
				maxFreq=val;
			}
		}
		LOGGER.trace("max freq:"+maxFreq);
		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LOGGER.trace("new sentence:"+sentence);
			HashSet<String> usedWords=new HashSet<String>();
			List<Word> wordList = sentence.getWords();
			double tf=0.0;
			for (Word word : wordList) {
				LOGGER.trace("word:"+word.getWord());
				if(!usedWords.contains(word.getWord())){
					LOGGER.trace("word not used before:"+word.getWord());
					int freq = freqTable.get(word.getWord());
					tf+=(freq/(double)maxFreq);
					usedWords.add(word.getWord());
				}
			}
			
			if(usedWords.size()==0){
				LOGGER.error("usedWords.size()==0 - sentence:"+sentence);
				sentence.getFeatureVector().add(0.0);
			}else{
				sentence.getFeatureVector().add(tf/(double)usedWords.size());
			}
		}
		
		

	}

}
