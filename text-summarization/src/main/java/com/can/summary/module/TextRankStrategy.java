package com.can.summary.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summarizer.model.Word;
import com.can.summary.calculations.CosineSimilarity;
import com.can.summary.calculations.NormalisedGoogleDistance;
import com.can.summary.calculations.SemanticSimilarity;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.scoring.PageRankWithPriors;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

@Component("TextRankStrategyBean")
public class TextRankStrategy extends AbstractSummarizer {
	private static final Logger LOGGER = Logger.getLogger(TextRankStrategy.class);
	
	private Map<Integer,Number> edgeWeights;
	
	@Autowired
	private IPOSTagger tagger;
	
	@Override
	public Document doSummary(Document aDocument) {
		super.doSummary(aDocument);
		edgeWeights=new HashMap<Integer, Number>();
		//getDocumentToBeSummarized().createStructuralProperties();
		tagger.createPosTags(getDocumentToBeSummarized());
		UndirectedSparseGraph<Integer, Integer> graph=new UndirectedSparseGraph<Integer, Integer>();
		PageRankWithPriors<Integer, Integer> pageRank=new PageRank<Integer, Integer>(graph,MapTransformer.getInstance(edgeWeights), 0.85);

		for(int i=0;i<aDocument.getSentenceList().size();i++)
		{
			graph.addVertex(new Integer(i));
		}
		int edgeCnt=0;
		for(int i=0;i<aDocument.getSentenceList().size();i++){
			for(int j=i+1;j<aDocument.getSentenceList().size();j++){
				graph.addEdge(edgeCnt,i,j);
				//double weight=CosineSimilarity.calculate(aDocument.getSentenceList().get(i), aDocument.getSentenceList().get(j));
				//double weight=NormalisedGoogleDistance.ngd(aDocument.getSentenceList().get(i), aDocument.getSentenceList().get(j), aDocument);
				//LOGGER.info("before-weight");
				double weight=SemanticSimilarity.calculate(aDocument.getSentenceList().get(i), aDocument.getSentenceList().get(j));
				//LOGGER.info("after-weight:"+weight);
				//double weight=calculateContentOverlap(aDocument.getSentenceList().get(i),aDocument.getSentenceList().get(j));
				if(Double.isNaN(weight)){
					LOGGER.error("Nan Value");
				}
				edgeWeights.put(edgeCnt,weight);
				edgeCnt++;
			}
		}
		
		pageRank.setMaxIterations(1);
		pageRank.initialize();
		pageRank.evaluate();
		List<RankIndex> rankIndexList=new ArrayList<RankIndex>();
		for(int i=0;i<aDocument.getSentenceList().size();i++)
		{
			rankIndexList.add(new RankIndex(i, pageRank.getVertexScore(i)));
		}
		Collections.sort(rankIndexList);
		Collections.reverse(rankIndexList);
		List<Integer> index=new ArrayList<Integer>();
		for(int i=0;i<rankIndexList.size();i++){
			index.add(rankIndexList.get(i).index);
		}
		LOGGER.info("index:"+index);
		
		return finalizeSummaryWithPropertyWordNumber(super.createSummaryDocument(getDocumentToBeSummarized(), index));
	}

	private double calculateContentOverlap(Sentence sentence, Sentence sentence2) {
		List<Word> words = sentence.getWords();
		List<Word> sentenceList2 = sentence2.getWords();
		double cnt=0;
		for (Word word : words) {
			if(sentenceList2.contains(word)){
				cnt++;
			}
		}
		cnt = cnt / (Math.log(words.size()) + Math.log10(sentenceList2.size()));
		return cnt;
	}
	private class RankIndex implements Comparable<RankIndex>{
		private final int index;
		private final double rank;
		
		public RankIndex(int index,double rank) {
			this.index=index;
			this.rank=rank;
		}

		@Override
		public int compareTo(RankIndex o) {
			if(rank<o.rank){
				return -1;
			}else if(rank>o.rank){
				return 1;
			}
			return 0;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "RankIndex [index=" + index + ", rank=" + rank + "]";
		}
		
	}
}
