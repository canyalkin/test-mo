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
		idfTable=isf;
		LOGGER.debug("InverseSentenceFreqTable created..."+idfTable);
		tfIdf=FrequencyCalculator.createTfIdfTable(freqTable, isf);
		LOGGER.debug("TF IDF created..."+tfIdf);
		numberOfSentenceContains=FrequencyCalculator.calculateNumberOfSentenceContains(freqTable,document);
		containsWordsTogether=FrequencyCalculator.getWordsTogetherMap(freqTable, document);
		LOGGER.debug(this);
		
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
		
		tfIdf=null;
		numberOfSentenceContains=null;
		containsWordsTogether=null;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StructuralProperties [freqTable=" + freqTable + ", tfTable="
				+ tfTable + ", isf=" + isf + ", idfTable=" + idfTable
				+ ", tfIdf=" + tfIdf
				+ ", numberOfSentenceContains=" + numberOfSentenceContains
				+ ", containsWordsTogether=" + containsWordsTogether + "]";
	}
	

}
