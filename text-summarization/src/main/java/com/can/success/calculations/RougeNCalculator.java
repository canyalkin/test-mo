package com.can.success.calculations;

import java.util.HashMap;
import java.util.List;

import com.can.summarizer.model.Sentence;

public class RougeNCalculator  {

	private List<Sentence> referenceSentences;
	private List<Sentence> systemSentences;
	
	public RougeNCalculator(List<Sentence> humanSentences,List<Sentence> systemSentences) {
		this.referenceSentences=humanSentences;
		this.systemSentences=systemSentences;
	}
	public RougeNCalculator() {
		// TODO Auto-generated constructor stub
	}
	
	public Double calculateRougeN(int n)  {
		int countMatch=0;
		int totalRefNGram=0;
		HashMap<String, Integer> occurenceList=new HashMap<String, Integer>();	
		for(Sentence candidateSentence: systemSentences  ){
			List<String> candidateNgramList = candidateSentence.getNgramList();
			for (String candidateNgram : candidateNgramList) {
				if(!occurenceList.containsKey(candidateNgram)){
					int number=getNumberOfNgramsOccuringInCandidateDoc(candidateNgram,referenceSentences);
					countMatch+=number;
					occurenceList.put(candidateNgram, number);
				}

				
			}
		}
		for (Sentence refSentence: referenceSentences) {
			totalRefNGram+=refSentence.getNgramList().size();
		}
		
		return countMatch/(double)totalRefNGram;
	}

	private int getNumberOfNgramsOccuringInCandidateDoc( String candidateNgram,
			List<Sentence> referenceSentences) {
		int cnt=0;
		for (Sentence sentence : referenceSentences) {
			List<String> nGrams = sentence.getNgramList();
			for (String string : nGrams) {
				if(candidateNgram.compareToIgnoreCase(string)==0){
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
