package com.can.summary.calculations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

public final class FrequencyCalculator {

	private static final Logger LOGGER = Logger.getLogger(FrequencyCalculator.class);
	
	public static HashMap<String,List<Double>> calculateTermFreq(HashMap<String,Integer> freqTable,Document aDocument){
		
		HashMap<String,List<Double>> wordSentenceHash=new HashMap<String, List<Double>>();
		Iterator<String> indexTermsIterator = freqTable.keySet().iterator();
		
		List<Double> maxIndexTermFrequencies=calculateMaxIndexTermFrequencies(aDocument,freqTable);
		LOGGER.debug("maxIndexTermFrequencies:"+maxIndexTermFrequencies);
		double maxFreq=0.0;
		while(indexTermsIterator.hasNext()){
			List<Double> freqListForAnIndexTerm=new LinkedList<Double>();
			String indexTerm = indexTermsIterator.next();
			List<Sentence> sentencesList = aDocument.getSentenceList();
			int sentenceIndex=0;
			for (Sentence sentence : sentencesList) {
				double freq=calculateIndexTermForSentence(indexTerm,sentence);
				maxFreq=maxIndexTermFrequencies.get(sentenceIndex);
				if(maxFreq!=0.0){
					freqListForAnIndexTerm.add(freq/maxFreq);
				}else{
					freqListForAnIndexTerm.add(freq);
				}
				sentenceIndex++;
			}
			wordSentenceHash.put(indexTerm, freqListForAnIndexTerm);
		}
	
		return wordSentenceHash;
		
	}

	private static List<Double> calculateMaxIndexTermFrequencies(
			Document aDocument, HashMap<String, Integer> freqTable) {
		List<Sentence> sentenceList = aDocument.getSentenceList();
		List<Double> freqList=new LinkedList<Double>();
		for (Sentence sentence : sentenceList) {
			freqList.add(findMaxFreqValueFromAllIndexTermsInTheSentence(freqTable, sentence));
		}
		return freqList;
	}

	private static double findMaxFreqValueFromAllIndexTermsInTheSentence(
			HashMap<String, Integer> freqTable, Sentence sentence) {
		Set<String> indexTerms = freqTable.keySet();
		double max=0.0;
		for (String string : indexTerms) {
			double val=calculateIndexTermForSentence(string, sentence);
			if(val>max){
				max=val;
			}
		}
		return max;
	}

	private static double calculateIndexTermForSentence(String indexTerm,
			Sentence sentence) {
		List<Word> wordList = sentence.getWords();
		double number=0.0;
		for (Word word : wordList) {
			if(word.getWord().equals(indexTerm)){
				number++;
			}
		}
		return number/wordList.size();
	}

	public static HashMap<String, Double> calculateInverseSentenceFreqTable(HashMap<String,Integer> freqTable, Document aDocument){
		HashMap<String, Double> isf=new HashMap<String, Double>();
		Iterator<String> keySetIterator = freqTable.keySet().iterator();
		int N=aDocument.getSentenceList().size();
		int n=0;
		while(keySetIterator.hasNext()){
			String key = keySetIterator.next();
			List<Sentence> sentencesList = aDocument.getSentenceList();
			n=0;
			for (Sentence sentence : sentencesList) {
				if(sentence.getWordsAsStringList().contains(key)){
					n++;
				}
			}
			isf.put(key, Math.log10(N/(double)n));
		}
		
		return isf;
		
	}

	public static HashMap<String,Integer> createFrequencyTable(Document aDocument){
		HashMap<String,Integer> freqHashMap=new HashMap<String, Integer>();
		List<Sentence> listOfSentences = aDocument.getSentenceList();
		
		for (Sentence sentence : listOfSentences) {
			List<Word> listOfWords = sentence.getWords();
			for (Word word : listOfWords) {
				if(freqHashMap.containsKey(word.getWord())){
					Integer value = freqHashMap.get(word.getWord());
					value++;
					freqHashMap.put(word.getWord(), value);
				}else{
					freqHashMap.put(word.getWord(), 1);
				}
			}
		}
		return freqHashMap;
		
	}
}
