package com.can.summarizer.sentence.sim;

import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ISentenceSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

@Component("SentenceContentOverlap")
public class ContentOverlap implements ISentenceSimilarity {

	@Override
	public double calculate(int i, int j, Document document) {
		
		Sentence sentence=document.getSentenceList().get(i);
		Sentence sentence2=document.getSentenceList().get(j);
		return com.can.summary.calculations.ContentOverlap.calculate(sentence, sentence2);
	}

}
