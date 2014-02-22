package com.can.summarizer.interfaces;

import java.util.List;

import com.can.summarizer.model.Document;

public interface SentenceOrder {
	
	
	List<Integer> orderSentence(List<Integer> indexes, Document document);

}
