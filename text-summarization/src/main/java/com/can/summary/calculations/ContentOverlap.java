package com.can.summary.calculations;

import java.util.List;

import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;



public class ContentOverlap {
	
	private ContentOverlap() {
	}
	
	
	
	public static double calculate(Sentence sentence,Sentence sentence2){
		List<Word> words = sentence.getWords();
		List<Word> sentenceList2 = sentence2.getWords();
		double cnt=0;
		for (Word word : words) {
			if(sentenceList2.contains(word)){
				cnt++;
			}
		}
		
		cnt = cnt / (Math.log10(words.size()) + Math.log10(sentenceList2.size()));
		if(Double.isNaN(cnt)){
			return 0.0;
		}
		return cnt;
	}
	
	



}
