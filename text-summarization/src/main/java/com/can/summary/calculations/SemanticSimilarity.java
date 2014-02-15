package com.can.summary.calculations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.model.Sentence;
import com.can.summary.module.GASummaryStrategyImpl;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class SemanticSimilarity {
	private static final Logger LOGGER = Logger.getLogger(SemanticSimilarity.class);
	private static ILexicalDatabase db;
	private static RelatednessCalculator rc;
	private static HashMap<String, Double> cache;
	static{
		cache=new HashMap<String, Double>(10000);
		db = new NictWordNet();
		rc= new WuPalmer(db);
		WS4JConfiguration.getInstance().setMFS(true);
	}
	private SemanticSimilarity(){
	}

	public static double calculate(String word1,String word2){
		
		String key = word1+word2;
		String key2=word2+word1;
		if(cache.containsKey(key)){
			return cache.get(key);
		}else if(cache.containsKey(key2)){
			return cache.get(key2);
		}
		
		List<POS[]> posPairs = rc.getPOSPairs();
		double maxScore = -1D;
		for(POS[] posPair: posPairs) {
			List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(word1, posPair[0].toString());
			List<Concept> synsets2 = (List<Concept>)db.getAllConcepts(word2, posPair[1].toString());

			for(Concept synset1: synsets1) {
				for (Concept synset2: synsets2) {
					Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
					double score = relatedness.getScore();
					if (score > maxScore) { 
						maxScore = score;
					}
				}
			}
		}

		if (maxScore == -1D) {
			maxScore = 0.0;
		}
		
		cache.put(key, maxScore);
		cache.put(key2, maxScore);
		return maxScore;
	}

	public static double calculate(Sentence sentence,Sentence sentence2){
		List<String> nounBase=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(nounBase, sentence, "NN");
		FrequencyCalculator.addWordsToListWrtPos(nounBase, sentence2, "NN");
		
		List<String> nouns1=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(nouns1, sentence, "NN");
		List<String> nouns2=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(nouns2, sentence2, "NN");
		List<Double>list1=new ArrayList<Double>();
		List<Double>list2=new ArrayList<Double>();
		fillArray(nounBase,nouns1,nouns2,list1,list2);
		double cosValueNoun = CosineSimilarity.calculate(list1, list2);
		
		
		List<String> verbBase=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(verbBase, sentence, "VB");
		FrequencyCalculator.addWordsToListWrtPos(verbBase, sentence2, "VB");
		List<String> verbs1=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(verbs1, sentence, "VB");
		List<String> verbs2=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(verbs2, sentence2, "VB");
		List<Double>verbList1=new ArrayList<Double>();
		List<Double>verbList2=new ArrayList<Double>();
		fillArray(verbBase,verbs1,verbs2,verbList1,verbList2);
		
		
		
		
		return cosValueNoun;
	}
	
	
	private static void fillArray(List<String> base, List<String> wordsFor1stSentence,
			List<String> wordsFor2stSentence, List<Double> list1, List<Double> list2) {
		
		for (String vectorNoun : base) {
			
			double max=0.0;
			for(String noun1:wordsFor1stSentence){//senA
				double val=SemanticSimilarity.calculate(vectorNoun, noun1);
				if(val>max){
					max=val;
				}
			}
			list1.add(max);
			
			max=0.0;
			for(String noun2:wordsFor2stSentence){//senB
				double val=SemanticSimilarity.calculate(vectorNoun, noun2);
				if(val>max){
					max=val;
				}
			}
			list2.add(max);
			
			
		}
		
	}

}
