package com.can.summary.GAFunctions;

import org.apache.log4j.Logger;
import org.jgap.Gene;
import org.jgap.RandomGenerator;
import org.jgap.impl.FixedBinaryGene;


public class GeneCorrector {
	
	private static final Logger LOGGER = Logger.getLogger(GeneCorrector.class);
	
	public static void updateGene(RandomGenerator generator,
			Gene[] genes,int numberOfSentencesInSum,int numberOfSentencesInDoc) {
		LOGGER.debug("updateGene Starts...");
		int curSentencesInSum = calculateNumberOfSentencesInSumm(genes);
		LOGGER.debug("curSentencesInSum="+curSentencesInSum);
		if(curSentencesInSum<numberOfSentencesInSum){
			int numberOfChanges=numberOfSentencesInSum-curSentencesInSum;
			numberOfChanges = addMissingSentences(generator, genes,numberOfChanges,numberOfSentencesInDoc);
		}else if(curSentencesInSum>numberOfSentencesInSum){
			int numberOfChanges=curSentencesInSum-numberOfSentencesInSum;
			numberOfChanges = removeUnnecessarySentences(generator, genes,numberOfChanges,numberOfSentencesInDoc);
		}
		curSentencesInSum=calculateNumberOfSentencesInSumm(genes);
		LOGGER.debug("curSentencesInSum="+curSentencesInSum);
		LOGGER.debug("updaGene Finished............");
	}

	private static int removeUnnecessarySentences(RandomGenerator generator,
			Gene[] genes, int numberOfChanges, int numberOfSentencesInDoc) {
		LOGGER.debug("removeUnnecessarySentences,numberOfChanges="+numberOfChanges);
		while(numberOfChanges!=0){
			int randomIndex = generator.nextInt(numberOfSentencesInDoc);
			int[] value = (int[]) genes[randomIndex].getAllele();
			LOGGER.debug("value="+value[0]);
			if(value[0]==1){
				LOGGER.debug("flip");
				FixedBinaryGene binaryGene = (FixedBinaryGene)genes[randomIndex];
				binaryGene.flip(0);
				numberOfChanges--;
			}
		}
		LOGGER.debug("numberOfChanges="+numberOfChanges);
		LOGGER.debug("removeUnnecessarySentences-finished");
		return numberOfChanges;
	}

	private static int addMissingSentences(RandomGenerator generator, Gene[] genes,
			int numberOfChanges, int numberOfSentencesInDoc) {
		LOGGER.debug("addMissingSentences,numberOfChanges="+numberOfChanges);
		while(numberOfChanges!=0){
			int randomIndex = generator.nextInt(numberOfSentencesInDoc);
			int[] value = (int[]) genes[randomIndex].getAllele();
			LOGGER.debug("value="+value[0]);
			if(value[0]==0){
				LOGGER.debug("flip");
				FixedBinaryGene binaryGene = (FixedBinaryGene)genes[randomIndex];
				binaryGene.flip(0);
				numberOfChanges--;
			}
		}
		LOGGER.debug("numberOfChanges="+numberOfChanges);
		LOGGER.debug("addMissingSentences-finished");
		return numberOfChanges;
	}

	private static int calculateNumberOfSentencesInSumm(Gene[] genes) {
		int curSentencesInSum=0;
		LOGGER.debug("calculateNumberOfSentencesInSumm....");
		for (int i=0;i<genes.length;i++) {
			int[] value = (int[]) genes[i].getAllele();
			LOGGER.debug("Gene ("+i+"):"+value[0]);
			if(value[0]==1){
				curSentencesInSum++;
			}
		}
		LOGGER.debug("calculateNumberOfSentencesInSumm.... ENDs");
		return curSentencesInSum;
	}

}
