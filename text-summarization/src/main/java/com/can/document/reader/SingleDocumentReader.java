package com.can.document.reader;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.can.document.handler.module.DocumentReader;
import com.can.summarizer.model.Document;

@Component
@Scope("prototype")
public class SingleDocumentReader {
	
	private String fileName;
	private Document doc=null;
	
	@Autowired
	private ApplicationContext context;
	
	public Document readDocument(String fileName, boolean isRef){
		this.fileName=fileName;
		File curFile=new File(fileName);
		if(curFile.isFile()){
			DocumentReader documentReader=(DocumentReader)context.getBean(DocumentReader.class);
			documentReader.setFile(curFile);
			doc=(documentReader.createDocument(isRef));
		}
		return doc;
		
	}

	public Document getDoc() {
		return doc;
	}

	public String getFileName() {
		return fileName;
	}

}
