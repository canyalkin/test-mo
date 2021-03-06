package com.can.document.reader;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.can.document.handler.module.DocumentReader;
import com.can.document.handler.module.StopWordHandler;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Document;
import com.can.word.utils.PropertyHandler;

@Component
@Scope("prototype")
public class BulkDocumentReader {
	private static final Logger LOGGER = Logger.getLogger(BulkDocumentReader.class);
	
	@Autowired
	StopWordHandler stopWordHandler;
	
	@Autowired
	IWordStemmer wordStemmer;
	
	@Autowired
	private ApplicationContext context;
	HashMap <String,Document> documentMap=null;
	
	
	@Autowired
	PropertyHandler propertyHandler;
	
	public Map<String,Document> doBulkRead(String path,boolean isRef){
		documentMap=new HashMap<String,Document>(1000);
		File file=new File(path);
		LOGGER.info("BulkRead started...");
		if(file.isDirectory()){
			LOGGER.info(path+" is a directory.");
			File[] filesInFolder = file.listFiles();
			DocumentReader documentReader=(DocumentReader)context.getBean(DocumentReader.class);
			for (File curFile : filesInFolder) {
				if(curFile.isFile()){
					
					documentReader.setFile(curFile);
					Document document=documentReader.createDocument(isRef);
					LOGGER.info(curFile.getName()+":"+document.getSentenceList().size());
					if(isRef){
						if(propertyHandler.isStemming()){
							document=wordStemmer.doStemming(document);
						}
						if(propertyHandler.isStopWordElimination()){
							document=stopWordHandler.doStopWordElimination(document);
						}
					}
					LOGGER.info(curFile.getName()+":"+document.getSentenceList().size());
					documentMap.put(curFile.getName(), document);
				}
			}
		}else{
			LOGGER.error("please supply directory and file structure");
		}
		LOGGER.info("BulkRead has finished...");
		return documentMap;
		
	}


	/**
	 * @return the documentMap
	 */
	public Map<String, Document> getDocumentMap() {
		return documentMap;
	}	


}
