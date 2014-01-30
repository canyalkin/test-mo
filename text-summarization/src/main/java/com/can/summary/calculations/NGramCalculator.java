package com.can.summary.calculations;

import java.util.LinkedList;
import java.util.List;

import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summarizer.model.Sentence;


public class NGramCalculator {

	private NGramCalculator() {
	}
	
	public static LinkedList<String> findNGram(int n,Sentence sentence,RougeNType rougeNType){
		
		if(rougeNType==RougeNType.wordBased){
			return findWordBasedNGram(n, sentence);
		}else if(rougeNType==RougeNType.charBased){
			return findCharBasedNGram(n, sentence.getWordsAsString());
		}else{
			return null;
		}
		
	}

	private static LinkedList<String> findWordBasedNGram(int n, Sentence sentence) {

		List<String> wordListAsString = sentence.getWordsAsStringList();

		LinkedList<String> ngramOfSentece = new LinkedList<String>();
		StringBuffer nWords = null;
		if (n < 1)
			return null;

		LinkedList<String> listOfPrevNGrams = findWordBasedNGram(n - 1, sentence);
		if (listOfPrevNGrams != null)
			ngramOfSentece.addAll(listOfPrevNGrams);


		int wordNumber = wordListAsString.size();
		for (int i = 0; i < wordNumber - n + 1; i++) {
			nWords = new StringBuffer();
			for (int j = 0; j < n; j++) {
				nWords.append(wordListAsString.get(i + j));
			}
			ngramOfSentece.add(nWords.toString());

		}

		return ngramOfSentece;
	}

	private static LinkedList<String> findCharBasedNGram(int n, String sentence) {


		LinkedList<String> ngramOfSentece = new LinkedList<String>();
		StringBuffer nWords = null;
		if (n < 1)
			return null;

		LinkedList<String> listOfPrevNGrams = findCharBasedNGram(n - 1, sentence);
		if (listOfPrevNGrams != null)
			ngramOfSentece.addAll(listOfPrevNGrams);

		int wordNumber = sentence.length();
		for (int i = 0; i < wordNumber - n + 1; i++) {
			nWords = new StringBuffer();
			for (int j = 0; j < n; j++) {
				nWords.append(sentence.charAt(i + j));
			}
			ngramOfSentece.add(nWords.toString());
		}

		return ngramOfSentece;
	}

	public static void createNGramForDocument(Document document,RougeNType rougeNType,int n) {
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LinkedList<String> nGramList = findNGram(n, sentence, rougeNType);
			sentence.setNgramList(nGramList);
		}
	}

}
