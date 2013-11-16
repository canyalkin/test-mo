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
	private Environment env;
		
	public AbstractSummarizer(String summarizerName) {
		super();
		this.summarizerName = summarizerName;
		
	}

	public AbstractSummarizer() {
	}
	

	/*public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		
		return bean;
	}*/

	public void getStemmingFromProperty() {
		String stemming=(env.getProperty("stemming"));
		if(stemming==null)
			stemming="true";
		if(stemming.equalsIgnoreCase("true")){
			setStemming(true);
		}else{
			setStemming(false);
		}
	}

	public void getStopWordEliminationFromProperty() {
		extractStopWordEliminationFromProperties();
		
	}

	public void extractSummPropFromProperties() {
		String summaryProportion=env.getProperty("summaryProportion");
		if(summaryProportion==null){
			this.setSummaryProportion(0.25);
		}else {
			this.setSummaryProportion(Double.parseDouble(summaryProportion));
		}
	}

	private void extractStopWordEliminationFromProperties() {
		String stopWordElimination = (env.getProperty("stopWordElimination"));
		if(stopWordElimination==null)
			stopWordElimination="true";
		if(stopWordElimination.equalsIgnoreCase("true")){
			setStopWordElimination(true);
		}else{
			setStopWordElimination(false);
		}
	}
	

	/*public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		getStopWordEliminationFromProperty();
		getStemmingFromProperty();
		extractSummPropFromProperties();
		return bean;
	}*/

	
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
	 * @return the env
	 */
	public Environment getEnv() {
		return env;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(Environment env) {
		this.env = env;
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
