package com.can.document.handler.module;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.can.document.reader.SingleDocumentReader;
import com.can.success.calculations.RougeNCalculator;
import com.can.summarizer.interfaces.IStopWord;
import com.can.summarizer.interfaces.IStrategyDirector;
import com.can.summarizer.interfaces.IVisitor;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.interfaces.SummaryStrategy;
import com.can.summarizer.interfaces.Visitable;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summary.calculations.NGramCalculator;
import com.can.summary.module.AbstractSummarizer;
import com.can.summary.module.GASummaryStrategyImpl;
import com.can.word.utils.PropertyHandler;
import com.can.word.utils.SummaryUtils;

@Component
public class SingleDocumentHandler implements Visitable {
	
	private static final Logger LOGGER = Logger.getLogger(SingleDocumentHandler.class);
	
	@Autowired
	private IStopWord stopWordHandler;
	
	@Autowired
	private IWordStemmer wordStemmer;
	
	@Autowired
	private SingleDocumentReader singleDocumentReader;
	
	//@Autowired
	//@Qualifier("GaStrategyBean")
	private AbstractSummarizer summarizer;
	
	@Autowired
	private IStrategyDirector strategyDirector;
	
	@Autowired
	private PropertyHandler propertyHandler;
	private Document singleDoc;
	private Document refDoc;
	
	private int refWordNumber,summarizedWordNumber;
	private double rougeNResult;
	private Document summarizedDocument;
	private double summarizationTime;

	public void readDocument(String file) {
		singleDoc = singleDocumentReader.readDocument(file,false);
	}

	public Document summarize() {
		
		long t1=System.currentTimeMillis();
		List<SummaryStrategy> strategyList = strategyDirector.getSummaryStrategies();
		SummaryStrategy summaryStrategy = null;
		for (int i = 0; i < strategyList.size(); i++) {
			summaryStrategy=strategyList.get(i);
			singleDoc=summaryStrategy.doSummary(singleDoc);
			
		}
		summarizer=(AbstractSummarizer) summaryStrategy;
		summarizedDocument=summarizer.finalizeSummaryWithPropertyWordNumber(singleDoc);
		long t2=System.currentTimeMillis();
		setSummarizationTime((t2-t1)/1000.0);
		LOGGER.info((t2-t1)/1000.0+" seconds...");
		setSummarizedWordNumber(SummaryUtils.calculateOriginalSentenceWordNumber(summarizedDocument));
		return summarizedDocument;
	}


	private void setSummarizationTime(double d) {
		summarizationTime=d;
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
		setRefWordNumber(SummaryUtils.calculateOriginalSentenceWordNumber(refDoc));
		return refDoc;
	}
	
	public double getLastFitnessValue(){
		double val=0.0;
		try{
			val=((GASummaryStrategyImpl)summarizer).getFitnessValue();
		}catch(ClassCastException castException){
			LOGGER.error(castException);
			val=0.0;
		}catch(Exception e){
			LOGGER.error(e);
			val=0.0;
		}
		return val;
		
		
	}

	public Double calculateRougeN(Document sysSum, Document refDocument,
			RougeNType rougeNType, int n) {
		
		NGramCalculator.createNGramForDocument(sysSum, rougeNType, n);
		NGramCalculator.createNGramForDocument(refDocument, rougeNType, n);
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator(refDocument.getSentenceList(), sysSum.getSentenceList());
		setRougeNResult(rougeNCalculator.calculateRougeN(n));
		return getRougeNResult();
	}
	
	public int getOriginalDocumentWordNumber(){
		return SummaryUtils.calculateOriginalSentenceWordNumber(singleDoc);
	}

	@Override
	public void accept(IVisitor visitor) {
		List<SummaryStrategy> strategyList = strategyDirector.getSummaryStrategies();
		for (SummaryStrategy summaryStrategy : strategyList) {
			((Visitable)summaryStrategy).accept(visitor);
		}
		visitor.visit(this);
	}

	public int getRefWordNumber() {
		return refWordNumber;
	}

	private void setRefWordNumber(int refWordNumber) {
		this.refWordNumber = refWordNumber;
	}

	public int getSummarizedWordNumber() {
		return summarizedWordNumber;
	}

	private void setSummarizedWordNumber(int summarizedWordNumber) {
		this.summarizedWordNumber = summarizedWordNumber;
	}

	public double getRougeNResult() {
		return rougeNResult;
	}

	private void setRougeNResult(double rougeNResult) {
		this.rougeNResult = rougeNResult;
	}
	
	/**
	 * @return the summarizedDocument
	 */
	public Document getSummarizedDocument() {
		return summarizedDocument;
	}

	public double getSummarizationTime() {
		return summarizationTime;
	}


	
	

}
