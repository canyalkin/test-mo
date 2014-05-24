package com.can.cluster.sentence.chooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.log4j.Logger;

import com.can.summarizer.interfaces.ClusterChooseSentenceStrategy;
import com.can.summarizer.model.Document;
import com.can.summary.calculations.ContentOverlap;
import com.clustering.HAC.Cluster;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class PageRankChooser implements ClusterChooseSentenceStrategy {
	
	private static final Logger LOGGER = Logger.getLogger(PageRankChooser.class);

	@Override
	public List<Integer> createSentence(List<Cluster> clusterList,
			Document document) {
		
		Collections.sort(clusterList, new ClusterComparator());
		//Collections.reverseOrder(new ClusterComparator());
		LOGGER.info("cluster List after sorting :"+clusterList);
		List<Integer> index=new ArrayList<Integer>();
		
		List<List<Integer>> indexesFromCluster=new ArrayList<List<Integer>>();
		int maxSize=0;
		for (Cluster cluster : clusterList) {
			List<Integer> indexWrtRank=createIndex(cluster,document);
			if(indexWrtRank.size()>maxSize){
				maxSize = indexWrtRank.size();
			}
			indexesFromCluster.add(indexWrtRank);
		}
		
		int baseIndex=0;
		for(int size=0;size<maxSize;size++){
			for(int i=0;i<indexesFromCluster.size();i++){
				if(indexesFromCluster.get(i).size() > baseIndex){
					int sentenceIndex=indexesFromCluster.get(i).get(baseIndex);
					index.add(sentenceIndex);
				}
			}
			baseIndex++;
		}
		
		return index;
	}

	private List<Integer> createIndex(Cluster cluster, Document document) {
		List<Integer> index=new ArrayList<Integer>();
		if(cluster.clusterSize()==1){
			index.add(cluster.getItem(0));
			return index;
		}else if(cluster.clusterSize()==0){
			return index;
		}
		Map<Integer,Number> edgeWeights;
		edgeWeights=new HashMap<Integer, Number>();
		UndirectedSparseGraph<Integer, Integer> graph=new UndirectedSparseGraph<Integer, Integer>();

		for(int i=0;i<cluster.clusterSize();i++)
		{
			graph.addVertex(new Integer(i));
		}
		int edgeCnt=0;
		for(int i=0;i<cluster.clusterSize();i++){
			for(int j=i+1;j<cluster.clusterSize();j++){
				graph.addEdge(edgeCnt,i,j);
				double weight=ContentOverlap.calculate(document.getSentenceList().get(cluster.getItem(i)), document.getSentenceList().get(cluster.getItem(j)));
				if(Double.isNaN(weight)){
					//LOGGER.error("Nan Value");
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
		
		TreeSet<RankIndex> treeRankIndex=new TreeSet<RankIndex>(new RankIndexComparator());
		for(int i=0;i<cluster.clusterSize();i++)
		{
			treeRankIndex.add(new RankIndex(cluster.getItem(i), pageRank.getVertexScore(i)));
		}
		Iterator<RankIndex> it = treeRankIndex.descendingIterator();
		while(it.hasNext()){
			RankIndex item = it.next();
			index.add(item.index);
		}
		return index;
	}
	
	private class RankIndex implements Comparable<RankIndex>{
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + index;
			long temp;
			temp = Double.doubleToLongBits(rank);
			result = prime * result + (int) (temp ^ (temp >>> 32));
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
			if (Double.doubleToLongBits(rank) != Double
					.doubleToLongBits(other.rank))
				return false;
			return true;
		}

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

		private PageRankChooser getOuterType() {
			return PageRankChooser.this;
		}
		
	}
	private class RankIndexComparator implements Comparator<RankIndex>{

		@Override
		public int compare(RankIndex o1, RankIndex o2) {
			if(o1.rank<o2.rank){
				return -1;
			}else if(o1.rank>o2.rank){
				return 1;
			}
			return 0;
		}
		
	}

	private class IndexWordNumber {
	
		private final int index;
		private final int wordNumber;
		
		
		public IndexWordNumber(int index, int wordNumber) {
			super();
			this.index = index;
			this.wordNumber = wordNumber;
		}
		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}
		
		/**
		 * @return the wordNumber
		 */
		public int getWordNumber() {
			return wordNumber;
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
			result = prime * result + wordNumber;
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
			IndexWordNumber other = (IndexWordNumber) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (index != other.index)
				return false;
			if (wordNumber != other.wordNumber)
				return false;
			return true;
		}
		private PageRankChooser getOuterType() {
			return PageRankChooser.this;
		}
	}
	
	private class IndexWordNumberComparator implements Comparator<IndexWordNumber>{

		@Override
		public int compare(IndexWordNumber o1, IndexWordNumber o2) {
			if(o1.wordNumber<o2.wordNumber){
				return -1;
			}else if(o1.wordNumber>o2.wordNumber){
				return 1;
			}
			return 0;
		}
		
	}

}
