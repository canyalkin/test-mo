package com.can.document.handler.module;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.can.document.reader.BulkDocumentReader;
import com.can.reporter.SummaryReport;
import com.can.summarizer.interfaces.IStrategyDirector;
import com.can.summarizer.interfaces.IVisitor;
import com.can.summarizer.interfaces.SummaryStrategy;
import com.can.summarizer.interfaces.Visitable;
import com.can.summarizer.model.AnalysisData;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summarizer.model.Sentence;
import com.can.summary.evaluator.BulkRougeNEvaluator;
import com.can.summary.exceptions.MissingFileException;
import com.can.summary.module.AbstractSummarizer;
import com.can.summary.module.GASummaryStrategyImpl;
import com.can.word.utils.PropertyHandler;
import com.can.word.utils.SummaryUtils;

@Component
public class BulkDocumentHandler implements Visitable{
	
	private static final Logger LOGGER = Logger.getLogger(BulkDocumentHandler.class);

	@Autowired
	private PropertyHandler propertyHandler;
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	private IStrategyDirector strategyDirector;
	
	private Map<String,Double>fitnessValues=null;
	
	private Map<String,AnalysisData> bulkDataAnalysis=null;
	
	private RougeNType rougeNType;
	private int rougeNNumber;
	private double summaryProportion;
	private int maxWordNumber;
	private int popSize;
	private int generationNumber;
	private double xoverRate;
	private double mutationRate;
	private double summaryTime;
	private double clusterNumber;

	private double evaluationTime;

	public BulkDocumentReader doBulkReferenceRead() {
		long t1;
		long t2;
		BulkDocumentReader referenceDocuments = context.getBean(BulkDocumentReader.class);
		t1=System.currentTimeMillis();
		referenceDocuments.doBulkRead(propertyHandler.getRefDocumentFolder(),true);
		t2=System.currentTimeMillis();
		LOGGER.info("bulk read of reference documents  takes:"+(t2-t1)/1000+" seconds");
		return referenceDocuments;
	}
	public Map<String, Document> doBulkSummarization(BulkDocumentReader systemDocuments) {
		long evaluation1=System.currentTimeMillis();
		AbstractSummarizer summarizer;
		Map<String, Document> systemDocMap = systemDocuments.getDocumentMap();
		Map<String, Document> summaryMap=new HashMap<String, Document>(1000);
		Set<String> files = systemDocMap.keySet();
		for (String curFile : files) {
			LOGGER.info("*****cur file: "+curFile);
			List<SummaryStrategy> strategyList = strategyDirector.getSummaryStrategies();
			SummaryStrategy summaryStrategy = null;
			Document document=systemDocMap.get(curFile);
			for (int i = 0; i < strategyList.size(); i++) {
				summaryStrategy=strategyList.get(i);
				document=summaryStrategy.doSummary(document);
				if(summaryStrategy instanceof GASummaryStrategyImpl){
					if(fitnessValues==null){
						fitnessValues=new HashMap<String, Double>(1000);
					}
					if(fitnessValues.containsKey(curFile)){
						fitnessValues.put(curFile,((GASummaryStrategyImpl)summaryStrategy).getFitnessValue()+fitnessValues.get(curFile));
					}else{
						fitnessValues.put(curFile,((GASummaryStrategyImpl)summaryStrategy).getFitnessValue());
					}
					
				}
			}
			summarizer=(AbstractSummarizer) summaryStrategy;
			Document summary=summarizer.finalizeSummaryWithPropertyWordNumber(document);
			summaryMap.put(curFile, summary);
			
		}
		long evaluation2=System.currentTimeMillis();
		summaryTime=(evaluation2-evaluation1)/1000.0;
		return summaryMap;
	}
	public BulkDocumentReader doBulkRead() {
		long freeMemory1=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		BulkDocumentReader systemDocuments = context.getBean(BulkDocumentReader.class);
		long t1=System.currentTimeMillis();
		systemDocuments.doBulkRead(propertyHandler.getDocumentFolder(),false);
		long t2=System.currentTimeMillis();
		long freeMemory2=Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LOGGER.info("bulk read of system documents takes:"+(t2-t1)/1000+" seconds");
		LOGGER.info("memory usage: "+(freeMemory2-freeMemory1)/(1024*1024.0)+" MB");
		return systemDocuments;
	}
	public void doBulkEvaluation(Map<String, Document> orginalDocuments,Map<String, Document> summaryDocuments,
			Map<String, Document> referenceDocuments) {
		long evaluation1=System.currentTimeMillis();
		BulkRougeNEvaluator bulkRougeNEvaluator=new BulkRougeNEvaluator(
				summaryDocuments, referenceDocuments, propertyHandler.getRougeNNumber(), propertyHandler.getRougeNType());
		rougeNType=propertyHandler.getRougeNType();
		rougeNNumber=propertyHandler.getRougeNNumber();
		summaryProportion=propertyHandler.getSummaryProportion();
		maxWordNumber=propertyHandler.getMaxWordNumber();
		popSize=propertyHandler.getPopulationNumber();
		generationNumber=propertyHandler.getGenerationNumber();
		xoverRate=propertyHandler.getCrossoverRate();
		mutationRate=propertyHandler.getMutationRate();
		clusterNumber=propertyHandler.getClusterNumber();
		try {
			Map<String, Double> results = bulkRougeNEvaluator.calculateRougeN();
			updateRougeNResults(results,orginalDocuments,referenceDocuments,summaryDocuments);
		} catch (MissingFileException e) {
			LOGGER.error(e.getMessage());
		}catch (Exception e){
			
		}
		long evaluation2=System.currentTimeMillis();
		evaluationTime=((evaluation2-evaluation1)/1000.0);
	}
	private void updateRougeNResults(Map<String, Double> results, Map<String, Document> orginalDocuments,
			Map<String, Document> referenceDocuments, Map<String, Document> summaryDocuments) {

		if(bulkDataAnalysis==null){
			bulkDataAnalysis=new HashMap<String, AnalysisData>(1000);
		}
		Set<String> evaluatedFiles = results.keySet();
		for (String curFile : evaluatedFiles) {

			if(bulkDataAnalysis.containsKey(curFile)){
				AnalysisData curValue = bulkDataAnalysis.get(curFile);
				curValue.setSummWordNumber(curValue.getSummWordNumber()+SummaryUtils.calculateOriginalSentenceWordNumber(summaryDocuments.get(curFile)));
				curValue.setRougeNValue(results.get(curFile)+curValue.getRougeNValue());
				curValue.setFitnessValue(fitnessValues.get(curFile));
				bulkDataAnalysis.put(curFile, curValue);
			}else{
				AnalysisData analysisData=new AnalysisData(curFile);
				analysisData.setOriginalWordNumber( SummaryUtils.calculateOriginalSentenceWordNumber((orginalDocuments.get(curFile)) ) );
				analysisData.setRefWordNumber(SummaryUtils.calculateOriginalSentenceWordNumber(referenceDocuments.get(curFile)));
				analysisData.setSummWordNumber(SummaryUtils.calculateOriginalSentenceWordNumber(summaryDocuments.get(curFile)));
				analysisData.setRougeNValue(results.get(curFile));
				if(fitnessValues!=null){
					analysisData.setFitnessValue(fitnessValues.get(curFile));
				}
				bulkDataAnalysis.put(curFile, analysisData);
			}
		}
	}
	
	/**
	 * @return the bulkDataAnalysis
	 */
	public Map<String, AnalysisData> getBulkDataAnalysis() {
		return bulkDataAnalysis;
	}
	
	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return LOGGER;
	}
	/**
	 * @return the fitnessValues
	 */
	public Map<String, Double> getFitnessValues() {
		return fitnessValues;
	}
	/**
	 * @return the rougeNType
	 */
	public RougeNType getRougeNType() {
		return rougeNType;
	}
	/**
	 * @return the rougeNNumber
	 */
	public int getRougeNNumber() {
		return rougeNNumber;
	}
	/**
	 * @return the summaryProportion
	 */
	public double getSummaryProportion() {
		return summaryProportion;
	}
	/**
	 * @return the maxWordNumber
	 */
	public int getMaxWordNumber() {
		return maxWordNumber;
	}
	/**
	 * @return the popSize
	 */
	public int getPopSize() {
		return popSize;
	}
	/**
	 * @return the generationNumber
	 */
	public int getGenerationNumber() {
		return generationNumber;
	}
	/**
	 * @return the xoverRate
	 */
	public double getXoverRate() {
		return xoverRate;
	}
	/**
	 * @return the mutationRate
	 */
	public double getMutationRate() {
		return mutationRate;
	}
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
		
	}
	public void clearData() {
		if(fitnessValues!=null){
			fitnessValues.clear();
		}
		bulkDataAnalysis.clear();
		
	}
	public double getSummaryTime() {
		return summaryTime;
	}

	public double getEvaluationTime() {
		return evaluationTime;
	}
	public double getClusterNumber() {
		return clusterNumber;
	}
}
