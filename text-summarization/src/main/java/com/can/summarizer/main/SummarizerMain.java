package com.can.summarizer.main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import com.can.document.handler.module.StopWordHandler;
import com.can.document.reader.BulkDocumentReader;
import com.can.document.reader.SingleDocumentReader;
import com.can.success.calculations.RougeNCalculator;
import com.can.summarizer.config.ApplicationConfiguration;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.NGramCalculator;
import com.can.summary.evaluator.BulkRougeNEvaluator;
import com.can.summary.exceptions.MissingFileException;
import com.can.summary.module.AbstractSummarizer;
import com.can.summary.module.GASummaryStrategyImpl;

public class SummarizerMain {

	private static final Logger LOGGER = Logger.getLogger(SummarizerMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		//DocumentReader documentReader=(DocumentReader)context.getBean(DocumentReader.class);
		Environment env=context.getBean(Environment.class);
		StopWordHandler stopWordHandler = (StopWordHandler)context.getBean(StopWordHandler.class);
		IWordStemmer wordStemmer=context.getBean(IWordStemmer.class);
		/***
		 * Single file read
		 * log level
		 * off:2.94 sm
		 * info:2.94 sn
		 * debug:10.061 sn
		 * 
		 */
	/*	long t1=System.currentTimeMillis();
		SingleDocumentReader singleDocumentReader=context.getBean(SingleDocumentReader.class);
		Document singleDoc = singleDocumentReader.readDocument(env.getProperty("file"));
		AbstractSummarizer summarizer=(AbstractSummarizer)context.getBean("GaStrategyBean");
		Document summarizedDocument=summarizer.doSummary(singleDoc);
		long t2=System.currentTimeMillis();
		System.out.println(summarizedDocument);
		System.out.println((t2-t1)/1000.0+" seconds...");
		
		
		t1=System.currentTimeMillis();
		SingleDocumentReader sum=context.getBean(SingleDocumentReader.class);
		Document sumDoc = singleDocumentReader.readDocument(env.getProperty("sum"));	
		sumDoc=stopWordHandler.doStopWordElimination(sumDoc);
		sumDoc=wordStemmer.doStemming(sumDoc);
		t2=System.currentTimeMillis();
		System.out.println((t2-t1)/1000.0+" seconds...");
		
		createNGramForDocument(summarizedDocument, RougeNType.wordBased, 1);
		createNGramForDocument(sumDoc, RougeNType.wordBased, 1);
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(sumDoc.getSentenceList(), summarizedDocument.getSentenceList());
		Double result = rougeNCalculator.calculateRougeN(1);
		System.out.println("Rouge -N result:"+result);
		
		print(summarizedDocument);
		*/
		
		
		
		/***
		 * Bulk Read
		 */
		long freeMemory1=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		BulkDocumentReader systemDocuments = context.getBean(BulkDocumentReader.class);
		long t1=System.currentTimeMillis();
		systemDocuments.doBulkRead(env.getProperty("docFolder"));
		long t2=System.currentTimeMillis();
		long freeMemory2=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LOGGER.info("bulk read of system documents takes:"+(t2-t1)/1000+" seconds");
		LOGGER.info("memory usage: "+(freeMemory2-freeMemory1)/(1024*1024.0)+" MB");
		
		/**
		 * Do bulk summarization, create system summaries and update system document map
		 */
		AbstractSummarizer summarizer=(AbstractSummarizer)context.getBean(AbstractSummarizer.class);
		Map<String, Document> systemDocMap = systemDocuments.getDocumentMap();
		Set<String> files = systemDocMap.keySet();
		for (String curFile : files) {
			LOGGER.info("*****cur file: "+curFile);
			Document document=systemDocMap.get(curFile);
			Document summary=summarizer.doSummary(document);
			systemDocMap.put(curFile, summary);
			
		}
		
		
		/***
		 * Bulk Read for reference
		 */
		BulkDocumentReader referenceDocuments = context.getBean(BulkDocumentReader.class);
		t1=System.currentTimeMillis();
		referenceDocuments.doBulkRead(env.getProperty("humanSummary"));
		t2=System.currentTimeMillis();
		LOGGER.info("bulk read of reference documents  takes:"+(t2-t1)/1000+" seconds");
		
		/**
		 * Bulk evaluation
		 */
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
		
		
		//AbstractSummarizer summarizer=(AbstractSummarizer)context.getBean(AbstractSummarizer.class);
		//Document summarizedDocument=summarizer.doSummary(document);
		//System.out.println(summarizedDocument);
		
	}
	private static void print(Document sumDoc) {
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

}
