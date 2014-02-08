package com.can.cluster.handling;

import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.interfaces.ClusterChooseStrategy;
import com.can.summarizer.model.Document;
import com.clustering.HAC.Cluster;
import com.clustering.HAC.Dendrom;

public class ProportionalClusterChooser implements ClusterChooseStrategy {
	private static final Logger LOGGER = Logger.getLogger(SimpleClusterChooseStrategy.class);
	@Override
	public List<Cluster> chooseCluster(Dendrom dendrom, int size,
			Document document) {
		int clusterNumber=(int) Math.sqrt(size/2.0);
		if( !(clusterNumber<size && clusterNumber>1) ){
			clusterNumber=size/2;
			LOGGER.info("actual cluster number:"+clusterNumber);
		}
		return dendrom.getClusterAccordingToClusterNumber(clusterNumber);
	}

}
