package com.can.summarizer.interfaces;

import com.can.summarizer.model.Document;

public interface ISentenceSimilarity {
	
	double calculate(int i,int j,Document document);

}
