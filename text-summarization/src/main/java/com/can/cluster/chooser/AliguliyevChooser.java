package com.can.cluster.chooser;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.interfaces.ClusterChooseStrategy;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.FrequencyCalculator;
import com.clustering.HAC.Cluster;
import com.clustering.HAC.Dendrom;

public class AliguliyevChooser implements ClusterChooseStrategy {
	private static final Logger LOGGER = Logger.getLogger(AliguliyevChooser.class);
	@Override
	public List<Cluster> chooseCluster(Dendrom dendrom, int size,
			Document document) {
		double calculatedClusterNo=size/2.0;
		HashMap<String, Integer> ft = FrequencyCalculator.createFrequencyTable(document);//union sayisini verir
		List<Sentence> sentenceList = document.getSentenceList();
		int n=document.getSentenceList().size();
		int wordCount=0;
		for (Sentence sentence : sentenceList) {
			wordCount+=sentence.getWords().size();
		}
		
		calculatedClusterNo=n*(ft.size()/(double)wordCount);
		
		if(calculatedClusterNo>2 && calculatedClusterNo<size){
			return dendrom.getClusterAccordingToClusterNumber((int)calculatedClusterNo);
		}else{
			LOGGER.info("Aliguliyev style not working!!:"+calculatedClusterNo);
			return dendrom.getClusterAccordingToClusterNumber(size/2);
		}
		
	}

}
