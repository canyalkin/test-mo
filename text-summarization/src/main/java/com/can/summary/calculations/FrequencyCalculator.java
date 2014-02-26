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

	private FrequencyCalculator() {
	}
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
				double freq=calculateIndexTermFreqForSentence(indexTerm,sentence);
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
			double val=calculateIndexTermFreqForSentence(string, sentence);
			if(val>max){
				max=val;
			}
		}
		return max;
	}

	private static double calculateIndexTermFreqForSentence(String indexTerm,
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
	
	public static HashMap<String,Double> createIdfTable(HashMap<String, Integer> frequencyTable){
		HashMap<String, Double> idfTable=new HashMap<String, Double>(frequencyTable.size());
		
		Set<String> uniqueTerms = frequencyTable.keySet();
		
		double totalWordNumber=calculateTotalWordNumber(frequencyTable);
		
		for (String term : uniqueTerms) {
			double idfValue=calculateIdf(term,frequencyTable,totalWordNumber);
			idfTable.put(term, idfValue);
		}
		return idfTable;
	}
	private static double calculateTotalWordNumber(
			HashMap<String, Integer> frequencyTable) {
		Set<String> uniqueTerms = frequencyTable.keySet();
		Double totalCount=0.0;
		for (String key : uniqueTerms) {
			totalCount+=frequencyTable.get(key);
		}
		return totalCount;
	}
	private static double calculateIdf(String term,
			HashMap<String, Integer> frequencyTable, double totalWordNumber) {
		
		Integer termCount = frequencyTable.get(term);
		double retVal=Math.log(totalWordNumber / (double)termCount);
		if(retVal==Double.NaN){
			LOGGER.error("for term:"+term+" NaN");
			retVal=0.0;
		}
		
		return retVal;
	}
	
	public static HashMap<String,Double> createTfIdfTable(HashMap<String,Integer> tf, HashMap<String,Double> idf){
		HashMap<String,Double> tfIdf=new HashMap<String, Double>(tf.size());
		if(tf.size()!=idf.size()){
			LOGGER.error("unexpected tf & idf size different. tf:"+tf.size()+" idf:"+idf.size());
			return tfIdf;
		}
		double totalWordNumber = calculateTotalWordNumber(tf);
		Set<String> keys = tf.keySet();
		for (String key: keys) {
			tfIdf.put(key, (tf.get(key)/totalWordNumber)*idf.get(key));
		}
		return tfIdf;
	}
	
	public static void addWordsToList(List<String> wordList,Sentence sentence){
		List<Word> words = sentence.getWords();
		for (Word word : words) {
			if(!wordList.contains(word.getWord())){
				wordList.add(word.getWord());
			}
		}
	}
	
	public static void addWordsToListWrtPos(List<String> wordList,Sentence sentence,String posTag){
		List<Word> words = sentence.getWords();
		int i=0;
		for (Word word : words) {
			if(!wordList.contains(word.getWord())){
				if(sentence.getPosTags().get(i).startsWith(posTag)){
					wordList.add(word.getWord());
				}
				
			}
			i++;
		}
	}
	
	public static HashMap<String, Integer> getWordsTogetherMap(HashMap<String, Integer>freqTable,Document document){
		HashMap<String, Integer> together=new HashMap<String, Integer>();
		
		Set<String> keys = freqTable.keySet();
		String keyArray[]=new String[keys.size()];
		keys.toArray(keyArray);
		for(int i=0; i<keyArray.length; i++){
			for(int j=i+1; j<keyArray.length;j++){
				together.put(keyArray[i]+keyArray[j], getNumberOfSentenceContainingTogether(keyArray[i], keyArray[j], document));
			}
			
		}
		return together;
	}
	
	private static int getNumberOfSentenceContainingTogether(String word,String word2, Document document) {
		int n=0;
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			List<String> words = sentence.getWordsAsStringList();
			if(words.contains(word) && words.contains(word2)){
				n++;
			}
		}
		return n;
	}
	
	public static HashMap<String, Integer> calculateNumberOfSentenceContains(
			HashMap<String, Integer> freqTable, Document document) {
		Set<String> keys = freqTable.keySet();
		HashMap<String, Integer> contains=new HashMap<String, Integer>(freqTable.size());
		for (String key : keys) {
			contains.put(key, getNumberOfSentenceContaining(key, document));
		}
		return contains;
	}
	
	private static int getNumberOfSentenceContaining(String word, Document document) {
		int n=0;
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			List<String> words = sentence.getWordsAsStringList();
			if(words.contains(word)){
				n++;
			}
		}
		return n;
	}
}
