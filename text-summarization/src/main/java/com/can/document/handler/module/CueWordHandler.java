package com.can.document.handler.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.can.summarizer.interfaces.ICueWord;


public class CueWordHandler implements ICueWord{
	
	private static final Logger LOGGER = Logger.getLogger(CueWordHandler.class);
	private static CueWordHandler INSTANCE=null;
	private HashSet<String> cueWords;
	
	private CueWordHandler(File cueFile) {
		cueWords=new HashSet<String>(20);
		BufferedReader bufferedReader = null;
		LOGGER.info("StopWords postProcessor is starting...");
		try {
			bufferedReader=new BufferedReader(new FileReader(cueFile));
			String cueWordsInLine=bufferedReader.readLine();
			String[] cueWordsAsArray=cueWordsInLine.split(",");
			for (String string : cueWordsAsArray) {
				//string=string.replaceAll("[^\\w]","");
				LOGGER.trace("cueWord:"+string);
				cueWords.add(string);
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

	@Override
	public boolean isItCueWord(String word) {
		
		if(cueWords.contains(word)){
			return true;
		}

		return false;
	}

	public static CueWordHandler getInstance(File cueWordsFile) {
		if(INSTANCE==null){
			INSTANCE=new CueWordHandler(cueWordsFile);
		}
		return INSTANCE;
	}

}
