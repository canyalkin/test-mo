package com.can.cluster.handling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import opennlp.tools.postag.POSTaggerME;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.CosineSimilarity;
import com.can.summary.calculations.FrequencyCalculator;

@Component("CosSimilarity")
public class CosSimilarityForCluster implements ICalculateSimilarity{

	private static final Logger LOGGER = Logger.getLogger(CosSimilarityForCluster.class);
	
	private HashMap<String, Integer> ft = null ;
	private HashMap<String, Double> idf = null;
	private HashMap<String, Double> tfIdf = null;
	
	
	
	@Override
	public double[][] calculateSimilarity(Document document) {
		int numberOfSentence=document.getSentenceList().size();
		double[][] simMatrix = createMatrix(numberOfSentence);
		ft = FrequencyCalculator.createFrequencyTable(document);
		idf = FrequencyCalculator.createIdfTable(ft);
		tfIdf = FrequencyCalculator.createTfIdfTable(ft, idf);
		for(int i = 0; i < numberOfSentence; i++){
			for(int j = 0; j < numberOfSentence; j++){
				if(i!=j){
					double sim=CosineSimilarity.calculate(document.getSentenceList().get(i),document.getSentenceList().get(j),tfIdf);
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
