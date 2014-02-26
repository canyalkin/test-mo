package com.can.summary.sentence.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.SentenceOrder;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;


public class TfIdfOrder implements SentenceOrder {

	@Override
	public List<Integer> orderSentence(List<Integer> indexes, Document document) {
		
		List<SentenceTfIdf> sentenceTfIdfList=new ArrayList<SentenceTfIdf>();
		for (Integer index : indexes) {
			double total=calculateTotalTfIdf(document.getSentenceList().get(index),document.getStructuralProperties().getTfIdf());
			sentenceTfIdfList.add(new SentenceTfIdf(total, index));
		}
		
		Collections.sort(sentenceTfIdfList);
		Collections.reverse(sentenceTfIdfList);
		List<Integer> newIndexes=new ArrayList<Integer>();
		
		for (SentenceTfIdf sentenceTfIdf : sentenceTfIdfList) {
			newIndexes.add(sentenceTfIdf.index);
		}
		
		
		return newIndexes;
	}
	
	private double calculateTotalTfIdf(Sentence sentence,
			HashMap<String, Double> tfIdf) {
		
		List<Word> wordList = sentence.getWords();
		double total=0.0;
		for (Word word : wordList) {
			total+=tfIdf.get(word.getWord());
		}
		
		return total;
	}

	private class SentenceTfIdf implements Comparable<SentenceTfIdf>{
		final double totalTfIdfValue;
		final int index;
		
		public SentenceTfIdf(double totalTfIdfValue, int index) {
			super();
			this.totalTfIdfValue = totalTfIdfValue;
			this.index = index;
		}

		@Override
		public int compareTo(SentenceTfIdf arg0) {
			if(this.totalTfIdfValue<arg0.totalTfIdfValue){
				return -1;
			}else if(this.totalTfIdfValue > arg0.totalTfIdfValue){
				return 1;
			}
			return 0;
		}
		
		
	}

}
