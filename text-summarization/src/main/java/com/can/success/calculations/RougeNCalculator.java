package com.can.success.calculations;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.model.Sentence;

public class RougeNCalculator  {

	private static final Logger LOGGER = Logger.getLogger(RougeNCalculator.class);
	private List<Sentence> referenceSentences;//human sentences
	private List<Sentence> systemSentences;// computer sentences
	
	public RougeNCalculator(List<Sentence> referenceSentences,List<Sentence> systemSentences) {
		this.referenceSentences=referenceSentences;
		this.systemSentences=systemSentences;
	}
	public RougeNCalculator() {
		// TODO Auto-generated constructor stub
	}
	
	public Double calculateRougeN(int n)  {
		int countMatch=0;
		int totalRefNGram=0;
		
		HashMap<String,Integer> nGramOccForRef=calculateNGramOcc(referenceSentences);
		HashMap<String,Integer> nGramOccForSystem=calculateNGramOcc(systemSentences);
		for(Sentence curRefSentence: referenceSentences  ){
			List<String> refNgramList = curRefSentence.getNgramList();
			for (String curRefNgram : refNgramList) {
				int number=0;
				if( getNumberOfOcc(nGramOccForSystem,curRefNgram) <= getNumberOfOcc(nGramOccForRef,curRefNgram)){
					number=getNumberOfOcc(nGramOccForSystem,curRefNgram);
				}else{
					number=getNumberOfOcc(nGramOccForRef,curRefNgram);
				}
				countMatch+=number;
			}
		}
		for (Sentence refSentence: referenceSentences) {
			totalRefNGram+=refSentence.getNgramList().size();
		}
		
		return countMatch/(double)totalRefNGram;
	}

	private Integer getNumberOfOcc(HashMap<String, Integer> nGramOccMap,
			String curRefNgram) {
		int val=0;
		
		if(nGramOccMap.get(curRefNgram)!=null){
			val=nGramOccMap.get(curRefNgram);
		}
		
		return val;
	}
	private HashMap<String, Integer> calculateNGramOcc(
			List<Sentence> referenceSentences) {
		HashMap<String,Integer> occMap=new HashMap<String, Integer>();
		
		for (Sentence sentence : referenceSentences) {
			List<String> nGramList = sentence.getNgramList();
			for (String curNGram : nGramList) {
				if(occMap.containsKey(curNGram)){
					Integer value=occMap.get(curNGram);
					value++;
					occMap.put(curNGram, value);
				}else{
					occMap.put(curNGram, 1);
				}
			}
		}
		
		return occMap;
	}
	
	/**
	 * @return the referenceSentences
	 */
	public List<Sentence> getReferenceSentences() {
		return referenceSentences;
	}

	/**
	 * @param referenceSentences the referenceSentences to set
	 */
	public void setReferenceSentences(List<Sentence> referenceSentences) {
		this.referenceSentences = referenceSentences;
	}

	/**
	 * @return the systemSentences
	 */
	public List<Sentence> getSystemSentences() {
		return systemSentences;
	}

	/**
	 * @param systemSentences the systemSentences to set
	 */
	public void setSystemSentences(List<Sentence> systemSentences) {
		this.systemSentences = systemSentences;
	}


}
