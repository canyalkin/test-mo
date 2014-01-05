package com.can.summary.GAFunctions;

import java.util.List;

import org.apache.log4j.Logger;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

import com.can.graph.module.Graph;

public class SummaryFitness extends FitnessFunction {
	private int sentenceNumInSum=0;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7571866515538817954L;
	private static final Logger LOGGER = Logger.getLogger(SummaryFitness.class);
	Graph similarityGraph;
	
	public SummaryFitness(Graph graph,int sentenceNumInSum) {
		similarityGraph=graph;
		this.sentenceNumInSum=sentenceNumInSum;
	}

	@Override
	protected double evaluate(IChromosome chromosome) {
		double CF=calculateCohesionFactor(chromosome);
		double RF=calculateReadibilityFactor(chromosome);
		if(0.5*CF+0.5*RF>1){
			LOGGER.debug("CF:"+CF);
			LOGGER.debug("RF:"+RF);
			printGene(chromosome.getGenes());
		}
		return 0.5*CF+0.5*RF;
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
