package com.can.summarizer.sentence.sim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.interfaces.ISentenceSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.SemanticSimilarity;

@Component("SentenceSemanticSim")
public class SemanticSim implements ISentenceSimilarity {

	@Autowired
	private IPOSTagger tagger;
	
	@Override
	public double calculate(int i, int j, Document document) {
		if(!document.isHasPosTag()){
			tagger.createPosTags(document);
		}
		Sentence sentence=document.getSentenceList().get(i);
		Sentence sentence2=document.getSentenceList().get(j);
		return SemanticSimilarity.calculate(sentence, sentence2);
	}

}
