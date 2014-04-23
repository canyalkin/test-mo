package com.can.summarizer.sentence.sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.can.summarizer.feature.vector.AverageTFFeature;
import com.can.summarizer.interfaces.ISentenceSimilarity;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;
import com.can.summary.calculations.ContentOverlap;
import com.can.summary.calculations.FrequencyCalculator;
import com.can.summary.calculations.SemanticSimilarity;

@Component("ContentOverlapWithSemanticsSim")
public class ContentOverlapWithSemanticsSim implements ISentenceSimilarity {
	private static final Logger LOGGER = Logger.getLogger(ContentOverlapWithSemanticsSim.class);

	@Override
	public double calculate(int i, int j, Document document) {
		if(document.getStructuralProperties()==null){
			document.createStructuralProperties();
		}
		HashMap<String, Double> idfTable = document.getStructuralProperties().getIsf();
		
		Sentence sentence=document.getSentenceList().get(i);
		Sentence sentence2=document.getSentenceList().get(j);
		
		List<String> nonOverlapList1 = new ArrayList<String>(getNonOverlapWords(sentence,sentence2));
		List<String> nonOverlapList2 = new ArrayList<String>(getNonOverlapWords(sentence2,sentence));
		
		double semanticRelation=0.0;
		
		if( nonOverlapList1.size()!=0 && nonOverlapList2.size()!=0){
			semanticRelation = calculateSemanticRelation(idfTable,
					nonOverlapList1, nonOverlapList2);
		}else{
			LOGGER.info("there are no nonoverlapping words for sentences "+i+" & "+j);
		}
		
		
		return ContentOverlap.calculate(sentence, sentence2)+semanticRelation;
	}

	private double calculateSemanticRelation(
			HashMap<String, Double> idfTable,
			List<String> nonOverlapList1, List<String> nonOverlapList2) {
		double semanticRelation=0.0;
		updateListWrtFreq(nonOverlapList1,idfTable);
		updateListWrtFreq(nonOverlapList2,idfTable);
		
		for (String word : nonOverlapList1) {
			for (String word2 : nonOverlapList2) {
				semanticRelation+=SemanticSimilarity.calculate(word, word2);
			}
		}
		
		semanticRelation=semanticRelation/(nonOverlapList1.size()+nonOverlapList2.size());
		return semanticRelation;
	}

	private void updateListWrtFreq(List<String> nonOverlapList1,
			HashMap<String, Double> idfTable) {

		LOGGER.debug("list snapshot:"+nonOverlapList1);
		LOGGER.debug("initial list size:"+nonOverlapList1.size());
		Iterator<String> it = nonOverlapList1.iterator();
		List<WordIsf> wordIsfList=new ArrayList<WordIsf>();
		while(it.hasNext()){
			String word=it.next();
			wordIsfList.add(new WordIsf(word, idfTable.get(word)));
		}
		Collections.sort(wordIsfList);
		Collections.reverse(wordIsfList);
		nonOverlapList1.clear();
		for(int i=0;i<wordIsfList.size() && i<3;i++){
			nonOverlapList1.add(wordIsfList.get(i).getWord());
		}
		LOGGER.debug("final list size:"+nonOverlapList1.size());
	}

	private HashSet<String> getNonOverlapWords(Sentence sentence,
			Sentence sentence2) {
		HashSet<String> resultList=new HashSet<String>();
		List<Word> wordList = sentence.getWords();
		for (Word word : wordList) {
			if(!sentence2.getWords().contains(word)){
				resultList.add(word.getWord());
			}
		}
		return resultList;
	}
	
	private class WordIsf implements Comparable<WordIsf>{
		private final String word;
		private final double isf;
		public WordIsf(String word, double isf) {
			super();
			this.word = word;
			this.isf = isf;
		}
		@Override
		public int compareTo(WordIsf o) {
			if(this.isf<o.isf){
				return -1;
			}else if(this.isf>o.isf){
				return 1;
			}
			return 0;
		}
		public String getWord() {
			return word;
		}
	}

}
