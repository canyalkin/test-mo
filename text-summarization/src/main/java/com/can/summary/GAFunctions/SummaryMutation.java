package com.can.summary.GAFunctions;

import java.util.List;
import java.util.Vector;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneticOperatorConstraint;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.impl.FixedBinaryGene;
import org.jgap.impl.MutationOperator;

public class SummaryMutation extends MutationOperator {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2111284891836963774L;


	public SummaryMutation(Configuration a_conf)
			throws InvalidConfigurationException {
		
		super(a_conf);
		
		setMutationRate(1000);
	}
	
	public SummaryMutation(Configuration a_conf,int mutationRate)
			throws InvalidConfigurationException {
		
		super(a_conf,mutationRate);
		
	}
	
	@Override
	public void operate(Population a_population, List a_candidateChromosomes) {
		if (a_population == null || a_candidateChromosomes == null) {
		      // Population or candidate chromosomes list empty:
		      // nothing to do.
		      // -----------------------------------------------
		      return;
		    }
		    if (getMutationRate() == 0 && getMutationRateCalc() == null) {
		      // If the mutation rate is set to zero and dynamic mutation rate is
		      // disabled, then we don't perform any mutation.
		      // ----------------------------------------------------------------
		      return;
		    }
		    // Determine the mutation rate. If dynamic rate is enabled, then
		    // calculate it using the IUniversalRateCalculator instance.
		    // Otherwise, go with the mutation rate set upon construction.
		    // -------------------------------------------------------------
		    boolean mutate = false;
		    RandomGenerator generator = getConfiguration().getRandomGenerator();
		    // It would be inefficient to create copies of each Chromosome just
		    // to decide whether to mutate them. Instead, we only make a copy
		    // once we've positively decided to perform a mutation.
		    // ----------------------------------------------------------------
		    int size = Math.min(getConfiguration().getPopulationSize(),
		                        a_population.size());
		    IGeneticOperatorConstraint constraint = getConfiguration().
		        getJGAPFactory().getGeneticOperatorConstraint();
		    for (int i = 0; i < size; i++) {
		      IChromosome chrom = a_population.getChromosome(i);
		      Gene[] genes = chrom.getGenes();
		      IChromosome copyOfChromosome = null;
		      // For each Chromosome in the population...
		      // ----------------------------------------
		      for (int j = 0; j < genes.length; j++) {
		        if (getMutationRateCalc() != null) {
		          // If it's a dynamic mutation rate then let the calculator decide
		          // whether the current gene should be mutated.
		          // --------------------------------------------------------------
		          mutate = getMutationRateCalc().toBePermutated(chrom, j);
		        }
		        else {
		          // Non-dynamic, so just mutate based on the the current rate.
		          // In fact we use a rate of 1/m_mutationRate.
		          // ----------------------------------------------------------
		          mutate = (generator.nextInt(getMutationRate()) == 0);
		        }
		        if (mutate) {
		          // Verify that crossover allowed.
		          // ------------------------------
		          /**@todo move to base class, refactor*/
		          if (constraint != null) {
		            List v = new Vector();
		            v.add(chrom);
		            if (!constraint.isValid(a_population, v, this)) {
		              continue;
		            }
		          }
		          // Now that we want to actually modify the Chromosome,
		          // let's make a copy of it (if we haven't already) and
		          // add it to the candidate chromosomes so that it will
		          // be considered for natural selection during the next
		          // phase of evolution. Then we'll set the gene's value
		          // to a random value as the implementation of our
		          // "mutation" of the gene.
		          // ---------------------------------------------------
		          if (copyOfChromosome == null) {
		            // ...take a copy of it...
		            // -----------------------
		            copyOfChromosome = (IChromosome) chrom.clone();
		            // ...add it to the candidate pool...
		            // ----------------------------------
		            a_candidateChromosomes.add(copyOfChromosome);
		            // ...then mutate all its genes...
		            // -------------------------------
		            genes = copyOfChromosome.getGenes();
		          }
		          // Process all atomic elements in the gene. For a StringGene this
		          // would be as many elements as the string is long , for an
		          // IntegerGene, it is always one element.
		          // --------------------------------------------------------------
		         
		            mutateGene(genes,j);
		         
		        }
		      }
		    }
	}

	private void mutateGene(Gene[] genes, int indexOfMutation) {
	
		FixedBinaryGene fixedBinaryGene=(FixedBinaryGene)genes[indexOfMutation];
		int[] alleleValue=(int[])fixedBinaryGene.getAllele();
		if(alleleValue[0]==0){
			fixedBinaryGene.flip(0);
			int index=findNextNumber(genes,indexOfMutation,1);
			fixedBinaryGene=(FixedBinaryGene)genes[index];
			fixedBinaryGene.flip(0);
		}else{
			fixedBinaryGene.flip(0);
			int index=findNextNumber(genes,indexOfMutation,0);
			fixedBinaryGene=(FixedBinaryGene)genes[index];
			fixedBinaryGene.flip(0);
		}
		
		
	}

	private int findNextNumber(Gene[] genes, int indexOfMutation, int nextValueToBeFound) {
		int length=genes.length;
		int index=0;
		indexOfMutation=indexOfMutation+1;
		indexOfMutation=indexOfMutation%length;
		while( ((int [])genes[indexOfMutation].getAllele())[0]!=nextValueToBeFound){
			indexOfMutation=indexOfMutation+1;
			indexOfMutation=indexOfMutation%length;
		}
		
		return indexOfMutation;
	}

	

}
