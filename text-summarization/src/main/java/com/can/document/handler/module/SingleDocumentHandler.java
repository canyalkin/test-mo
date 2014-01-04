package com.can.document.handler.module;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.can.document.reader.SingleDocumentReader;
import com.can.success.calculations.RougeNCalculator;
import com.can.summarizer.interfaces.IStopWord;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summary.calculations.NGramCalculator;
import com.can.summary.module.AbstractSummarizer;
import com.can.word.utils.PropertyHandler;
import com.can.word.utils.SummaryUtils;

@Component
public class SingleDocumentHandler {
	
	private static final Logger LOGGER = Logger.getLogger(SingleDocumentHandler.class);
	
	@Autowired
	private IStopWord stopWordHandler;
	
	@Autowired
	private IWordStemmer wordStemmer;
	
	@Autowired
	private SingleDocumentReader singleDocumentReader;
	
	@Autowired
	@Qualifier("GaStrategyBean")
	private AbstractSummarizer summarizer;
	
	@Autowired
	private PropertyHandler propertyHandler;
	private Document singleDoc;
	private Document refDoc;

	public void readDocument(String file) {
		singleDoc = singleDocumentReader.readDocument(file,false);
	}

	public Document summarize() {
		long t1=System.currentTimeMillis();
		Document summarizedDocument=summarizer.doSummary(singleDoc);
		long t2=System.currentTimeMillis();
		LOGGER.info((t2-t1)/1000.0+" seconds...");
		return summarizedDocument;
	}

	public Document readRefDocument(String file) {
		LOGGER.debug("reference document reading");
		refDoc = singleDocumentReader.readDocument(file,true);	
		if(propertyHandler.isStopWordElimination()){
			refDoc=stopWordHandler.doStopWordElimination(refDoc);
		}
		if(propertyHandler.isStemming()){
			refDoc=wordStemmer.doStemming(refDoc);
		}
		return refDoc;
	}

	public Double calculateRougeN(Document sysSum, Document refDocument,
			RougeNType rougeNType, int n) {
		
		NGramCalculator.createNGramForDocument(sysSum, rougeNType, n);
		NGramCalculator.createNGramForDocument(refDocument, rougeNType, n);
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(refDocument.getSentenceList(), sysSum.getSentenceList());
		
		return rougeNCalculator.calculateRougeN(n);
	}
	
	public int getOriginalDocumentWordNumber(){
		return SummaryUtils.calculateOriginalSentenceWordNumber(singleDoc);
	}
	
	
	

}
