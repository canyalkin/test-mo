package com.can.summary.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.model.Document;
import com.can.word.utils.PropertyHandler;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
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
		
		for(int i=0;i<aDocument.getSentenceList().size();i++)
		{
			boolean added=graph.addVertex(new Integer(i));
			if(!added){
				LOGGER.error("vertex "+(i)+" cannot be added");
			}
		}
		int edgeCnt=0;
		for(int i=0;i<aDocument.getSentenceList().size();i++){
			for(int j=i+1;j<aDocument.getSentenceList().size();j++){
				boolean added=graph.addEdge(edgeCnt,i,j);
				if(!added){
					LOGGER.error("edge "+(i)+"to "+j+" cannot be added");
				}
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

		List<RankIndex> indexToBeSorted=new ArrayList<TextRankStrategy.RankIndex>();
		for(int i=0;i<aDocument.getSentenceList().size();i++)
		{
			if(!Double.isInfinite(pageRank.getVertexScore(i))){
				boolean inserted=indexToBeSorted.add(new RankIndex(i, pageRank.getVertexScore(i)));
				if(!inserted){
					LOGGER.error("error inserting to treeset index "+i+" rank:"+ pageRank.getVertexScore(i));
				}
			}
		}

		Collections.sort(indexToBeSorted);
		Collections.reverse(indexToBeSorted);
		
		List<Integer> index=new ArrayList<Integer>();
		Iterator<RankIndex> descIt = indexToBeSorted.iterator();
		while(descIt.hasNext()){
			RankIndex item = descIt.next();
			index.add(item.index);
		}
		LOGGER.info("index:"+index);
		
		return finalizeSummary(super.createSummaryDocument(getDocumentToBeSummarized(), index));
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

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + index;
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RankIndex other = (RankIndex) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (index != other.index)
				return false;
			return true;
		}

		private TextRankStrategy getOuterType() {
			return TextRankStrategy.this;
		}

		
		
	}
}
