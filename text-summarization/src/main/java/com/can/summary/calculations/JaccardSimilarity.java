package com.can.summary.calculations;

import java.util.List;

import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;



public class JaccardSimilarity {
	
	private JaccardSimilarity() {
	}

	public static double calculate(Sentence sentence,Sentence sentence2){
		List<Word> words = sentence.getWords();
		List<Word> sentenceList2 = sentence2.getWords();
		double intersection=0;
		for (Word word : words) {
			if(sentenceList2.contains(word)){
				intersection++;
			}
		}
		double total=words.size()+sentenceList2.size();
		return intersection/(total-intersection);
		
		
	}
	
	
	
	



}
