package com.can.document.handler.module;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.word.utils.PropertyHandler;

@Component("docFromFile")
@Scope("singleton")
public class DocumentReaderFromFile extends DocumentReader {
	private static final Logger LOGGER = Logger.getLogger(DocumentReaderFromFile.class);
	
	@Autowired
	PropertyHandler propertyHandler;
	
	public DocumentReaderFromFile() {
	}
	

	public Document createDocument(boolean isRef) {
		Document document=null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		LOGGER.debug("creating Doc.");
		try {
			fileReader=new FileReader(getFile());
			bufferedReader=new BufferedReader(fileReader);
			String line;
			List<Sentence> wholeDocument=new LinkedList<Sentence>();
			
			while( (line=bufferedReader.readLine())!=null ){
				extractWordsAndSentences(line, wholeDocument);
			}
			document=new Document(isRef);
			if(propertyHandler.isTitle() && !document.isRef()){
				document.setTitle(wholeDocument.get(0));
				wholeDocument.remove(0);
			}
			document.setSentenceList(wholeDocument);
		}catch (PatternSyntaxException e){
			LOGGER.error("PatternSyntaxException",e.initCause(e.getCause()));
		}
		catch (FileNotFoundException e) {
			LOGGER.error("File not found exception",e.initCause(e.getCause()));
		} catch (IOException e) {
			LOGGER.error("IOException",e.initCause(e.getCause()));
		}
		finally{
			if (bufferedReader!=null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("IOException-buffered reader-finally block",e.initCause(e.getCause()));
				}
			}
			if(fileReader!=null){
				try {
					fileReader.close();
				} catch (IOException e) {
					LOGGER.error("IOException-file reader-finally block",e.initCause(e.getCause()));
				}
			}
		}
		return document;
	}

	

	
	
	

}
