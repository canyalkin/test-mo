package com.can.summary.GAFunctions;

import java.util.List;

import org.apache.log4j.Logger;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

import com.can.graph.module.Graph;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.CosineSimilarity;

public class SummaryFitness extends FitnessFunction {
	private int sentenceNumInSum=0;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7571866515538817954L;
	private static final Logger LOGGER = Logger.getLogger(SummaryFitness.class);
	private Graph similarityGraph;
	private Document document;
	
	public SummaryFitness(Graph graph,int sentenceNumInSum,Document document) {
		similarityGraph=graph;
		this.sentenceNumInSum=sentenceNumInSum;
		this.document=document;
	}

	@Override
	protected double evaluate(IChromosome chromosome) {
		double CF=calculateCohesionFactor(chromosome);
		double RF=calculateReadibilityFactor(chromosome);
		//double sentenceToTitleSim=calculateSentenceToTitleSim(chromosome);
		double titleSim=calculateSentenceToTitleSim(chromosome);
		double sentenceLength=calculateSentenceLength(chromosome);
		double returnValue=0.0;
		//returnValue=0.3*CF+0.2*RF+0.5*sentenceToTitleSim;
		//returnValue=0.3*CF+0.3*RF+0.2*titleSim+0.2*sentenceLength;
		//returnValue=0.3*CF+0.3*RF+0.4*sentenceLength;
		returnValue=0.5*CF+0.5*RF;
		if(returnValue>1){
			LOGGER.debug("CF:"+CF);
			LOGGER.debug("RF:"+RF);
			LOGGER.debug("simToTitle:"+titleSim);
			if(LOGGER.isTraceEnabled()){
				printGene(chromosome.getGenes());
			}
		}
		if(Double.isNaN(returnValue)){
			LOGGER.error("fitness : NaN value !!!!!!!!");
			returnValue=0.0;
		}
		return returnValue;
	}
	
	private double calculateSentenceLength(IChromosome chromosome) {
		List<Integer> indexList = GeneHandler.getSummaryIndexes(chromosome);
		double retVal=0.0;
		for (Integer index : indexList) {
			retVal+=document.getSentenceList().get(index).getWords().size();
		}
		return retVal/indexList.size();
	}

	private double calculateSentenceToTitleSim(IChromosome chromosome) {
		List<Integer> indexList = GeneHandler.getSummaryIndexes(chromosome);
		if (document.getTitle()==null){
			return 0.0;
		}
		double retVal=0.0;
		for (Integer index : indexList) {
			retVal+=CosineSimilarity.calculate(document.getTitle(), document.getSentenceList().get(index));
		}
		retVal=retVal/indexList.size();
		return retVal;
	}

	private double calculateCohesionFactor(IChromosome chromosome){
		double value=0.0;
		double Cs=0.0;
		double M=0.0;
		List<Integer> summaryIndex=GeneHandler.getSummaryIndexes(chromosome);
		for(int i=0;i<summaryIndex.size();i++){
			int indexI=summaryIndex.get(i);
			for(int j=i+1;j<summaryIndex.size();j++){
				int indexJ=summaryIndex.get(j);
				Cs+=similarityGraph.getEdge(indexJ, indexI).getWeight();
				if(similarityGraph.getEdge(indexJ, indexI).getWeight()>M){
					M=similarityGraph.getEdge(indexJ, indexI).getWeight();
				}
			}
		}
		
		double Ns=sentenceNumInSum*(sentenceNumInSum-1)/2.0;
		if(Ns!=0.0){
			Cs=Cs/Ns;
		}
		
		if(Cs!=0.0 && M != 0.0){
			value=Math.log10(9.0*Cs+1.0)/Math.log10(9.0*M+1.0);
		}else{
			value=0.0;
		}
		
		if(Double.isNaN(value)){
			LOGGER.error("calculateCohesionFactor : NaN value !!!!!!!!");
			value=0.0;
		}
		return value;
	}
	
	private double calculateReadibilityFactor(IChromosome chromosome){
		double value=0.0;
		List<Integer> indexList = GeneHandler.getSummaryIndexes(chromosome);
		for(int i=0;i<indexList.size()-1;i++){
			double weight=similarityGraph.getEdge(indexList.get(i+1), indexList.get(i)).getWeight();
			value+=weight;
		}
		
		if(similarityGraph.getMaxLength()!=0){
			value=value/similarityGraph.getMaxLength();
		}
		if(Double.isNaN(value)){
			LOGGER.error("calculateReadibilityFactor : NaN value !!!!!!!!");
			value=0.0;
		}
		return value;
	}

	private static void printGene(Gene[] genes) {
		LOGGER.trace("printGene....");
		for (int i=0;i<genes.length;i++) {
			int[] value = (int[]) genes[i].getAllele();
			LOGGER.trace("Gene ("+i+"):"+value[0]);
		}
		LOGGER.trace("printGene.... ENDs");
	}



}
