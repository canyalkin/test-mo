package com.can.pos.tagger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.cmdline.postag.POSTaggerConverterTool;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

import org.apache.log4j.Logger;

import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.WordType;

public class OpenNLPPosTagger implements IPOSTagger {
	private static final Logger LOGGER = Logger.getLogger(OpenNLPPosTagger.class);
	private static OpenNLPPosTagger INSTANCE=null;
	private POSTaggerME tagger;
	private OpenNLPPosTagger(String file){
		InputStream data = null;
		try {
			data = new FileInputStream(new File(file));
			tagger = new POSTaggerME( new POSModel( data ) );
		} catch (InvalidFormatException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public static OpenNLPPosTagger getInstance(String file){
		
		if(INSTANCE==null){
			INSTANCE = new OpenNLPPosTagger(file);
		}
		return INSTANCE;
	}
	
	@Override
	public String[] getPOSTags(String[] wordsOfSentence) {
		String[] posTags = tagger.tag(wordsOfSentence);
		if(LOGGER.isTraceEnabled()){
			for (String pos : posTags) {
				LOGGER.trace(pos);
			}
		}
		return posTags;
	}

	@Override
	public void createPosTags(Document document) {
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			List<String> wordAsStringList = sentence.getWordsAsStringList();
			String[] stringArray = wordAsStringList.toArray(new String[wordAsStringList.size()]);
			String[] posTags = getPOSTags(stringArray);
			sentence.setPosTags(Arrays.asList(posTags));
		}
		
	}

}
