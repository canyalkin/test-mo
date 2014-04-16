package com.can.summarizer.feature.vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

public class AverageTFFeature implements ITextFeature {

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
		
		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			HashSet<String> usedWords=new HashSet<String>();
			List<Word> wordList = sentence.getWords();
			double tf=0.0;
			
			for (Word word : wordList) {
				if(!usedWords.contains(word.getWord())){
					int freq = freqTable.get(word.getWord());
					tf+=(freq/(double)maxFreq);
					usedWords.add(word.getWord());
				}
			}
			
			if(usedWords.size()==0){
				//TODO log ekle
				sentence.getFeatureVector().add(0.0);
			}else{
				sentence.getFeatureVector().add(tf/(double)usedWords.size());
			}
		}
		
		

	}

}
