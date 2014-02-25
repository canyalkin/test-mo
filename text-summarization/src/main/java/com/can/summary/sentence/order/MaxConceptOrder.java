package com.can.summary.sentence.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.interfaces.ISynsetFinder;
import com.can.summarizer.interfaces.SentenceOrder;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.SemanticSimilarity;


public class MaxConceptOrder implements SentenceOrder {
	
	@Autowired
	private IPOSTagger tagger;
	
	@Autowired
	ISynsetFinder synsetFinder;
	
	public List<Integer> orderWrtMaxConcept(List<Integer> indexList, Document document ){
		if(!document.isHasPosTag()){
			tagger.createPosTags(document);
		}
		List<SentenceConceptItem> item=new ArrayList<SentenceConceptItem>(indexList.size());
		List<Integer> finalIndex=new ArrayList<Integer>(indexList.size());
		for (Integer curIndex : indexList) {
			List<String> posTags = document.getSentenceList().get(curIndex).getPosTags();
			int i=0;
			int conceptSize=0;
			for (String pos : posTags) {
				if(pos.startsWith("NN")|| pos.startsWith("VB"))
				{
					//conceptSize += SemanticSimilarity.getConcepts((document.getSentenceList().get(curIndex).getWords().get(i).getWord())).size();
					conceptSize +=synsetFinder.findHypernym((document.getSentenceList().get(curIndex).getWords().get(i).getWord()), pos).size();
				}
				i++;
			}
			SentenceConceptItem sci = new SentenceConceptItem(curIndex, conceptSize);
			item.add(sci);
		}
		Collections.sort(item);
		//Collections.reverse(item);
		for (SentenceConceptItem sciItem : item) {
			finalIndex.add(sciItem.index);
		}
		
		return finalIndex;
		
	}
	
	private class SentenceConceptItem implements Comparable<SentenceConceptItem>{
		final int index;
		final int conceptSize;
		public SentenceConceptItem(int index, int conceptSize) {
			
			this.index = index;
			this.conceptSize = conceptSize;
		}
		@Override
		public int compareTo(SentenceConceptItem o) {
			if(this.conceptSize<o.conceptSize){
				return -1;
			}else if(this.conceptSize>o.conceptSize){
				return 1;
			}
			
			return 0;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "SentenceConceptItem [index=" + index + ", conceptSize="
					+ conceptSize + "]";
		}
		
		
		
	}

	@Override
	public List<Integer> orderSentence(List<Integer> indexes, Document document) {
		return orderWrtMaxConcept(indexes, document);
	}

}
