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
import com.can.summary.calculations.ContentOverlap;
import com.can.summary.calculations.CosineSimilarity;
import com.can.summary.calculations.JaccardSimilarity;
import com.can.summary.calculations.NormalisedGoogleDistance;
import com.can.summary.calculations.SemanticSimilarity;
import com.can.word.utils.PropertyHandler;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.scoring.PageRankWithPriors;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

@Component("TextRankStrategyBean")
public class TextRankStrategy extends AbstractSummarizer {
	private static final Logger LOGGER = Logger.getLogger(TextRankStrategy.class);
	
	private Map<Integer,Number> edgeWeights;
	
	@Autowired
	private IPOSTagger tagger;
	
	@Autowired
	private PropertyHandler propertyHandler;
	
	@Override
	public Document doSummary(Document aDocument) {
		super.doSummary(aDocument);
		edgeWeights=new HashMap<Integer, Number>();
		UndirectedSparseGraph<Integer, Integer> graph=new UndirectedSparseGraph<Integer, Integer>();
		//PageRankWithPriors<Integer, Integer> pageRank=new PageRank<Integer, Integer>(graph,MapTransformer.getInstance(edgeWeights), 0.85);
		

		for(int i=0;i<aDocument.getSentenceList().size();i++)
		{
			graph.addVertex(new Integer(i));
		}
		int edgeCnt=0;
		for(int i=0;i<aDocument.getSentenceList().size();i++){
			for(int j=i+1;j<aDocument.getSentenceList().size();j++){
				graph.addEdge(edgeCnt,i,j);
				double weight=propertyHandler.getiSentenceSimilarity().calculate(i, j, aDocument);
				if(Double.isNaN(weight)){
					LOGGER.error("Nan Value");
					weight=0.0;
				}
				edgeWeights.put(edgeCnt,weight);
				edgeCnt++;
			}
		}
		PageRank<Integer, Integer> pageRank=new PageRank<Integer, Integer>(graph, MapTransformer.getInstance(edgeWeights), 0.85);
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
