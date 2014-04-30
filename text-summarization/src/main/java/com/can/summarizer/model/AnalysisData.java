package com.can.summarizer.model;

public class AnalysisData {
	
	private final String name;
	private double originalWordNumber=0;
	private double refWordNumber=0;
	private double summWordNumber=0;
	private double rougeNValue=0;
	private double fitnessValue=-1;
	private double presicion=0;
	private double recall=0;
	private double f1=0;
	private double sentencePrecision=0;
	private double sentenceRecall=0;
	private double sentenceF1=0;
	private int refSentenceNumber=0;
	private int sumSentenceNumber=0;
	
	public AnalysisData(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public double getOriginalWordNumber() {
		return originalWordNumber;
	}

	public void setOriginalWordNumber(double originalWordNumber) {
		this.originalWordNumber = originalWordNumber;
	}

	public double getRefWordNumber() {
		return refWordNumber;
	}

	public void setRefWordNumber(double refWordNumber) {
		this.refWordNumber = refWordNumber;
	}

	public double getSummWordNumber() {
		return summWordNumber;
	}

	public void setSummWordNumber(double summWordNumber) {
		this.summWordNumber = summWordNumber;
	}

	public double getRougeNValue() {
		return rougeNValue;
	}

	public void setRougeNValue(double rougeNValue) {
		this.rougeNValue = rougeNValue;
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	public void setFitnessValue(double fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	/**
	 * @return the presicion
	 */
	public double getPresicion() {
		return presicion;
	}

	/**
	 * @param presicion the presicion to set
	 */
	public void setPresicion(double presicion) {
		this.presicion = presicion;
	}

	/**
	 * @return the recall
	 */
	public double getRecall() {
		return recall;
	}

	/**
	 * @param recall the recall to set
	 */
	public void setRecall(double recall) {
		this.recall = recall;
	}

	/**
	 * @return the f1
	 */
	public double getF1() {
		return f1;
	}

	/**
	 * @param f1 the f1 to set
	 */
	public void setF1(double f1) {
		this.f1 = f1;
	}

	/**
	 * @return the sentencePrecision
	 */
	public double getSentencePrecision() {
		return sentencePrecision;
	}

	/**
	 * @param sentencePrecision the sentencePrecision to set
	 */
	public void setSentencePrecision(double sentencePrecision) {
		this.sentencePrecision = sentencePrecision;
	}

	/**
	 * @return the sentenceRecall
	 */
	public double getSentenceRecall() {
		return sentenceRecall;
	}

	/**
	 * @param sentenceRecall the sentenceRecall to set
	 */
	public void setSentenceRecall(double sentenceRecall) {
		this.sentenceRecall = sentenceRecall;
	}

	/**
	 * @return the sentenceF1
	 */
	public double getSentenceF1() {
		return sentenceF1;
	}

	/**
	 * @param sentenceF1 the sentenceF1 to set
	 */
	public void setSentenceF1(double sentenceF1) {
		this.sentenceF1 = sentenceF1;
	}

	/**
	 * @return the refSentenceNumber
	 */
	public int getRefSentenceNumber() {
		return refSentenceNumber;
	}

	/**
	 * @param refSentenceNumber the refSentenceNumber to set
	 */
	public void setRefSentenceNumber(int refSentenceNumber) {
		this.refSentenceNumber = refSentenceNumber;
	}

	/**
	 * @return the sumSentenceNumber
	 */
	public int getSumSentenceNumber() {
		return sumSentenceNumber;
	}

	/**
	 * @param sumSentenceNumber the sumSentenceNumber to set
	 */
	public void setSumSentenceNumber(int sumSentenceNumber) {
		this.sumSentenceNumber = sumSentenceNumber;
	}
	

}
