package com.can.summary.calculations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;



public class CosineSimilarity {
	
	private CosineSimilarity() {
	}
	
	public static double calculate(List<Double> list1,List<Double> list2){
		double value=0.0;
		if(list1.size()!=list2.size()){
			return 0.0;
		}
		double numerator=0.0;
		double denominatorA=0.0;
		double denominatorB=0.0;
		for(int i=0;i<list1.size();i++){
			numerator += list1.get(i)*list2.get(i);
			denominatorA += Math.pow(list1.get(i),2);
			denominatorB += Math.pow(list2.get(i),2);
		}
		if (denominatorA != 0.0 && denominatorB != 0.0) {
            value = numerator / (denominatorA * denominatorB);
        } else {
            return 0.0;
        }
		return value;
		
		
	}
	
	public static double calculate(Sentence sentence,Sentence sentence2,Map<String,Double> map){
		double value=0.0;
		
		List<String> wordsForVector=new ArrayList<String>();
		FrequencyCalculator.addWordsToList(wordsForVector, sentence);
		FrequencyCalculator.addWordsToList(wordsForVector, sentence2);
		List<Double> vector1 = new ArrayList<Double>();
		List<Double> vector2 = new ArrayList<Double>();
		fillVectors(wordsForVector,vector1,vector2,sentence,sentence2,map);
		value=calculate(vector1, vector2);
		
		return value;
		
	}
	
	public static double calculate(Sentence sentence,Sentence sentence2){
		double value=0.0;
		
		List<String> wordsForVector=new ArrayList<String>();
		FrequencyCalculator.addWordsToList(wordsForVector, sentence);
		FrequencyCalculator.addWordsToList(wordsForVector, sentence2);
		List<Double> vector1 = new ArrayList<Double>();
		List<Double> vector2 = new ArrayList<Double>();
		fillVectors(wordsForVector,vector1,vector2,sentence,sentence2,null);
		value=calculate(vector1, vector2);
		
		return value;
		
	}
	
	private static void fillVectors(List<String> wordsForVector,
			List<Double> vector1, List<Double> vector2,
			Sentence sentence, Sentence sentence2,Map<String, Double> valueMap) {
		for (String word : wordsForVector) {
			if(sentence.getWordsAsStringList().contains(word)){
				vector1.add(valueMap != null ? valueMap.get(word):count(word,sentence));
			}else{
				vector1.add(0.0);
			}
			
			if(sentence2.getWordsAsStringList().contains(word)){
				vector2.add(valueMap != null ? valueMap.get(word):count(word,sentence2));
			}else{
				vector2.add(0.0);
			}
		}
	}
	
	private static double count(String word, Sentence sentence) {
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
