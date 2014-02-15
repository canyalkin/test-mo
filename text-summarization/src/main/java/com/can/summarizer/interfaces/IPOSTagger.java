package com.can.summarizer.interfaces;

import com.can.summarizer.model.Document;


public interface IPOSTagger {
	
	String[] getPOSTags(String [] wordsOfSentence);
	void createPosTags(Document document);

}
