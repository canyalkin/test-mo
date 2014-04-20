package com.can.summarizer.feature.vector;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

@Component
public class NumericFeature implements ITextFeature {

	private static final Logger LOGGER = Logger.getLogger(NumericFeature.class);
	
	@Override
	public void calculateTextFeatureForDocument(Document doc) {

		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LOGGER.trace("new sentence:"+sentence);
			List<Word> wordList = sentence.getWords();
			int numericNumber=0;
			for (Word word : wordList) {
				LOGGER.trace("word:"+word);
				if(isNumeric(word.getWord())){
					LOGGER.trace("numeric word:"+word);
					numericNumber++;
				}
			}

			if(wordList.size()==0){
				LOGGER.error("wordList.size()==0 - sentence:"+sentence);
				sentence.getFeatureVector().add(0.0);
			}else{
				LOGGER.trace("numericNumber:"+numericNumber);
				sentence.getFeatureVector().add(numericNumber/(double)wordList.size());
			}
			
		}
			
			

	}

	public static boolean isNumeric(String str)  
	{  
		try{  
			double d = Double.parseDouble(str);  
		}catch(NumberFormatException nfe){  
			return false;  
		}  

		return true;  
	}

}
