package com.can.cluster.handling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.interfaces.ISynsetFinder;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

@Component("SemanticSimilarity")
public class SemanticSimilarity implements ICalculateSimilarity {

	private static final Logger LOGGER = Logger.getLogger(SemanticSimilarity.class);
	@Autowired
	IPOSTagger tagger;
	
	@Autowired
	ISynsetFinder synsetFinder;
	
	@Override
	public double[][] calculateSimilarity(Document document) {
		createPosTags(document);
		List<Sentence> sentences = document.getSentenceList();
		HashMap<String, Integer> hypernym;
		for (Sentence sentence : sentences) {
			hypernym=new HashMap<String, Integer>();
			List<Word> words = sentence.getWords();
			for(int i=0;i<words.size();i++){
				if(sentence.getPosTags().get(i).startsWith("VB")|| sentence.getPosTags().get(i).startsWith("NN") ){
					List<String> hypernymList = synsetFinder.findHypernym(words.get(i).getWord(), sentence.getPosTags().get(i));
					if(hypernymList!=null && hypernymList.size()>0){
						for (String currHypernym : hypernymList) {
							if(hypernym.containsKey(currHypernym)){
								Integer value = hypernym.get(currHypernym);
								hypernym.put(currHypernym, value+1);		
							}else{
								hypernym.put(currHypernym, 1);
							}
						}
					}
				}
			}
			LOGGER.debug(hypernym);
			sentence.setHypernym(hypernym);
		}
		
		
		return null;
	}

	private void createPosTags(Document document) {
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			List<String> wordAsStringList = sentence.getWordsAsStringList();
			String[] stringArray = wordAsStringList.toArray(new String[wordAsStringList.size()]);
			String[] posTags = tagger.getPOSTags(stringArray);
			sentence.setPosTags(Arrays.asList(posTags));
		}
		
	}

}
