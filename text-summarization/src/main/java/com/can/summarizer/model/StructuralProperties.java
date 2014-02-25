package com.can.summarizer.model;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.can.summary.calculations.FrequencyCalculator;

public class StructuralProperties {
	
	private static final Logger LOGGER = Logger.getLogger(StructuralProperties.class);
	private HashMap<String, Integer> freqTable;
	private HashMap<String, List<Double>> tfTable;
	private HashMap<String, Double> isf;
	private HashMap<String, Double> idfTable;
	private HashMap<String, Double> idf;
	private HashMap<String, Double> tfIdf;
	private HashMap<String, Integer> numberOfSentenceContains;
	private HashMap<String, Integer> containsWordsTogether;



	public StructuralProperties(Document document){
		
		freqTable=FrequencyCalculator.createFrequencyTable(document);
		LOGGER.debug("freq table created..."+freqTable);
		tfTable = FrequencyCalculator.calculateTermFreq(freqTable, document);
		LOGGER.debug("TermFreq table created..."+tfTable);
		isf = FrequencyCalculator.calculateInverseSentenceFreqTable(freqTable, document);
		LOGGER.debug("isf..."+isf);
		idfTable=FrequencyCalculator.calculateInverseSentenceFreqTable(freqTable, document);
		LOGGER.debug("InverseSentenceFreqTable created..."+idfTable);
		idf=FrequencyCalculator.createIdfTable(freqTable);
		LOGGER.debug("IDF created..."+idf);
		tfIdf=FrequencyCalculator.createTfIdfTable(freqTable, idf);
		LOGGER.debug("TF IDF created..."+tfIdf);
		numberOfSentenceContains=FrequencyCalculator.calculateNumberOfSentenceContains(freqTable,document);
		containsWordsTogether=FrequencyCalculator.getWordsTogetherMap(freqTable, document);
		
	}

	/**
	 * @return the freqTable
	 */
	public HashMap<String, Integer> getFreqTable() {
		return freqTable;
	}

	/**
	 * @return the tfTable
	 */
	public HashMap<String, List<Double>> getTfTable() {
		return tfTable;
	}

	/**
	 * @return the isf
	 */
	public HashMap<String, Double> getIsf() {
		return isf;
	}

	/**
	 * @return the idfTable
	 */
	public HashMap<String, Double> getIdfTable() {
		return idfTable;
	}

	/**
	 * @return the idf
	 */
	public HashMap<String, Double> getIdf() {
		return idf;
	}

	/**
	 * @return the tfIdf
	 */
	public HashMap<String, Double> getTfIdf() {
		return tfIdf;
	}
	/**
	 * @return the numberOfSentenceContains
	 */
	public HashMap<String, Integer> getNumberOfSentenceContains() {
		return numberOfSentenceContains;
	}

	/**
	 * @return the containsWordsTogether
	 */
	public HashMap<String, Integer> getContainsWordsTogether() {
		return containsWordsTogether;
	}

	public void clear() {
		freqTable=null;
		tfTable=null;
		isf=null;
		idfTable=null;
		idf=null;
		tfIdf=null;
		numberOfSentenceContains=null;
		containsWordsTogether=null;
		
	}

}
