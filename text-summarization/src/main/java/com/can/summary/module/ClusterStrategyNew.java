package com.can.summary.module;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.ClusterChooseSentenceStrategy;
import com.can.summarizer.interfaces.ClusterChooseStrategy;
import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.interfaces.IVisitor;
import com.can.summarizer.interfaces.Visitable;
import com.can.summarizer.model.Document;
import com.can.word.utils.PropertyHandler;
import com.clustering.HAC.Cluster;
import com.clustering.HAC.CompleteLink;
import com.clustering.HAC.Dendrom;
import com.clustering.HAC.HAC;
import com.clustering.HAC.SingleLink;

@Component("ClusterStrategyNewBean")
@Scope("singleton")
public class ClusterStrategyNew  extends AbstractSummarizer implements Visitable{

	private static final Logger LOGGER = Logger.getLogger(ClusterStrategy.class);
	
	public ClusterStrategyNew() {
	}
	
	@Autowired
	PropertyHandler propertyHandler;
	
	@Resource(name="simFunctions")
	private List<ICalculateSimilarity> similarityCriterias;
	
	@Resource(name="clusterProportion")
	private List<Double> mergeProportion;
	
	@Autowired
	ClusterChooseStrategy clusterChooseStrategy;
	
	@Autowired
	ClusterChooseSentenceStrategy chooseSentenceStrategy;
	
	@Override
	public Document doSummary(Document aDocument) {
		LOGGER.info("Cluster strategy starts");
		super.doSummary(aDocument);
		LOGGER.debug("abstract summary finished...");
		double [][]simMatrix=createSentenceSimilarityMatrix();
		LOGGER.debug("Similarity Matrix created");
		HAC hac=new HAC(getNumberOfSentences(), simMatrix, new CompleteLink());
		Dendrom dendrom=hac.createCluster();
		LOGGER.debug(dendrom);
		int curClusterNumber=2;
		int wordNumber=0;
		List<Cluster> clusterList;
		List<Integer> indexes = null ;
		while(wordNumber<propertyHandler.getMaxWordNumber()){
			LOGGER.info("current Cluster number:"+curClusterNumber);
			clusterList=dendrom.getClusterAccordingToClusterNumber(curClusterNumber);
			//LOGGER.info("cluster List:"+clusterList);
			LOGGER.info("cluster number:"+clusterList.size());
			indexes = chooseSentenceStrategy.createSentence(clusterList,aDocument);
			wordNumber=calculateWordNumber(indexes,aDocument);
			curClusterNumber++;
		}
		
		LOGGER.info("final indexes:"+indexes);
		return finalizeSummary(super.createSummaryDocument(getDocumentToBeSummarized(), indexes));
	}

	private int calculateWordNumber(List<Integer> indexes, Document aDocument) {
		int wordCount=0;
		for (Integer integer : indexes) {
			wordCount+=aDocument.getSentenceList().get(integer).getOriginalSentencesWordNumber();
		}
		return wordCount;
	}

	private double[][] createSentenceSimilarityMatrix() {
		
		double[][] simMatrix=null;
		int i=0;
		for (ICalculateSimilarity similarity : similarityCriterias) {
			double[][] curMatrix = similarity.calculateSimilarity(getDocumentToBeSummarized());
			simMatrix=applyProportion(simMatrix,curMatrix,i++);
			LOGGER.trace("proportion applied:"+simMatrix);
		}
		return simMatrix;
	}


	private double[][] applyProportion(double[][] simMatrix, double[][] curMatrix,
			int i) {

		Double propValue = mergeProportion.get(i);
		for (int j = 0; j< getNumberOfSentences();j++) {
			for (int k=0; k<getNumberOfSentences();k++) {
				curMatrix[j][k]=curMatrix[j][k]*propValue;
			} 
		}
		if(simMatrix==null){
			simMatrix=curMatrix;
		}else{
			for (int j = 0; j< getNumberOfSentences();j++) {
				for (int k=0; k<getNumberOfSentences();k++) {
					simMatrix[j][k]=simMatrix[j][k]+curMatrix[j][k];
				} 
			}
		}
		return simMatrix;
	}


	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
}
