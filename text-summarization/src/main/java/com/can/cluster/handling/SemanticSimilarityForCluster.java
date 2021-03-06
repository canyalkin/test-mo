package com.can.cluster.handling;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.FrequencyCalculator;
import com.can.summary.calculations.SemanticSimilarity;

@Component("SemanticSimilarity")
public class SemanticSimilarityForCluster extends AbstractSimForCluster implements ICalculateSimilarity {

	private static final Logger LOGGER = Logger.getLogger(SemanticSimilarityForCluster.class);
	
	@Autowired
	private IPOSTagger tagger;
		
	@Override
	public double[][] calculateSimilarity(Document document) {
		tagger.createPosTags(document);
		HashMap<String, Integer> freqTable;
		if(document.getStructuralProperties()==null){
			freqTable = FrequencyCalculator.createFrequencyTable(document);
		}else{
			freqTable=document.getStructuralProperties().getFreqTable();
		}
		int numberOfSentence=document.getSentenceList().size();
		double[][] simMatrix = createMatrix(numberOfSentence);
		for(int i = 0; i < numberOfSentence; i++){
			for(int j = 0; j < numberOfSentence; j++){
				if(i!=j){
					double sim=SemanticSimilarity.calculate(document.getSentenceList().get(i), document.getSentenceList().get(j));
					if(sim==0){
						sim=Double.MAX_VALUE;
					}else{//single link k���k de�erleri yak�n olarak hesaplar ama cosinus de b�y�k de�erler yak�n demektir.
						sim=1/sim;
					}
					simMatrix[i][j]=sim;
				}
				LOGGER.trace("matrix("+i+")("+j+"):"+simMatrix[i][j]+" ");
			}
			LOGGER.trace("");
		}
		
		
		return simMatrix;
	}

}
