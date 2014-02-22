package com.can.cluster.sentence.chooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.interfaces.ClusterChooseSentenceStrategy;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.clustering.HAC.Cluster;

public class MaxUniqueWordChooser implements ClusterChooseSentenceStrategy {

	private static final Logger LOGGER = Logger.getLogger(MaxUniqueWordChooser.class);
	
	@Override
	public List<Integer> createSentence(List<Cluster> clusterList,
			Document document) {
		List<Integer> indexList=new ArrayList<Integer>();
		HashMap <Integer,List<Integer>> clusterIndex=new HashMap<Integer, List<Integer>>(clusterList.size());
		for(int i=0; i < clusterList.size();i++){
			clusterIndex.put(i, getOrderedList(clusterList.get(i),document.getSentenceList()));		
		}
		
		int index=0;
		while(indexIsNotOutOfBoundsForAllClusters(clusterList,index)){
			
			for(int i=0;i<clusterList.size();i++){
				Cluster currentCluster = clusterList.get(i);
				if(index<currentCluster.clusterSize()){
					indexList.add(clusterIndex.get(i).get(index));
				}
			}
			index++;
			
		}
		return indexList;
	}
	private boolean indexIsNotOutOfBoundsForAllClusters(List<Cluster> clusterList, int index) {
		boolean retVal=false;
		for (Cluster cluster : clusterList) {
			if(index<cluster.clusterSize()){
				return true;
			}
		}
		return retVal;
	}
	private List<Integer> getOrderedList(Cluster cluster, List<Sentence> list) {
		List<IndexSentenceItem> indexes=new ArrayList<IndexSentenceItem>();
		for (int i=0;i<cluster.clusterSize();i++) {
			indexes.add( new IndexSentenceItem(cluster.getItem(i),list.get(cluster.getItem(i))) );
		}
		Collections.sort(indexes);
		Collections.reverse(indexes);
		LOGGER.info(indexes);
		List<Integer> indexesAsList=new ArrayList<Integer>();
		for (IndexSentenceItem item : indexes) {
			indexesAsList.add(item.index);
		}
		return indexesAsList;
	}
	
	private class IndexSentenceItem implements Comparable<IndexSentenceItem>{
		final int index;
		final Sentence sentence;
		public IndexSentenceItem(int index, Sentence sentence) {
			super();
			this.index = index;
			this.sentence = sentence;
		}
		@Override
		public int compareTo(IndexSentenceItem o) {
			if(this.sentence.getUniqueWordNumber()<o.sentence.getUniqueWordNumber()){
				return -1;
			}else if(this.sentence.getUniqueWordNumber()>o.sentence.getUniqueWordNumber()){
				return 1;
			}
			
			return 0;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "IndexSentenceItem [index=" + index + ", sentence="
					+ sentence + "]";
		}
		
		
	}

}
