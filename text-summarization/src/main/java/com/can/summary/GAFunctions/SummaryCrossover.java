package com.can.summary.GAFunctions;

import java.util.List;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;

public class SummaryCrossover extends CrossoverOperator {

	private int numberOfSentencesInDoc=0;
	private int numberOfSentencesInSum=0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 139273920970574958L;

	
	public SummaryCrossover(int numberDoc,int numberSumm,Configuration configuration) throws InvalidConfigurationException {
		super(configuration, 0.85);
		
		numberOfSentencesInDoc=numberDoc;
		numberOfSentencesInSum=numberSumm;
	}
	public SummaryCrossover(int numberDoc,int numberSumm,Configuration configuration,double crossoverRate) throws InvalidConfigurationException {
		super(configuration, crossoverRate);
		
		numberOfSentencesInDoc=numberDoc;
		numberOfSentencesInSum=numberSumm;
	}
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate,
            List a_candidateChromosomes,
            RandomGenerator generator) {
		// TODO Auto-generated method stub
		super.doCrossover(firstMate, secondMate, a_candidateChromosomes, generator);
		updateNewChromosomes(a_candidateChromosomes,generator);
		
	}

	private void updateNewChromosomes(List a_candidateChromosomes,
			RandomGenerator generator) {
		int candidateSize = a_candidateChromosomes.size();
		IChromosome firstChromosome = (IChromosome)a_candidateChromosomes.get(candidateSize-1);
		IChromosome secondChromosome = (IChromosome)a_candidateChromosomes.get(candidateSize-2);
		Gene[] genesForFirstChromosome = firstChromosome.getGenes();
		GeneCorrector.updateGene(generator, genesForFirstChromosome,numberOfSentencesInSum,numberOfSentencesInDoc);
		
		Gene[] genesForSecondChromosome = secondChromosome.getGenes();
		GeneCorrector.updateGene(generator, genesForSecondChromosome,numberOfSentencesInSum,numberOfSentencesInDoc);
		
	}

	

}
