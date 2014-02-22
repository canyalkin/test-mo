package com.can.summary.calculations;

import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

public class NormalisedGoogleDistance {
	private static final Logger LOGGER = Logger.getLogger(NormalisedGoogleDistance.class);
	private NormalisedGoogleDistance(){
		
	}
	
	public static double ngd(Sentence s1,Sentence s2,Document document){
		double ngd=0.0;
		
		List<Word> words1 = s1.getWords();
		List<Word> words2 = s2.getWords();
		
		for(int i=0;i<words1.size();i++){
			for(int j=0;j<words2.size();j++){
				ngd+=ngd(words1.get(i), words2.get(j), document);
			}
		}
		
		
		return ngd;
	}
	
	public static double ngd(Word word1,Word word2,Document document){
		double val=0.0;
		if(word1.equals(word2)){
			return 1.0;
		}
		int fWord1=getNumberOfSentenceContaining(word1,document);
		int fWord2=getNumberOfSentenceContaining(word2,document);
		int wordsTogether=getNumberOfSentenceContainingTogether(word1, word2, document);
		int n=document.getSentenceList().size();
		
		if( !word1.equals(word2) && fWord1==fWord2 && fWord2==wordsTogether && fWord1>0){
			return 1.0;
		}
		
		double logFWord1=Math.log(fWord1);
		double logFWord2=Math.log(fWord2);
		double logTogether=Math.log(wordsTogether);
		double max= (logFWord1 > logFWord2) ? logFWord1 : logFWord2;
		double min= (logFWord1 < logFWord2) ? logFWord1 : logFWord2;
		
		double ngd=( max - logTogether ) / ( Math.log(n) - min );
		
		if(fWord1>0 && fWord2>0 && wordsTogether==0){
			ngd=1;
		}
		
		val=Math.exp(-1*ngd);
		
		if(Double.isNaN(ngd)){
			LOGGER.error("ngd NaN for:"+word1+", "+word2+" title:"+document.getTitle().getOriginalSentence());
		}
		if(Double.isNaN(val)){
			LOGGER.error("val NaN for:"+word1+", "+word2+" title:"+document.getTitle().getOriginalSentence());
		}
		
		return val;
	}

	private static int getNumberOfSentenceContaining(Word word, Document document) {
		int n=0;
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			List<String> words = sentence.getWordsAsStringList();
			if(words.contains(word.getWord())){
				n++;
			}
		}
		return n;
	}
	
	private static int getNumberOfSentenceContainingTogether(Word word,Word word2, Document document) {
		int n=0;
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			List<String> words = sentence.getWordsAsStringList();
			if(words.contains(word.getWord()) && words.contains(word2)){
				n++;
			}
		}
		return n;
	}
	
	

}
