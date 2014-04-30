package com.can.cluster.sentence.chooser;

import java.util.Comparator;

import com.clustering.HAC.Cluster;

public class ClusterComparator implements Comparator<Cluster> {

	@Override
	public int compare(Cluster arg0, Cluster arg1) {
		if(arg0.clusterSize() > arg1.clusterSize()){
			return -1;
		}else if(arg0.clusterSize() < arg1.clusterSize()){
			return 1;
		}
		return 0;
	}

}
