package com.can.summary.module;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.can.document.handler.module.StopWordHandler;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.interfaces.SummaryStrategy;
import com.can.summarizer.model.Document;
import com.can.word.utils.PropertyHandler;

@Component
public abstract class AbstractSummarizer implements SummaryStrategy,BeanPostProcessor {
	private static final Logger LOGGER = Logger.getLogger(AbstractSummarizer.class);
	private String summarizerName;
	private Document documentToBeSummarized;
	private boolean stopWordElimination=true;
	private int numberOfSentences;
	private boolean stemming=true;
	
	private double summaryProportion=0.25;
	private int desiredNumberOfSentenceInSum=8;
	
	@Autowired
	StopWordHandler stopWordHandler;
	
	@Autowired
	IWordStemmer wordStemmer;
	
	
	@Autowired
	private PropertyHandler propertyHandler;
		
	public AbstractSummarizer(String summarizerName) {
		super();
		this.summarizerName = summarizerName;
		
	}

	public AbstractSummarizer() {
	}
	
	public void getStemmingFromProperty() {
		setStemming(propertyHandler.isStemming());
		
	}

	public void getStopWordEliminationFromProperty() {
		extractStopWordEliminationFromProperties();
		
	}

	public void extractSummPropFromProperties() {
		setSummaryProportion(propertyHandler.getSummaryProportion());
		
	}

	private void extractStopWordEliminationFromProperties() {
		setStopWordElimination(propertyHandler.isStopWordElimination());
	}
	
	public void doStopWordElimination(){
		setDocumentToBeSummarized(stopWordHandler.doStopWordElimination(getDocumentToBeSummarized()));		
	}
	
	public void doStemming(){
		setDocumentToBeSummarized(wordStemmer.doStemming(getDocumentToBeSummarized()));
	}

	
	public String getSummarizerName() {
		return summarizerName;
	}

	public void setSummarizerName(String summarizerName) {
		this.summarizerName = summarizerName;
	}

	/**
	 * @return the stopWordElimination
	 */
	public boolean isStopWordElimination() {
		return stopWordElimination;
	}



	/**
	 * @param stopWordElimination the stopWordElimination to set
	 */
	public void setStopWordElimination(boolean stopWordElimination) {
		this.stopWordElimination = stopWordElimination;
	}



	public Document getDocumentToBeSummarized() {
		return documentToBeSummarized;
	}



	public void setDocumentToBeSummarized(Document documentToBeSummarized) {
		this.documentToBeSummarized = documentToBeSummarized;
	}


	/**
	 * @return the numberOfSentences
	 */
	public int getNumberOfSentences() {
		return numberOfSentences;
	}

	/**
	 * @param numberOfSentences the numberOfSentences to set
	 */
	public void setNumberOfSentences(int numberOfSentences) {
		this.numberOfSentences = numberOfSentences;
	}

	/**
	 * @return the stemming
	 */
	public boolean isStemming() {
		return stemming;
	}

	/**
	 * @param stemming the stemming to set
	 */
	public void setStemming(boolean stemming) {
		this.stemming = stemming;
	}
	
	public int getDesiredNumberOfSentenceInSum() {
		return desiredNumberOfSentenceInSum;
	}


	public void setDesiredNumberOfSentenceInSum(int desiredNumberOfSentenceInSum) {
		this.desiredNumberOfSentenceInSum = desiredNumberOfSentenceInSum;
	}

	public double getSummaryProportion() {
		return summaryProportion;
	}

	public void setSummaryProportion(double summaryProportion) {
		this.summaryProportion = summaryProportion;
	}

}
