package com.can.analysis;

public class AnalysisValue{
	private int generationNumber;
	private int populationSize;
	private double crossoverRate;
	private int mutationRate;
	private int clusterNumber;
	/**
	 * @return the generationNumber
	 */
	public int getGenerationNumber() {
		return generationNumber;
	}
	/**
	 * @param generationNumber the generationNumber to set
	 */
	public void setGenerationNumber(int generationNumber) {
		this.generationNumber = generationNumber;
	}
	/**
	 * @return the populationSize
	 */
	public int getPopulationSize() {
		return populationSize;
	}
	/**
	 * @param populationSize the populationSize to set
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	/**
	 * @return the crossoverRate
	 */
	public double getCrossoverRate() {
		return crossoverRate;
	}
	/**
	 * @param crossoverRate the crossoverRate to set
	 */
	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}
	/**
	 * @return the mutationrate
	 */
	public int getMutationrate() {
		return mutationRate;
	}
	/**
	 * @param mutationrate the mutationrate to set
	 */
	public void setMutationrate(int mutationrate) {
		this.mutationRate = mutationrate;
	}
	public int getClusterNumber() {
		return clusterNumber;
	}
	public void setClusterNumber(int clusterNumber) {
		this.clusterNumber = clusterNumber;
	}
	
	
}