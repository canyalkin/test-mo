package com.can.success.calculations;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.can.document.handler.module.BulkDocumentHandler;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.FrequencyCalculator;

public class PresicionRecallCalculator {
	private static final Logger LOGGER = Logger.getLogger(PresicionRecallCalculator.class);
	private Document refDocument=null;
	private Document sysDocument=null;
	
	private double presicion;
	private double recall;
	private double f1;
	
	private double sentencePrecision;
	private double sentenceRecall;
	private double sentenceF1;
	
	public void doCalulations(){
		calculateWrtWords();
		calculateWrtSentence();
		
	}
	
	private void calculateWrtSentence() {
		int intersection=calculateSentenceIntersection();
		
		sentencePrecision=intersection/(double)sysDocument.getSentenceList().size();
		sentenceRecall=intersection/(double)refDocument.getSentenceList().size();
		sentenceF1=2*(sentencePrecision*sentenceRecall)/(sentencePrecision+sentenceRecall);
		
		if(Double.isNaN(sentencePrecision)||Double.isNaN(sentenceRecall)||Double.isNaN(sentenceF1)){
			LOGGER.error("sentencePrecision:"+sentencePrecision);
			LOGGER.error("sentenceRecall:"+sentenceRecall);
			LOGGER.error("sentenceF1:"+sentenceF1);
			LOGGER.error("intersection:"+intersection);
			sentenceF1=0.0;
		}
		
	}

	private int calculateSentenceIntersection() {
		List<Sentence> refSentenceList = refDocument.getSentenceList();
		List<Sentence> sysSentenceList = sysDocument.getSentenceList();
		int intersection=0;
		for (Sentence curSysSentence : sysSentenceList) {
			for (Sentence curRefSentence : refSentenceList) {
				if(curRefSentence.getOriginalSentence().equalsIgnoreCase(curSysSentence.getOriginalSentence())){
					intersection++;
				}
			}
		}
		
		return intersection;
	}

	private void calculateWrtWords() {
		HashMap<String, Integer> systemFreqTable = FrequencyCalculator.createFrequencyTable(sysDocument);
		HashMap<String, Integer> refFrequencyTable = FrequencyCalculator.createFrequencyTable(refDocument);
		
		int wordsInSystem=systemFreqTable.keySet().size();
		int wordsInRef=refFrequencyTable.keySet().size();
		int intersection = calculateWordIntersection(systemFreqTable, refFrequencyTable);
		
		presicion=intersection/(double)wordsInSystem;
		recall=intersection/(double)wordsInRef;
		f1=2*(presicion*recall)/(presicion+recall);
		
		if(Double.isNaN(presicion)||Double.isNaN(recall)||Double.isNaN(f1)){
			LOGGER.error("presicion:"+presicion);
			LOGGER.error("recall:"+recall);
			LOGGER.error("f1:"+f1);
			LOGGER.error("intersection:"+intersection);
			LOGGER.error("wordsInSystem:"+wordsInSystem);
			LOGGER.error("wordsInRef:"+wordsInRef);
			f1=0.0;
		}
	}
	
	public double getPresicion(){
		return presicion;
	}
	public double getRecall(){
		return recall;
	}
	public double getF1() {
		return f1;
	}
	
	public int calculateWordIntersection(HashMap<String, Integer> systemFreqTable, HashMap<String, Integer> refFrequencyTable){
		int cnt=0;
		Set<String> keys = systemFreqTable.keySet();
		for (String key : keys) {
			if(refFrequencyTable.containsKey(key)){
				cnt++;
			}
		}
		return cnt;
	}
	
	
	
	
	/**
	 * @param refDocument the refDocument to set
	 */
	public void setRefDocument(Document refDocument) {
		this.refDocument = refDocument;
	}
	/**
	 * @param sysDocument the sysDocument to set
	 */
	public void setSysDocument(Document sysDocument) {
		this.sysDocument = sysDocument;
	}
	
	/**
	 * @return the sentencePrecision
	 */
	public double getSentencePrecision() {
		return sentencePrecision;
	}

	/**
	 * @return the sentenceRecall
	 */
	public double getSentenceRecall() {
		return sentenceRecall;
	}

	/**
	 * @return the sentenceF1
	 */
	public double getSentenceF1() {
		return sentenceF1;
	}

	

	
	

}
