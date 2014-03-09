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
	

}
