package com.can.cluster.handling;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ClusterChooseStrategy;
import com.can.summarizer.model.Document;
import com.can.summary.module.ClusterStrategy;
import com.can.word.utils.PropertyHandler;
import com.clustering.HAC.Cluster;
import com.clustering.HAC.Dendrom;


public class SimpleClusterChooseStrategy implements ClusterChooseStrategy {

	private static final Logger LOGGER = Logger.getLogger(SimpleClusterChooseStrategy.class);
	@Autowired
	PropertyHandler propertyHandler;
	
	@Override
	public List<Cluster> chooseCluster(Dendrom dendrom, int size,Document document) {
		int clusterNumber=propertyHandler.getClusterNumber();
		if( !(clusterNumber<size && clusterNumber>1) ){
			clusterNumber=size/2;
			LOGGER.info("actual cluster number:"+clusterNumber);
		}
		return dendrom.getClusterAccordingToClusterNumber(clusterNumber);
	}

}
