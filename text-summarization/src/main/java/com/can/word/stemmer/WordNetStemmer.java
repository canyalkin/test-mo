package com.can.word.stemmer;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;
import com.can.word.utils.PropertyHandler;
import com.can.word.utils.RegexWord;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.morph.WordnetStemmer;

@Component
@Scope("singleton")
public class WordNetStemmer implements IWordStemmer,BeanPostProcessor {

	private static final Logger LOGGER = Logger.getLogger(WordNetStemmer.class);
	
	@Autowired
	PropertyHandler propertyHandler;
	
	private WordnetStemmer wordnetStemmer;
	
	public WordNetStemmer() {
		
	}
	
	public synchronized String stemTheWord(String word) {
		LOGGER.debug("Stem word:"+word);
		List<String> wordList = wordnetStemmer.findStems(word, null);
		for (String string : wordList) {
			LOGGER.debug("word stemmer: "+string);
		}
		LOGGER.debug("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		
		if(wordList.size()>0){
			return wordList.get(0);
		}
		return word;
	}
	
	public Document doStemming(Document document){
		LOGGER.info("Stemming Starts...");
		if(propertyHandler.isTitle() && !document.isRef()){
			LOGGER.info("-------------The Title-----------");
			stemTheGivenSentence(document.getTitle());
			LOGGER.info("-------------The Title ENDS!!!-----------");
		}
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			stemTheGivenSentence(sentence);
		}
		return document;
	}
	private void stemTheGivenSentence(Sentence sentence) {
		Iterator<Word> wordsIteratorOfSentence = sentence.getWords().iterator();
		LOGGER.debug("sentence: "+sentence.getSentence());
		while (wordsIteratorOfSentence.hasNext()) {
			Word word = (Word) wordsIteratorOfSentence.next();
			String stemmedWord = stemTheWord(word.getWord());
			stemmedWord=RegexWord.extractWordWithRespectToPattern(stemmedWord);
			word.setWord(stemmedWord);
		}
	}
	
	public static void main(String[] args) {
		WordNetStemmer netStemmer=new WordNetStemmer();
		netStemmer.stemTheWord("observation");
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		String wnhome = propertyHandler.getWordNetFolder();
		String path = wnhome;
		URL url;
		try {
			url = new URL ( "file" , null , path );
			// construct the dictionary object and open it
			IDictionary dict = new Dictionary ( url ) ;
			dict.open();	
			wordnetStemmer=new WordnetStemmer(dict);
		} catch (MalformedURLException e) {
			LOGGER.error("MalformedURLException");
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IOException in word stemmer");
			LOGGER.error(e.getMessage());
		}
		return bean;
	}
	

}
