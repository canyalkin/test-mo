package com.can.graph.similarities;

import com.can.summarizer.interfaces.GraphSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.SemanticSimilarity;


public class GraphSemanticSimilarity implements GraphSimilarity {

	@Override
	public double calculateSimilarity(int index1, int index2, Document document) {
		
		Sentence sentence1 = document.getSentenceList().get(index1);
		Sentence sentence2 = document.getSentenceList().get(index2);
		
		return SemanticSimilarity.calculate(sentence1, sentence2,document);
	}
	
	

}
