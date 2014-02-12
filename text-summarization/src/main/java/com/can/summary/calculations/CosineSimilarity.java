package com.can.summary.calculations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;



public class CosineSimilarity {
	
	private CosineSimilarity() {
	}
	
	public static double calculateFeature(Sentence sentence, Sentence sentence2) {
		double sim = 0.0;
		int sumOfProduct = 0;
		double lengthOfS1 = 0.0, lengthOfS2 = 0.0;

		HashMap<String, WordCounts> wordList = getUniqueWords(sentence,
				sentence2);
		Collection<WordCounts> listOfValues = wordList.values();
		Iterator<WordCounts> wordIterator = listOfValues.iterator();
		while (wordIterator.hasNext()) {
			WordCounts w = wordIterator.next();
			
			sumOfProduct = sumOfProduct + w.word1 * w.word2;
			lengthOfS1 = lengthOfS1 + w.word1 * w.word1;
			lengthOfS2 = lengthOfS2 + w.word2 * w.word2;
		}
		if(lengthOfS1==0 || lengthOfS2 == 0){
			return 0;
		}
		sim = ((double) sumOfProduct)
				/ (Math.sqrt(lengthOfS1) * Math.sqrt(lengthOfS2));
		return sim;
	}
	
	private static HashMap<String, WordCounts> getUniqueWords(
			Sentence sentence, Sentence sentence2) {
		
		HashMap<String, WordCounts> wordList = new HashMap<String, WordCounts>(
				sentence.getWords().size() * 2);
		wordList = createWordListForSimilarity(sentence, sentence2, wordList,//1. cümlede olup da 2. cümlede olmayan kelimeler
				true);
		wordList = createWordListForSimilarity(sentence2, sentence, wordList,//2.cümlede olup da 1de olmayanlar için 
				false);
		return wordList;
	}
	
	private static HashMap<String, WordCounts> createWordListForSimilarity(
			Sentence sentence, Sentence sentence2,
			HashMap<String, WordCounts> wordList, boolean baseSentenceOne) {
		
		int i;
		for (i = 0; i < sentence.getWords().size(); i++) {
			if (!wordList.containsKey(sentence.getWords().get(i).getWord())) {
				
				WordCounts wordCounter = new WordCounts();
				if (baseSentenceOne) {
					wordCounter.word1 = countWordInSentences(sentence.getWords().get(i).getWord(), sentence.getWords());
					wordCounter.word2 = countWordInSentences(sentence.getWords().get(i).getWord(), sentence2.getWords());
				} else {
					wordCounter.word2 = countWordInSentences(sentence.getWords().get(i).getWord(), sentence.getWords());
					wordCounter.word1 = countWordInSentences(sentence.getWords().get(i).getWord(), sentence2.getWords());
				}
				wordList.put(sentence.getWords().get(i).getWord(), wordCounter);
			}
		}
		return wordList;
	}
	
	private static int countWordInSentences(String word, List<Word> wordsInSentence) {

		int cnt = 0;
		for (int i = 0; i < wordsInSentence.size(); i++) {
			if (word.compareToIgnoreCase(wordsInSentence.get(i).getWord()) == 0) {
				cnt++;
			}
		}

		return cnt;
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
		if (denominatorA != 0.0 | denominatorB != 0.0) {
            value = numerator / (denominatorA * denominatorB);
        } else {
            return 0.0;
        }
		return value;
		
		
	}



}
