package com.can.summarizer.interfaces;

import java.util.List;

import com.can.summarizer.model.Document;
import com.clustering.HAC.Cluster;

public interface ClusterChooseSentenceStrategy {
	
	List<Integer> createSentence(List<Cluster> clusterList,Document document);

}
