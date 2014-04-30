package com.can.summarizer.model;

public class PresicionRecallData {

	private final double presicion;
	private final double recall;
	private final double f1;
	
	private double sentencePrecision;
	private double sentenceRecall;
	private double sentenceF1;
	
	public PresicionRecallData(double presicion,double recall, double f1) {
		
		this.presicion=presicion;
		this.recall=recall;
		this.f1=f1;
		
	}
	/**
	 * @return the presicion
	 */
	public double getPresicion() {
		return presicion;
	}


	/**
	 * @return the recall
	 */
	public double getRecall() {
		return recall;
	}


	/**
	 * @return the f1
	 */
	public double getF1() {
		return f1;
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



}
