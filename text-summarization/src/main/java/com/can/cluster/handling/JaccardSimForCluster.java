package com.can.cluster.handling;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.JaccardSimilarity;

@Component("JaccardSimCluster")
public class JaccardSimForCluster extends AbstractSimForCluster implements ICalculateSimilarity {

	private static final Logger LOGGER = Logger.getLogger(JaccardSimForCluster.class);
	
	@Override
	public double[][] calculateSimilarity(Document document) {
		int numberOfSentence=document.getSentenceList().size();
		double[][] simMatrix = createMatrix(numberOfSentence);
		
		for(int i = 0; i < numberOfSentence; i++){
			for(int j = 0; j < numberOfSentence; j++){
				if(i!=j){
					double sim=JaccardSimilarity.calculate(document.getSentenceList().get(i),document.getSentenceList().get(j));
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
	
	
}
