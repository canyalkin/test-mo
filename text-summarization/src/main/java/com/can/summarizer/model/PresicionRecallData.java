package com.can.summarizer.model;

public class PresicionRecallData {

	private final double presicion;
	private final double recall;
	private final double f1;
	
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



}
