package com.can.word.utils;

import java.util.List;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

public class SummaryUtils {
	
	public static int calculateOriginalSentenceWordNumber(Document document){
		int count = 0;
		
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			count+=sentence.getOriginalSentencesWordNumber();
		}
		return count;
		
	}
	
	public static int calculateProcessedSentenceWordNumber(Document document){
		int count = 0;
		
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			count+=sentence.getWords().size();
		}
		return count;
		
	}

}
