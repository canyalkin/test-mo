package com.can.word.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.can.document.handler.module.BulkDocumentHandler;
import com.can.summarizer.model.RougeNType;

@Component
public class PropertyHandler {

	@Autowired
	private Environment environment;
	
	public boolean isStemming() {
		String stopWordElimination = (environment.getProperty("stopWordElimination"));
		if(stopWordElimination==null)
			stopWordElimination="true";
		if(stopWordElimination.equalsIgnoreCase("true")){
			return(true);
		}else{
			return (false);
		}
	}

	public boolean isStopWordElimination() {
		String stopWordElimination = (environment.getProperty("stopWordElimination"));
		if(stopWordElimination==null)
			stopWordElimination="true";
		if(stopWordElimination.equalsIgnoreCase("true")){
			return(true);
		}else{
			return(false);
		}
	}
	
	public int getRougeNNumber(){
		return Integer.parseInt(environment.getProperty("nGramNumber"));
	}
	
	public RougeNType getRougeNType(){
		return RougeNType.getFromValue(environment.getProperty("nGramType"));
	}

	public String getDocumentFolder() {
		return environment.getProperty("docFolder");
	}
	
	public String getRefDocumentFolder() {
		return environment.getProperty("humanSummary");
	}
	
	public double getSummaryProportion(){
		String summaryProportion=environment.getProperty("summaryProportion");
		if(summaryProportion==null){
			return 0.25;
		}else {
			return (Double.parseDouble(summaryProportion));
		}
	}
	
	public int getMutationRate(){
		int mutationRate=1000;
		try{
			mutationRate=Integer.parseInt(environment.getProperty("mutation_rate"));
		}catch (Exception e){
			mutationRate=1000;
		}
		return mutationRate;
	}

	public double getCrossoverRate() {
		double crossoverRate=0.85;
		try{
			crossoverRate=Double.parseDouble(environment.getProperty("crossover_rate"));
		}catch (Exception e){
			crossoverRate=0.85;
		}
		return crossoverRate;
	}

	public int getGenerationNumber() {
		int generationNumber=20;
		try{
			generationNumber=Integer.parseInt(environment.getProperty("generation_number"));
		}catch (Exception e){
			generationNumber=20;
		}
		return generationNumber;
	}

	public int getPopulationNumber() {
		int popNumber=20;
		try{
			popNumber=Integer.parseInt(environment.getProperty("population_size"));
		}catch (Exception e){
			popNumber=20;
		}
		return popNumber;
	}
	public String getDocumentName(){
		return environment.getProperty("file");
	}
	public String getRefDocumentName(){
		return environment.getProperty("sum");
	}
	
	public boolean hasTitle(){
		if(environment.getProperty("title").equals("true") || environment.getProperty("title").equals("yes")){
			return true;
		}
		return false;
	}
	
	public String getWordNetFolder(){
		return environment.getProperty("wordNetFolder");
	}
	
	public int getMaxWordNumber(){
		int wordNumber=100;
		try {
			wordNumber=Integer.parseInt(environment.getProperty("maxWordNumber"));
		} catch (Exception e) {
			wordNumber=100;
		}
		return wordNumber;	
	}
	
	public String getOutputFile(){
		return environment.getProperty("outputFile");
	}
	
}
