package com.can.summarizer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Sentence {
	
	private final String originalSentence;
	private final int originalSentencesWordNumber;

	private String sentence;
	
	private String sentenceFromWords=null;
	private List<Word> words;
	private List<String> posTags=null;
	private List<String> wordListAsString=null;
	private List<String> ngramList=null;
	private HashMap<String, Integer> hypernym=null;
	private List<Double> featureVector=null;
	
	public Sentence(String originalSentence) {
		super();
		this.originalSentence = originalSentence;
		originalSentencesWordNumber=new StringTokenizer(this.originalSentence).countTokens();
	}
	/**
	 * @return the originalSentence
	 */
	public String getOriginalSentence() {
		return originalSentence;
	}
	/**
	 * @param originalSentence the originalSentence to set
	 */
	
	/**
	 * @return the sentence
	 */
	public String getSentence() {
		return sentence;
	}
	/**
	 * @param sentence the sentence to set
	 */
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	/**
	 * @return the words
	 */
	public List<Word> getWords() {
		return words;
	}
	/**
	 * @param words the words to set
	 */
	public void setWords(List<Word> words) {
		this.words = words;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sentence [unique word number"+ getUniqueWordNumber()+" originalSentence=" + originalSentence + "] ";
	}
	public List<String> getWordsAsStringList() {
		wordListAsString=new LinkedList<String>();
		for(int i=0;i<words.size();i++){
			wordListAsString.add(words.get(i).getWord());
		}
		return wordListAsString;
	}
	
	public String getWordsAsString() {
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<words.size();i++){
			buffer.append(words.get(i).getWord());
		}
		sentenceFromWords=buffer.toString();
		return sentenceFromWords;
	}

	/**
	 * @return the ngramList
	 */
	public List<String> getNgramList() {
		return ngramList;
	}
	/**
	 * @param ngramList the ngramList to set
	 */
	public void setNgramList(List<String> ngramList) {
		this.ngramList = ngramList;
	}
	
	/**
	 * @return the originalSentencesWordNumber
	 */
	public int getOriginalSentencesWordNumber() {
		return originalSentencesWordNumber;
	}
	
	public int getUniqueWordNumber(){
		int count=0;
		if(words==null)
			return 0;
		HashMap<String, Integer> wordSet=new HashMap<String, Integer>();
		for(int i=0;i<words.size();i++){
			String key=words.get(i).getWord();
			if(!wordSet.containsKey(key)){
				count++;
				wordSet.put(key, 1);
			}
		}
		wordSet=null;
		return count;
	}
	public List<String> getPosTags() {
		return posTags;
	}
	public void setPosTags(List<String> posTags) {
		this.posTags = posTags;
	}
	public void setHypernym(HashMap<String, Integer> hypernym) {
		this.hypernym=hypernym;
	}
	
	public HashMap<String, Integer> getHypernym() {
		return this.hypernym;
	}
	
	public void clear(){
		wordListAsString=null;
		if(posTags!=null){
			posTags=null;
		}
		if(featureVector!=null){
			featureVector=null;
		}
	}
	
	/**
	 * @return the featureVector
	 */
	public List<Double> getFeatureVector() {
		if(featureVector==null){
			featureVector=new ArrayList<Double>(25);
		}
		return featureVector;
	}

	/**
	 * @param featureVector the featureVector to set
	 */
	public void setFeatureVector(List<Double> featureVector) {
		this.featureVector = featureVector;
	}
	

}
