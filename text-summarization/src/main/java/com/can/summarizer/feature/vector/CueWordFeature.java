package com.can.summarizer.feature.vector;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICueWord;
import com.can.summarizer.interfaces.ITextFeature;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;

@Component
public class CueWordFeature implements ITextFeature {
	private static final Logger LOGGER = Logger.getLogger(CueWordFeature.class);
	@Autowired
	private ICueWord cueWord;
	@Override
	public void calculateTextFeatureForDocument(Document doc) {
		List<Sentence> sentenceList = doc.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LOGGER.trace("new sentence:"+sentence);
			List<Word> wordList = sentence.getWords();
			int cueWordNumber=0;
			for (Word word : wordList) {
				LOGGER.trace("word:"+word);
				if(cueWord.isItCueWord(word.getWord())){
					LOGGER.trace("cue word:"+word);
					cueWordNumber++;
				}
			}
			if(wordList.size()==0){
				LOGGER.error("wordList.size()==0-sentence:"+sentence);
				sentence.getFeatureVector().add(0.0);
			}else{
				LOGGER.trace("cueWordNumber:"+cueWordNumber);
				sentence.getFeatureVector().add(cueWordNumber/(double)wordList.size());
			}
		}
	}

}
