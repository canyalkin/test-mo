package com.can.summary.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.document.handler.module.StopWordHandler;
import com.can.summarizer.interfaces.IWordStemmer;
import com.can.summarizer.interfaces.SummaryStrategy;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.word.utils.PropertyHandler;

@Component
public abstract class AbstractSummarizer implements SummaryStrategy {
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
	
	public Document createSummaryDocument(Document document, List<Integer> indexes){
		Document summaryDocument=new Document(false);
		
		summaryDocument.setTitle(document.getTitle());
		List<Sentence> sumSentence=new ArrayList<Sentence>(30);
		int wordCount=0;
		int i=0;
		while (i < indexes.size()) {
				sumSentence.add(document.getSentenceList().get(indexes.get(i)));
				//wordCount+=document.getSentenceList().get(indexes.get(i)).getOriginalSentencesWordNumber();
				i++;
		}
		document.clear();
		summaryDocument.setSentenceList(sumSentence);
		return summaryDocument;
	}
	
	public Document finalizeSummaryWithPropertyWordNumber(Document document){
		Document summaryDocument=new Document(false);
		summaryDocument.setTitle(document.getTitle());
		List<Sentence> sumSentence=new ArrayList<Sentence>(30);
		int wordCount=0;
		int i=0;
		while ( wordCount < propertyHandler.getMaxWordNumber() && i<document.getSentenceList().size()) {
				sumSentence.add(document.getSentenceList().get(i));
				wordCount+=document.getSentenceList().get(i).getOriginalSentencesWordNumber();
				i++;
		}
		summaryDocument.setSentenceList(sumSentence);
		
		return summaryDocument;
	}
	@Override
	public Document doSummary(Document aDocument) {
		getStopWordEliminationFromProperty();
		getStemmingFromProperty();
		extractSummPropFromProperties();
		LOGGER.debug("summarization starts...");
		setDocumentToBeSummarized(aDocument);
		if(isStemming()){
			doStemming();
		}
		if(isStopWordElimination()){
			doStopWordElimination();
		}
		
		setNumberOfSentences(aDocument.getSentenceList().size());
		setDesiredNumberOfSentenceInSum((int)Math.round(getNumberOfSentences()*getSummaryProportion()));
		
		return null;
	}
	
	protected int calculateAverageWordInSentence(Document document) {
		int average=0;
		List<Sentence> sentences = document.getSentenceList();
		List<Integer>wordSizeList=new ArrayList<Integer>();
		for (Sentence sentence : sentences) {
			wordSizeList.add(sentence.getWords().size());
		}
		Collections.sort(wordSizeList);
		Collections.reverse(wordSizeList);
		int size=3;
		if(wordSizeList.size()<size){
			size=wordSizeList.size();
		}
		for(int i=0;i<size;i++){
			average+=wordSizeList.get(i);
		}
		average=(int) (average/(double)size);
		wordSizeList=null;
		return average;
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
		if(this.desiredNumberOfSentenceInSum<2){
			this.desiredNumberOfSentenceInSum=2;
		}
	}

	public double getSummaryProportion() {
		return summaryProportion;
	}

	public void setSummaryProportion(double summaryProportion) {
		this.summaryProportion = summaryProportion;
	}

}
