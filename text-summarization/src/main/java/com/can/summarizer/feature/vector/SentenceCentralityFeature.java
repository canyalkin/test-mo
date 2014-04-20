package com.can.summarizer.feature.vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

@Component
public class SentenceCentralityFeature implements ITextFeature {
	private static final Logger LOGGER = Logger.getLogger(NameEntityFeature.class);
	
	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		if(doc.getStructuralProperties()==null){
			doc.createStructuralProperties();
		}
		HashMap<String, Integer> freqTable = doc.getStructuralProperties().getFreqTable();
		int k=0;
		for(int f:freqTable.values()){
			k+=f;
		}
		
		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LOGGER.trace("sentence:"+sentence);
			Set<Word> wordSet=new HashSet<Word>(sentence.getWords());
			int m=0;
			for (Iterator<Word> iterator = wordSet.iterator(); iterator.hasNext();) {
				Word word = (Word) iterator.next();
				for(int i=0;i<sentenceList.size();i++){
					if(!sentence.equals(sentenceList.get(i))){
						Set<Word> curSentenceWordList =new HashSet<Word>(sentenceList.get(i).getWords());
						if(curSentenceWordList.contains(word)){
							m++;
						}
					}
				}
			}
			LOGGER.trace("m:"+m);
			LOGGER.trace("k:"+k);
			if(k==0){
				LOGGER.trace("k:"+k);
				sentence.getFeatureVector().add(0.0);
			}else{
				sentence.getFeatureVector().add(m/(double)k);
			}
		}
	}

}
