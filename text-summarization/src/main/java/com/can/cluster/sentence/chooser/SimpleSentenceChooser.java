package com.can.cluster.sentence.chooser;

import java.util.ArrayList;
import java.util.List;

import com.can.summarizer.interfaces.ClusterChooseSentenceStrategy;
import com.can.summarizer.model.Document;
import com.clustering.HAC.Cluster;

public class SimpleSentenceChooser implements ClusterChooseSentenceStrategy {

	@Override
	public List<Integer> createSentence(List<Cluster> clusterList,Document document) {
		List<Integer> indexes=new ArrayList<Integer>();
		int index=0;
		while(indexIsNotOutOfBoundsForAllClusters(clusterList,index)){
			for(int i = 0 ;i<clusterList.size();i++){
				Cluster cluster=clusterList.get(i);
				if(index<cluster.clusterSize()){
					indexes.add(cluster.getItem(index));
				}
			}
			index++;
		}
		
		return indexes;
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

}
