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


public class MaxWordNumberOrder implements SentenceOrder {

	@Override
	public List<Integer> orderSentence(List<Integer> indexes, Document document) {
		
		List<SentenceWordNumber> sentenceWordNumberList=new ArrayList<SentenceWordNumber>();
		for (Integer index : indexes) {
			int total=document.getSentenceList().get(index).getWords().size();
			sentenceWordNumberList.add(new SentenceWordNumber(total, index));
		}
		
		Collections.sort(sentenceWordNumberList);
		Collections.reverse(sentenceWordNumberList);
		List<Integer> newIndexes=new ArrayList<Integer>();
		
		for (SentenceWordNumber sentenceWordNumberIndex : sentenceWordNumberList) {
			newIndexes.add(sentenceWordNumberIndex.index);
		}
		return newIndexes;
	}
	

	private class SentenceWordNumber implements Comparable<SentenceWordNumber>{
		final int totalWordNumber;
		final int index;
		
		public SentenceWordNumber(int totalTfIdfValue, int index) {
			super();
			this.totalWordNumber = totalTfIdfValue;
			this.index = index;
		}

		@Override
		public int compareTo(SentenceWordNumber arg0) {
			if(this.totalWordNumber<arg0.totalWordNumber){
				return -1;
			}else if(this.totalWordNumber > arg0.totalWordNumber){
				return 1;
			}
			return 0;
		}
		
		
	}

}
