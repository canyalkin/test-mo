package com.can.summarizer.interfaces;

import java.util.List;

import com.can.summarizer.model.Document;
import com.clustering.HAC.Cluster;
import com.clustering.HAC.Dendrom;

public interface ClusterChooseStrategy {
	
	List<Cluster> chooseCluster(Dendrom dendrom,int size,Document document);

}
