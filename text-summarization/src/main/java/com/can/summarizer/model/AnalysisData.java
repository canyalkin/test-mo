package com.can.summarizer.model;

public class AnalysisData {
	
	private final String name;
	private double originalWordNumber=0;
	private double refWordNumber=0;
	private double summWordNumber=0;
	private double rougeNValue=0;
	private double fitnessValue=-1;
	
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
	

}
