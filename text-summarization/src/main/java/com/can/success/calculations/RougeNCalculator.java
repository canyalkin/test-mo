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
		HashMap<String, Integer> occurenceList=new HashMap<String, Integer>();	
		for(Sentence curRefSentence: referenceSentences  ){
			List<String> refNgramList = curRefSentence.getNgramList();
			for (String curRefNgram : refNgramList) {
				//if(!occurenceList.containsKey(curRefNgram)){
					int number=getNumberOfNgramsOccuringInCandidateDoc(curRefNgram,systemSentences);
					countMatch+=number;
					occurenceList.put(curRefNgram, number);
				//}
			}
		}
		for (Sentence refSentence: referenceSentences) {
			totalRefNGram+=refSentence.getNgramList().size();
		}
		
		return countMatch/(double)totalRefNGram;
	}

	private int getNumberOfNgramsOccuringInCandidateDoc( String curRefNgram,
			List<Sentence> systemSentences) {
		int cnt=0;
		for (Sentence sentence : systemSentences) {
			List<String> nGrams = sentence.getNgramList();
			for (String string : nGrams) {
				if(curRefNgram.compareToIgnoreCase(string)==0){
					cnt++;
				}
			}
			
		}
		return cnt;
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
