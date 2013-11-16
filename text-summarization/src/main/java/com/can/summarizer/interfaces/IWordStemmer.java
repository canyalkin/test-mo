package com.can.summarizer.interfaces;

import com.can.summarizer.model.Document;

public interface IWordStemmer {
	
	public String stemTheWord(String word);
	public Document doStemming(Document document);

}
