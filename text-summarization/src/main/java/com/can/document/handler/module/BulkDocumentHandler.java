package com.can.document.handler.module;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.can.document.reader.BulkDocumentReader;
import com.can.summarizer.interfaces.IStopWord;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.NGramCalculator;
import com.can.summary.evaluator.BulkRougeNEvaluator;
import com.can.summary.exceptions.MissingFileException;
import com.can.summary.module.AbstractSummarizer;

@Component
public class BulkDocumentHandler {
	
	private static final Logger LOGGER = Logger.getLogger(BulkDocumentHandler.class);
	
	@Autowired 
	IStopWord stopWordHandler;
	
	@Autowired
	IWordStemmer wordStemmer;
	
	@Autowired 
	Environment environment;
	
	public static BulkDocumentReader doBulkReferenceRead(
			ApplicationContext context, Environment env) {
		long t1;
		long t2;
		BulkDocumentReader referenceDocuments = context.getBean(BulkDocumentReader.class);
		t1=System.currentTimeMillis();
		referenceDocuments.doBulkRead(env.getProperty("humanSummary"),true);
		t2=System.currentTimeMillis();
		LOGGER.info("bulk read of reference documents  takes:"+(t2-t1)/1000+" seconds");
		return referenceDocuments;
	}
	public static void doBulkSummarization(ApplicationContext context,
			BulkDocumentReader systemDocuments) {
		AbstractSummarizer summarizer=(AbstractSummarizer)context.getBean(AbstractSummarizer.class);
		Map<String, Document> systemDocMap = systemDocuments.getDocumentMap();
		Set<String> files = systemDocMap.keySet();
		for (String curFile : files) {
			LOGGER.info("*****cur file: "+curFile);
			Document document=systemDocMap.get(curFile);
			Document summary=summarizer.doSummary(document);
			systemDocMap.put(curFile, summary);
			
		}
	}
	public static BulkDocumentReader doBulkRead(ApplicationContext context,
			Environment env) {
		long freeMemory1=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		BulkDocumentReader systemDocuments = context.getBean(BulkDocumentReader.class);
		long t1=System.currentTimeMillis();
		systemDocuments.doBulkRead(env.getProperty("docFolder"),false);
		long t2=System.currentTimeMillis();
		long freeMemory2=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LOGGER.info("bulk read of system documents takes:"+(t2-t1)/1000+" seconds");
		LOGGER.info("memory usage: "+(freeMemory2-freeMemory1)/(1024*1024.0)+" MB");
		return systemDocuments;
	}
	public static void print(Document sumDoc) {
		List<Sentence> sList = sumDoc.getSentenceList();
		for (Sentence sentence : sList) {
			System.out.println(sentence.getOriginalSentence());
		}
		
	}
	public static void createNGramForDocument(Document document,RougeNType rougeNType,int n) {
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LinkedList<String> nGramList = NGramCalculator.findNGram(n, sentence, rougeNType);
			sentence.setNgramList(nGramList);
		}
	}
	public static void doBulkEvaluation(Environment env,
			BulkDocumentReader systemDocuments,
			BulkDocumentReader referenceDocuments) {
		BulkRougeNEvaluator bulkRougeNEvaluator=new BulkRougeNEvaluator(
				systemDocuments, referenceDocuments, Integer.parseInt(env.getProperty("nGramNumber")), RougeNType.getFromValue(env.getProperty("nGramType")));
		
		try {
			Map<String, Double> results = bulkRougeNEvaluator.calculateRougeN();
			Set<String> evaluatedFiles = results.keySet();
			for (String string : evaluatedFiles) {
				System.out.println(string+":"+results.get(string));
			}
			
		} catch (MissingFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e){
			
		}
	}
}
