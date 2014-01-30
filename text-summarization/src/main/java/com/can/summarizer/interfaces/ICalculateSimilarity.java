package com.can.summarizer.interfaces;

import com.can.summarizer.model.Document;

public interface ICalculateSimilarity {

	double[][] calculateSimilarity(Document document);
	
}
