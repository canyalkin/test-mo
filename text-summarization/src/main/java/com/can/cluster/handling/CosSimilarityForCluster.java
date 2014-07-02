package com.can.cluster.handling;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.CosineSimilarity;
import com.can.summary.calculations.FrequencyCalculator;

@Component("CosSimilarity")
public class CosSimilarityForCluster extends AbstractSimForCluster implements ICalculateSimilarity{

	private static final Logger LOGGER = Logger.getLogger(CosSimilarityForCluster.class);
	
	private HashMap<String, Integer> ft = null ;
	private HashMap<String, Double> idf = null;
	private HashMap<String, Double> tfIdf = null;
	
	
	
	@Override
	public double[][] calculateSimilarity(Document document) {
		int numberOfSentence=document.getSentenceList().size();
		double[][] simMatrix = createMatrix(numberOfSentence);
		ft = FrequencyCalculator.createFrequencyTable(document);
		idf = FrequencyCalculator.calculateInverseSentenceFreqTable(ft, document);
		tfIdf = FrequencyCalculator.createTfIdfTable(ft, idf);
		for(int i = 0; i < numberOfSentence; i++){
			for(int j = 0; j < numberOfSentence; j++){
				if(i!=j){
					double sim=CosineSimilarity.calculate(document.getSentenceList().get(i),document.getSentenceList().get(j),tfIdf);
					LOGGER.debug("cluster cos sim:"+sim);
					if(sim==0){
						sim=Double.MAX_VALUE;
					}else{//single link küçük deðerleri yakýn olarak hesaplar ama cosinus de büyük deðerler yakýn demektir.
						sim=1/sim;
						//sim=1-sim;
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
