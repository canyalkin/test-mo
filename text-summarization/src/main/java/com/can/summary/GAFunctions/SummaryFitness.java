package com.can.summary.GAFunctions;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.impl.FixedBinaryGene;

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
			System.out.println("CF:"+CF);
			System.out.println("RF:"+RF);
			
			printGene(chromosome.getGenes());
		}
		return 0.5*CF+0.5*RF;
	}
	
	private double calculateCohesionFactor(IChromosome chromosome){
		double value=0.0;
		double Cs=0.0;
		double M=0.0;
		List<Integer> summaryIndex=getSummaryIndexes(chromosome);
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
		Cs=Cs/Ns;
		if(Cs!=0){
			value=Math.log10(9.0*Cs+1.0)/Math.log10(9.0*M+1.0);
		}else{
			value=0.0;
		}
		return value;
	}
	
	private double calculateReadibilityFactor(IChromosome chromosome){
		double value=0.0;
		double max=0.0;
		List<Integer> indexList = getSummaryIndexes(chromosome);
		for(int i=0;i<indexList.size()-1;i++){
			double weight=similarityGraph.getEdge(indexList.get(i+1), indexList.get(i)).getWeight();
			value+=weight;
			
		}
		
	
		return value;
	}

	private List<Integer> getSummaryIndexes(IChromosome chromosome) {
		List<Integer> indexList=new LinkedList<Integer>();
		Gene[] genes = chromosome.getGenes();
		for(int i=0;i<genes.length;i++){
			FixedBinaryGene binaryGene = (FixedBinaryGene)genes[i];
			int[] alleleValue=(int[])binaryGene.getAllele();
			if(alleleValue[0]==1){
				indexList.add(i);
			}
		}
		return indexList;
	}
	
	private static void printGene(Gene[] genes) {
		System.out.println("printGene....");
		for (int i=0;i<genes.length;i++) {
			int[] value = (int[]) genes[i].getAllele();
			System.out.println("Gene ("+i+"):"+value[0]);
		}
		System.out.println("printGene.... ENDs");
	}



}
