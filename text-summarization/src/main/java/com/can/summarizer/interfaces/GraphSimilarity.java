package com.can.summarizer.interfaces;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

public interface GraphSimilarity {
	
	double calculateSimilarity(int index1,int index2, Document document);

}
