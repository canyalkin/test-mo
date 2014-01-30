package com.can.cluster.handling;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.CosineSimilarity;

@Component("CosSimilarity")
public class CosSimilarity implements ICalculateSimilarity{

	private static final Logger LOGGER = Logger.getLogger(CosSimilarity.class);
	
	@Override
	public double[][] calculateSimilarity(Document document) {
		int numberOfSentence=document.getSentenceList().size();
		double[][] simMatrix = createMatrix(numberOfSentence);
		for(int i = 0; i < numberOfSentence; i++){
			for(int j = 0; j < numberOfSentence; j++){
				if(i!=j){
					double sim=CosineSimilarity.calculateFeature(document.getSentenceList().get(i), document.getSentenceList().get(j));
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
