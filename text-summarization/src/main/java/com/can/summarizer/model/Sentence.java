package com.can.summarizer.model;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Sentence {
	
	private final String originalSentence;
	private final int originalSentencesWordNumber;

	private String sentence;
	
	private String sentenceFromWords=null;
	private List<Word> words;
	private List<String> wordListAsString=null;
	private List<String> ngramList=null;
	
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
		return "Sentence [originalSentence=" + originalSentence + "]";
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

}
