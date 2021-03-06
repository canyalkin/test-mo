package com.can.summary.calculations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;

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
	static{
		WS4JConfiguration.getInstance().setStem(false);
		WS4JConfiguration.getInstance().setMFS(true);
		WS4JConfiguration.getInstance().setCache(true);
		db = new NictWordNet();
		rc= new WuPalmer(db);
	}
	private SemanticSimilarity(){
	}

	public static double calculate(String word1,String word2){
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
		
		return maxScore;
	}
	
	public static double calculate(String word1,String word2,String pos){
		if(pos.startsWith("NN")){
			word1=word1+"#n";
			word2=word2+"#n";
		}else if(pos.startsWith("VB")){
			word1=word1+"#v";
			word2=word2+"#v";
		}
		double maxScore=rc.calcRelatednessOfWords(word1, word2);
		if(maxScore<0 || maxScore>1){
			LOGGER.error("error on calculate semantic similarity- word1: "+word1+" word2: "+word2+"score: "+maxScore);
			maxScore=maxScore/Double.MAX_VALUE;
			LOGGER.error("valu normalized to: "+maxScore);
		}
		/*double maxScore = -1D;
		
			List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(word1, pos);
			List<Concept> synsets2 = (List<Concept>)db.getAllConcepts(word2, pos);

			for(Concept synset1: synsets1) {
				for (Concept synset2: synsets2) {
					Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
					double score = relatedness.getScore();
					if (score > maxScore) { 
						maxScore = score;
					}
				}
			}
		

		if (maxScore == -1D) {
			maxScore = 0.0;
		}*/
		
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
		fillArray(nounBase,nouns1,nouns2,list1,list2,"NN");
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
		fillArray(verbBase,verbs1,verbs2,verbList1,verbList2,"VB");
		double cosValueVerb = CosineSimilarity.calculate(verbList1, verbList2);
		
		
		
		
		return 0.5*cosValueNoun + 0.5*cosValueVerb;
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
	
	private static void fillArray(List<String> base, List<String> wordsFor1stSentence,
			List<String> wordsFor2stSentence, List<Double> list1, List<Double> list2,String pos) {
		
		
		for (String vectorNoun : base) {
			
			double max=0.0;
			for(String noun1:wordsFor1stSentence){//senA
				double val=SemanticSimilarity.calculate(vectorNoun, noun1,pos);
				if(val>max){
					max=val;
				}
			}
			list1.add(max);
			
			max=0.0;
			for(String noun2:wordsFor2stSentence){//senB
				double val=SemanticSimilarity.calculate(vectorNoun, noun2,pos);
				if(val>max){
					max=val;
				}
			}
			list2.add(max);
			
			
		}
		
	}
	
	public static List<Concept> getConcepts(String word){
		
		List<POS[]> posPairs = rc.getPOSPairs();
		List<Concept> concepts=new ArrayList<Concept>();
		for(POS[] posPair: posPairs) {
			try{
				List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(word, posPair[0].toString());
				concepts.addAll(synsets1);
			}catch(Exception exception){
				LOGGER.error(exception.getMessage());
			}
		}
		return concepts;
	}
	
	public static Collection<String> getHypernymConcepts(String word){
		
		Collection<String> hypernyms=new ArrayList<String>();
		try{
			hypernyms = db.getHypernyms(word);
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
		}
		return hypernyms;
	}

	public static double calculate(Sentence sentence1, Sentence sentence2,
			Document document) {
		
		int ms=calculateMS(document.getSentenceList().size());
		List<String> nounBase=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(nounBase, sentence1, "NN",ms,document);
		FrequencyCalculator.addWordsToListWrtPos(nounBase, sentence2, "NN",ms,document);
		
		List<String> nouns1=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(nouns1, sentence1, "NN",ms,document);
		List<String> nouns2=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(nouns2, sentence2, "NN",ms,document);
		List<Double>list1=new ArrayList<Double>();
		List<Double>list2=new ArrayList<Double>();
		fillArray(nounBase,nouns1,nouns2,list1,list2);
		double cosValueNoun = CosineSimilarity.calculate(list1, list2);
		
		
		List<String> verbBase=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(verbBase, sentence1, "VB",ms,document);
		FrequencyCalculator.addWordsToListWrtPos(verbBase, sentence2, "VB",ms,document);
		
		List<String> verbs1=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(verbs1, sentence1, "VB",ms,document);
		List<String> verbs2=new ArrayList<String>();
		FrequencyCalculator.addWordsToListWrtPos(verbs2, sentence2, "VB",ms,document);
		List<Double>verbList1=new ArrayList<Double>();
		List<Double>verbList2=new ArrayList<Double>();
		fillArray(verbBase,verbs1,verbs2,verbList1,verbList2);
		double cosValueVerb = CosineSimilarity.calculate(verbList1, verbList2);
		
		return 0.5*cosValueNoun + 0.5*cosValueVerb;
		
	}
	
	private static int calculateMS(int size) {
		if(size<25){
			return (int) (7+(0.1*(25-size)));
		}else if(size>40){
			return (int) (7+(0.1*(size-40)));
		}else{
			return 7;
		}
	}

}
