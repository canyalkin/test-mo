package com.can.cluster.handling;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.NormalisedGoogleDistance;

@Component("NGD")
public class NGDSimilarityForCluster implements ICalculateSimilarity{

	private static final Logger LOGGER = Logger.getLogger(NGDSimilarityForCluster.class);
	@Override
	public double[][] calculateSimilarity(Document document) {
		if(document.getStructuralProperties()==null){
			document.createStructuralProperties();
		}
		int numberOfSentence=document.getSentenceList().size();
		double[][] simMatrix = createMatrix(numberOfSentence);
		for(int i = 0; i < numberOfSentence; i++){
			for(int j = 0; j < numberOfSentence; j++){
				if(i!=j){
					double sim=NormalisedGoogleDistance.ngd(document.getSentenceList().get(i),document.getSentenceList().get(j),document);
					if(sim==0){
						sim=Double.MAX_VALUE;
					}else{//single link küçük deðerleri yakýn olarak hesaplar ama cosinus de büyük deðerler yakýn demektir.
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
	private double[][] createMatrix(int numberOfSentence) {
		double [][]simMatrix=new double[numberOfSentence][];
		for(int i=0;i<numberOfSentence;i++){
			simMatrix[i]=new double[numberOfSentence];
			for(int j=0;j<numberOfSentence;j++){
				simMatrix[i][j]=0.0;
			}
		}
		return simMatrix;
	}

}
