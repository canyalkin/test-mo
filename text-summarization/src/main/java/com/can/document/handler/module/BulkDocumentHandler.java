package com.can.document.handler.module;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.can.document.reader.BulkDocumentReader;
import com.can.summarizer.interfaces.IStopWord;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.evaluator.BulkRougeNEvaluator;
import com.can.summary.exceptions.MissingFileException;
import com.can.summary.module.AbstractSummarizer;
import com.can.word.utils.PropertyHandler;
import com.can.word.utils.SummaryUtils;

@Component
public class BulkDocumentHandler {
	
	private static final Logger LOGGER = Logger.getLogger(BulkDocumentHandler.class);
	
	@Autowired 
	private IStopWord stopWordHandler;
	
	@Autowired
	private IWordStemmer wordStemmer;
		
	@Autowired
	private PropertyHandler propertyHandler;
	
	@Autowired
	ApplicationContext context;
	
	public BulkDocumentReader doBulkReferenceRead() {
		long t1;
		long t2;
		BulkDocumentReader referenceDocuments = context.getBean(BulkDocumentReader.class);
		t1=System.currentTimeMillis();
		referenceDocuments.doBulkRead(propertyHandler.getRefDocumentFolder(),true);
		t2=System.currentTimeMillis();
		LOGGER.info("bulk read of reference documents  takes:"+(t2-t1)/1000+" seconds");
		return referenceDocuments;
	}
	public Map<String, Document> doBulkSummarization(BulkDocumentReader systemDocuments) {
		AbstractSummarizer summarizer=(AbstractSummarizer)context.getBean("GaStrategyBean");
		Map<String, Document> systemDocMap = systemDocuments.getDocumentMap();
		Map<String, Document> summaryMap=new HashMap<String, Document>(600);
		Set<String> files = systemDocMap.keySet();
		for (String curFile : files) {
			LOGGER.info("*****cur file: "+curFile);
			Document document=systemDocMap.get(curFile);
			Document summary=summarizer.doSummary(document);
			summaryMap.put(curFile, summary);
		}
		return summaryMap;
	}
	public BulkDocumentReader doBulkRead() {
		long freeMemory1=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		BulkDocumentReader systemDocuments = context.getBean(BulkDocumentReader.class);
		long t1=System.currentTimeMillis();
		systemDocuments.doBulkRead(propertyHandler.getDocumentFolder(),false);
		long t2=System.currentTimeMillis();
		long freeMemory2=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LOGGER.info("bulk read of system documents takes:"+(t2-t1)/1000+" seconds");
		LOGGER.info("memory usage: "+(freeMemory2-freeMemory1)/(1024*1024.0)+" MB");
		return systemDocuments;
	}
	public String doBulkEvaluation(Map<String, Document> orginalDocuments,Map<String, Document> summaryDocuments,
			Map<String, Document> referenceDocuments) {
		BulkRougeNEvaluator bulkRougeNEvaluator=new BulkRougeNEvaluator(
				summaryDocuments, referenceDocuments, propertyHandler.getRougeNNumber(), propertyHandler.getRougeNType());
		StringBuffer stringBuffer=new StringBuffer();
		double total=0.0;
		double average=0.0;
		try {
			Map<String, Double> results = bulkRougeNEvaluator.calculateRougeN();
			Set<String> evaluatedFiles = results.keySet();
			stringBuffer.append("file:rouge-n:# of words in original doc:# of words in refernce doc:# of words in summary doc"+"\n");
			DecimalFormat formatter = new DecimalFormat();
			formatter.setMaximumFractionDigits(5);
			DecimalFormatSymbols dfs = formatter.getDecimalFormatSymbols();
			dfs.setDecimalSeparator(',');
			formatter.setDecimalFormatSymbols(dfs);
			for (String string : evaluatedFiles) {
				
				stringBuffer.append(string+":"+formatter.format(results.get(string))+":"+SummaryUtils.calculateOriginalSentenceWordNumber((orginalDocuments.get(string)))+
						":"+SummaryUtils.calculateOriginalSentenceWordNumber(referenceDocuments.get(string)) +":"
						+SummaryUtils.calculateOriginalSentenceWordNumber(summaryDocuments.get(string))+"\n");
				total+=results.get(string);
			}
			
			average=total/evaluatedFiles.size();
			stringBuffer.append("Average:"+average+"\n");
			
		} catch (MissingFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e){
			
		}
		return stringBuffer.toString();
	}
	public int calculateWordCount(List<Sentence> sentenceList){
		int count=0;
		for (Sentence sentence : sentenceList) {
			count+=sentence.getWords().size();
		}
		return count;
	}
}
