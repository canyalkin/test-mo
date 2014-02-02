package com.can.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class AnalysisProperty {

	private static final Logger LOGGER = Logger.getLogger(AnalysisProperty.class);
	
	private static AnalysisProperty INSTANCE=null;
	public static AnalysisProperty getInstance(){
		if(INSTANCE==null){
			INSTANCE=new AnalysisProperty();
		}
		return INSTANCE;
	}
	
	private AnalysisProperty() {
		LOGGER.debug(this);
	}
	
	
	public void init() {
		String list = environment.getProperty("analysis_generation_number", "");
		String[] array = list.split(",");
		for (int i = 0; i < array.length; i++) {
			generation_number.add(Integer.parseInt(array[i]));
		}
		
		list = environment.getProperty("analysis_population", "");
		array = list.split(",");
		for (int i = 0; i < array.length; i++) {
			population.add(Integer.parseInt(array[i]));
		}
		
		list = environment.getProperty("analysis_crossover", "");
		array = list.split(",");
		for (int i = 0; i < array.length; i++) {
			crossover.add(Double.parseDouble(array[i]));
		}
		
		list = environment.getProperty("analysis_mutation", "");
		array = list.split(",");
		for (int i = 0; i < array.length; i++) {
			mutation.add(Integer.parseInt(array[i]));
		}
		
		list = environment.getProperty("analysis_cluster", "");
		array = list.split(",");
		for (int i = 0; i < array.length; i++) {
			clusterNumber.add(Integer.parseInt(array[i]));
		}
		
		
	}


	/**
	 * @return the clusterNumber
	 */
	public List<Integer> getClusterNumber() {
		return clusterNumber;
	}


	@Autowired
	private Environment environment;
	
	private List<Integer> generation_number=new ArrayList<Integer>(20);
	private List<Integer> population=new ArrayList<Integer>(20);
	private List<Double> crossover=new ArrayList<Double>(20);
	private List<Integer> mutation=new ArrayList<Integer>(20);
	private List<Integer> clusterNumber=new ArrayList<Integer>(20);
	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * @return the generation_number
	 */
	public List<Integer> getGeneration_number() {
		return generation_number;
	}

	/**
	 * @param generation_number the generation_number to set
	 */
	public void setGeneration_number(List<Integer> generation_number) {
		this.generation_number = generation_number;
	}

	/**
	 * @return the population
	 */
	public List<Integer> getPopulation() {
		return population;
	}

	/**
	 * @param population the population to set
	 */
	public void setPopulation(List<Integer> population) {
		this.population = population;
	}

	/**
	 * @return the crossover
	 */
	public List<Double> getCrossover() {
		return crossover;
	}

	/**
	 * @param crossover the crossover to set
	 */
	public void setCrossover(List<Double> crossover) {
		this.crossover = crossover;
	}

	/**
	 * @return the mutation
	 */
	public List<Integer> getMutation() {
		return mutation;
	}

	/**
	 * @param mutation the mutation to set
	 */
	public void setMutation(List<Integer> mutation) {
		this.mutation = mutation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnalysisProperty [environment=" + environment
				+ ", generation_number=" + generation_number + ", population="
				+ population + ", crossover=" + crossover + ", mutation="
				+ mutation + "]";
	}
	
}
