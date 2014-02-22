package com.can.cluster.chooser;

import java.util.List;

import com.can.summarizer.interfaces.ClusterChooseStrategy;
import com.can.summarizer.model.Document;
import com.clustering.HAC.Cluster;
import com.clustering.HAC.Dendrom;


public class MaxDiffStrategy implements ClusterChooseStrategy {

	
	@Override
	public List<Cluster> chooseCluster(Dendrom dendrom, int size,Document document) {

		double diff=-1.0;
		double maxDiff=0.20;
		int maxDiffIndex=1;
		for(int i=1;i<size-1;i++){
			diff=dendrom.getLevelDistance(i+1)-dendrom.getLevelDistance(i);
			if(diff>=maxDiff){
				maxDiffIndex=i;
				break;
			}
		}
		return dendrom.getClusterAccordingToClusterNumber(size - maxDiffIndex);
	}

}
