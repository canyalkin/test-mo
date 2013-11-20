package com.can.summarizer.main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import com.can.document.handler.module.BulkDocumentHandler;
import com.can.document.handler.module.SingleDocumentHandler;
import com.can.document.reader.SingleDocumentReader;
import com.can.success.calculations.RougeNCalculator;
import com.can.summarizer.config.ApplicationConfiguration;
import com.can.summarizer.interfaces.IStopWord;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summary.calculations.NGramCalculator;
import com.can.summary.module.AbstractSummarizer;

public class SummarizerMain {

	private static final Logger LOGGER = Logger.getLogger(SummarizerMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		//DocumentReader documentReader=(DocumentReader)context.getBean(DocumentReader.class);
		Environment env=context.getBean(Environment.class);

		/***
		 * Single file read
		 * log level
		 * off:2.94 sm
		 * info:2.94 sn
		 * debug:10.061 sn
		 * 
		 */
		SingleDocumentHandler singleDocumentHandler=context.getBean(SingleDocumentHandler.class);
		singleDocumentHandler.readDocument(env.getProperty("file"));
		Document sysSum=singleDocumentHandler.summarize();
		

		Document refDocument=singleDocumentHandler.readRefDocument(env.getProperty("sum"));
		Double result=singleDocumentHandler.calculateRougeN(sysSum,refDocument,RougeNType.wordBased,1);

		
		
		System.out.println("Rouge -N result:"+result);
		System.out.println(sysSum);
		
		
	/*	
		
		*//***
		 * Bulk Read
		 *//*
		BulkDocumentReader systemDocuments = BulkDocumentHandler.doBulkRead(context, env);
		
		*//**
		 * Do bulk summarization, create system summaries and update system document map
		 *//*
		BulkDocumentHandler.doBulkSummarization(context, systemDocuments);
		
		*//***
		 * Bulk Read for reference
		 *//*
		BulkDocumentReader referenceDocuments = BulkDocumentHandler.doBulkReferenceRead(context,
				env);
		*//**
		 * Bulk evaluation
		 *//*
		BulkDocumentHandler.doBulkEvaluation(env, systemDocuments, referenceDocuments);
		*/
		
	}
	

}
