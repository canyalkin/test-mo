package com.can.document.handler.module;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.can.summarizer.interfaces.DocumentCreator;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;
import com.can.word.stemmer.WordNetStemmer;
import com.can.word.utils.RegexWord;

public abstract class DocumentReader implements DocumentCreator {
	private static final Logger LOGGER = Logger.getLogger(DocumentReaderFromFile.class);
	private File file;
		
	public DocumentReader() {
	}
	
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	protected void extractWordsAndSentences(String line, List<Sentence> wholeDocument) {
		LOGGER.debug("line:"+line);
		Word word;
		List<Word> wordList=new LinkedList<Word>(); 
		
		Sentence s=new Sentence(line);
		s.setOriginalSentence(new String(line));
		line=line.toLowerCase(Locale.ENGLISH);
		String[] words = line.split(" ");
		
		StringBuffer newLine=new StringBuffer();
		for (int i=0;i<words.length;i++) {
			String curWord=words[i];
			/*************New Regex Pattern Matcher***************/
			curWord = RegexWord.extractWordWithRespectToPattern(curWord);
			/******************************************************/
			words[i]=curWord;
			newLine.append(curWord+" ");
			if(curWord.equals("")){
				LOGGER.debug("***empty word");
				continue;
			}
			LOGGER.debug("words:"+curWord);
			word=new Word(curWord);
			wordList.add(word);
			LOGGER.debug("word added:>"+curWord+"<");
		}
		if(wordList.size()>0){
			s.setSentence(newLine.toString().trim());
			s.setWords(wordList);
			wholeDocument.add(s);
		}else{
			LOGGER.debug("***no words for sentence:"+line);
		}
	}

}
