package com.can.summarizer.sentence.sim;

import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ISentenceSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.CosineSimilarity;

@Component("SentenceCosSim")
public class CosSim implements ISentenceSimilarity {

	@Override
	public double calculate(int i, int j, Document document) {
		
		Sentence sentence=document.getSentenceList().get(i);
		Sentence sentence2=document.getSentenceList().get(j);
		return CosineSimilarity.calculate(sentence, sentence2);
	}

}
