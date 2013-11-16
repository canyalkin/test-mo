package com.can.document.handler.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;


public class StopWordHandler {
	private static StopWordHandler INSTANCE=null;
	private static final Logger LOGGER = Logger.getLogger(StopWordHandler.class);
	private HashSet<String> stopWrods=new HashSet<String>(100);
	@Autowired
	private Environment env;
	
	private StopWordHandler(File stopWordFile) {
		//File stopWordFile= new File(env.getProperty("stopWordsFile"));
		BufferedReader bufferedReader = null;
		LOGGER.info("StopWords postProcessor is starting...");
		try {
			bufferedReader=new BufferedReader(new FileReader(stopWordFile));
			String stopWordsInLine=bufferedReader.readLine();
			String[] stopWrodsAsArray=stopWordsInLine.split(",");
			for (String string : stopWrodsAsArray) {
				//string=string.replaceAll("[^\\w]","");
				LOGGER.debug("StopWord:"+string);
				stopWrods.add(string);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("FileNotFoundException"+e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IOException"+e.getMessage());
		}finally{
			if(bufferedReader!=null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("IOException-finally"+e.getMessage());
				}
			}
		}
	}

	public synchronized static StopWordHandler getInstance(File stopWordFile){
		if(INSTANCE==null){
			INSTANCE=new StopWordHandler(stopWordFile);
		}
		return INSTANCE;
	}
	
	
	public synchronized Document doStopWordElimination(Document aDocument){
		
		List<Sentence> sentenceList = aDocument.getSentenceList();
		if(env.getProperty("title").equals("true")){
			LOGGER.info("-------------The Title-----------");
			deleteStopWordsForTheGivenSentence(aDocument.getTitle());
			LOGGER.info("-------------The Title ENDS!!!-----------");
		}
		for (Sentence sentence : sentenceList) {
			deleteStopWordsForTheGivenSentence(sentence);
		}
		
		return aDocument;
	}

	private void deleteStopWordsForTheGivenSentence(Sentence sentence) {
		List<Word> wordList = sentence.getWords();	
		Iterator<Word> wordListIterator = wordList.iterator();
		String[] originalSentenceAsArray=sentence.getSentence().split(" ");
		StringBuffer newSentence=new StringBuffer();
		for (String string : originalSentenceAsArray) {
			if(!stopWrods.contains(string)){
				newSentence.append(string+" ");
			}
		}
		sentence.setSentence(newSentence.toString().trim());
		LOGGER.debug("--sentence: "+sentence.getOriginalSentence()+"--");
		while(wordListIterator.hasNext()){
			Word currentWord = wordListIterator.next();
			if(stopWrods.contains(currentWord.getWord())){
				LOGGER.debug("StopWord detected:"+currentWord.getWord());
				wordListIterator.remove();
			}
		}
		LOGGER.debug("-----------------------------------------------------------");
	}


}
