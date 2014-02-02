package com.can.analysis;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.can.word.utils.PropertyHandler;

public class AnalysisHandler {
	private static final Logger LOGGER = Logger.getLogger(AnalysisHandler.class);
	private static AnalysisHandler INSTANCE = null;
	
	public static AnalysisHandler getInstance(){
		if(INSTANCE==null){
			INSTANCE=new AnalysisHandler();
		}
		return INSTANCE;
	}
	
	@Autowired
	private AnalysisProperty analysisProperty;
	
	@Autowired PropertyHandler propertyHandler;
	
	private List<AnalysisValue> analyseValues=new LinkedList<AnalysisValue>();
	private int currIndex=0;
	private AnalysisHandler() {
		
		LOGGER.debug(this);
	}
	
	public void reset(){
		List<Integer> generationList = analysisProperty.getGeneration_number();
		List<Integer> populationList = analysisProperty.getPopulation();
		List<Double> crossoverList = analysisProperty.getCrossover();
		List<Integer> mutationList = analysisProperty.getMutation();
		List<Integer> clusterList = analysisProperty.getClusterNumber();
		
		AnalysisValue analysisValue;
		for(int i=0;i<generationList.size();i++){
			for(int j=0;j<populationList.size();j++){
				for(int k=0; k<crossoverList.size();k++){
					for(int l=0;l<mutationList.size();l++){
						for(int m=0;m<clusterList.size();m++){
							analysisValue=new AnalysisValue();
							analysisValue.setGenerationNumber(generationList.get(i));
							analysisValue.setPopulationSize(populationList.get(j));
							analysisValue.setCrossoverRate(crossoverList.get(k));
							analysisValue.setMutationrate(mutationList.get(l));
							analysisValue.setClusterNumber(clusterList.get(m));
							analyseValues.add(analysisValue);
						}
					}
				}
			}
		}
		
		
		
	}
	
	public boolean setNextValues(){
		
		if(currIndex<analyseValues.size()){
			propertyHandler.setGenerationNumber(analyseValues.get(currIndex).getGenerationNumber());
			propertyHandler.setPopulationNumber(analyseValues.get(currIndex).getPopulationSize());
			propertyHandler.setCrossoverRate(analyseValues.get(currIndex).getCrossoverRate());
			propertyHandler.setMutationRate(analyseValues.get(currIndex).getMutationrate());
			propertyHandler.setClusterNumber(analyseValues.get(currIndex).getClusterNumber());
			currIndex++;
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnalysisHandler [analyseValues=" + analyseValues
				+ ", currIndex=" + currIndex + "]";
	}
	
	public boolean isAnalysisMode(){
		return propertyHandler.isAnalysisMode(); 
	}
	

}
