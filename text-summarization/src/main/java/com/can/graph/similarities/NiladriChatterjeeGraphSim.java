package com.can.graph.similarities;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.can.summarizer.interfaces.GraphSimilarity;
import com.can.summarizer.model.Document;


public class NiladriChatterjeeGraphSim implements GraphSimilarity {

	private static final Logger LOGGER = Logger.getLogger(NiladriChatterjeeGraphSim.class);
	
	private double calculateSimilarityForSentences(int i, int j,Document aDocument) {

		Iterator<String> keyIterator = aDocument.getStructuralProperties().getFreqTable().keySet().iterator();
		double nominator=0.0;
		double denominatorLeft=0.0;
		double denominatorRight=0.0;
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			double wI=calculateWeight(key,i,aDocument);
			double wJ=calculateWeight(key,j,aDocument);
			nominator+=wI*wJ;
			denominatorLeft+=wI*wI;
			denominatorRight+=wJ*wJ;
		}
		if(denominatorLeft==0 || denominatorRight == 0){
			LOGGER.error("error on calculating similarity: denominatorLeft==0 || denominatorRight == 0");
			return 0.0;
		}
		return nominator/(Math.sqrt(denominatorLeft) * (Math.sqrt(denominatorRight)) );
	}

	private double calculateWeight(String key, int i,Document aDocument) {
		List<Double> termFreqListOfIndexTerm = aDocument.getStructuralProperties().getTfTable().get(key);
		Double tfValue = termFreqListOfIndexTerm.get(i);
		Double isfValue = aDocument.getStructuralProperties().getIsf().get(key);
		return tfValue*isfValue;
	}

	@Override
	public double calculateSimilarity(int index1, int index2, Document document) {
		return calculateSimilarityForSentences(index1,index2,document);
	}

}
