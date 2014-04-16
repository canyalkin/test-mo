package com.can.summarizer.feature.vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

public class AverageTfIdfFeature implements ITextFeature {

	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		if(doc.getStructuralProperties()==null){
			doc.createStructuralProperties();
		}
		
		HashMap<String, Double> tfIdfTable = doc.getStructuralProperties().getTfIdf();
		
		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			HashSet<String> usedWords=new HashSet<String>();
			List<Word> wordList = sentence.getWords();
			double tfIdf=0.0;
			for (Word word : wordList) {
				if(!usedWords.contains(word.getWord())){
					double curTfIdf = tfIdfTable.get(word.getWord());
					tfIdf+=curTfIdf;
					usedWords.add(word.getWord());
				}
			}
			
			if(usedWords.size()==0){
				//TODO log ekle
				sentence.getFeatureVector().add(0.0);
			}else{
				sentence.getFeatureVector().add(tfIdf/(double)usedWords.size());
			}
		}

	}

}
