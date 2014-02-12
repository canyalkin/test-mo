package com.can.cluster.handling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;
import com.can.summary.calculations.CosineSimilarity;
import com.can.summary.calculations.FrequencyCalculator;

@Component("CosSimilarity")
public class CosSimilarity implements ICalculateSimilarity{

	private static final Logger LOGGER = Logger.getLogger(CosSimilarity.class);
	
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
					List<String> wordsForVector=new ArrayList<String>();
					FrequencyCalculator.addWordsToList(wordsForVector, document.getSentenceList().get(i));
					FrequencyCalculator.addWordsToList(wordsForVector, document.getSentenceList().get(j));
					List<Double> vector1 = new ArrayList<Double>();
					List<Double> vector2 = new ArrayList<Double>();
					fillVectors(wordsForVector,vector1,vector2,document.getSentenceList().get(i),document.getSentenceList().get(j));
					//double sim=CosineSimilarity.calculateFeature(document.getSentenceList().get(i), document.getSentenceList().get(j));
					double sim=CosineSimilarity.calculate(vector1, vector2);
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

	private void fillVectors(List<String> wordsForVector,
			List<Double> vector1, List<Double> vector2,
			Sentence sentence, Sentence sentence2) {
		for (String word : wordsForVector) {
			if(sentence.getWordsAsStringList().contains(word)){
				//vector1.add((double)count(word,sentence));
				vector1.add(tfIdf.get(word));
			}else{
				vector1.add(0.0);
			}
			
			if(sentence2.getWordsAsStringList().contains(word)){
				//vector2.add((double)count(word,sentence2));
				vector2.add(tfIdf.get(word));
			}else{
				vector2.add(0.0);
			}
		}
	}
	private double count(String word, Sentence sentence) {
		int count=0;
		List<Word> wordsOfSentence = sentence.getWords();
		for (Word word2 : wordsOfSentence) {
			if(word2.getWord().equals(word)){
				count++;
			}
		}
		return count;
	}

}
