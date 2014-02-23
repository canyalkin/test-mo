package com.can.word.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.can.summarizer.interfaces.SummaryStrategy;
import com.can.summarizer.model.RougeNType;


public class PropertyHandler {
	private static final Logger LOGGER = Logger.getLogger(PropertyHandler.class);
	private static PropertyHandler INSTANCE=null;
	public static PropertyHandler getInstance(){
		if(INSTANCE==null){
			INSTANCE=new PropertyHandler();
		}
		return INSTANCE;
	}
	private PropertyHandler() {
		LOGGER.debug(this);
	}
	public void init() {
		try{
			crossoverRate=Double.parseDouble(environment.getProperty("crossover_rate"));
		}catch (Exception e){
			crossoverRate=0.85;
		}
		
		documentFolder=environment.getProperty("docFolder");
		documentName=environment.getProperty("file");
		try{
			generationNumber=Integer.parseInt(environment.getProperty("generation_number"));
		}catch (Exception e){
			generationNumber=20;
		}
		
		try {
			maxWordNumber=Integer.parseInt(environment.getProperty("maxWordNumber"));
		} catch (Exception e) {
			maxWordNumber=100;
		}
		
		
		try{
			mutationRate=Integer.parseInt(environment.getProperty("mutation_rate"));
		}catch (Exception e){
			mutationRate=1000;
		}
		
		outputFolder=environment.getProperty("outputFolder");
		
		
		try{
			populationNumber=Integer.parseInt(environment.getProperty("population_size"));
		}catch (Exception e){
			populationNumber=20;
		}
		
		refDocumentFolder=environment.getProperty("humanSummary");
		
		refDocumentName=environment.getProperty("sum");
		
		rougeNNumber=Integer.parseInt(environment.getProperty("nGramNumber"));
		
		rougeNType=RougeNType.getFromValue(environment.getProperty("nGramType"));
		
		
		String summaryProportion=environment.getProperty("summaryProportion");
		if(summaryProportion==null){
			this.summaryProportion=0.25;
		}else {
			this.summaryProportion = (Double.parseDouble(summaryProportion));
		}
		
		wordNetFolder=environment.getProperty("wordNetFolder");
		
		if(environment.getProperty("title").equals("true") || environment.getProperty("title").equals("yes")){
			title = true;
		}else {
			title = false;
		}
		
		String stemming = (environment.getProperty("stemming"));
		if(stemming==null)
			stemming="true";
		if(stemming.equalsIgnoreCase("true")){
			isStemming=true;
		}else{
			isStemming=false;
		}
		
		String stopWordElimination = (environment.getProperty("stopWordElimination"));
		if(stopWordElimination==null){
			stopWordElimination="true";
		}
		if(stopWordElimination.equalsIgnoreCase("true")){
			this.stopWordElimination=(true);
		}else{
			this.stopWordElimination =(false);
		}
		
		String clusterNumber = environment.getProperty("clusterNumber");
		if(clusterNumber==null || clusterNumber.equals("")){
			setClusterNumber(3);
		}else{
			try{
				setClusterNumber(Integer.parseInt(clusterNumber));
			}catch(NumberFormatException nfe){
				setClusterNumber(3);
			}
		}
		String strategies=environment.getProperty("summaryStrategy");
		setSummaryStrategy(strategies);
		
		String analysisMode=environment.getProperty("analyse_mode");
		if(analysisMode.equals("true")){
			this.analysisMode=true;
		}else{
			this.analysisMode=false;
		}
		
		this.sentenceOrder=environment.getProperty("sentenceOrder");
		//sentenceOrder
		try{
			this.run=Integer.parseInt(environment.getProperty("run"));
		}catch(Exception exception){
			this.run=1;
		}
		
	}
	private void setSummaryStrategy(String strategies) {
		List<SummaryStrategy> summaryStrategies=new ArrayList<SummaryStrategy>();
		String[] strategyArray = strategies.split(",");
		for (String strategy : strategyArray) {
			summaryStrategies.add(applicationContext.getBean(strategy,SummaryStrategy.class));
		}
		setSummaryStrategy(summaryStrategies);
		
	}
	@Autowired
	private Environment environment;
	
	@Autowired 
	private ApplicationContext applicationContext;
		
	private double crossoverRate=0.85;
	private String documentFolder;
	private String documentName;
	private int generationNumber=20;
	private int maxWordNumber=100;
	private int mutationRate=1000;
	private String outputFolder;
	private int populationNumber=20;
	private String refDocumentFolder;
	private String refDocumentName;
	private int rougeNNumber;
	private RougeNType rougeNType;
	private double summaryProportion;
	private String wordNetFolder;
	private boolean title=true;
	private boolean isStemming=true;
	private boolean stopWordElimination=true;
	private boolean analysisMode=false;
	private String  sentenceOrder;
	private int run;
	
	private int clusterNumber;
	private List<SummaryStrategy> summaryStrategy;
	//stop word file burda yok
	
	public boolean isStemming() {
		return isStemming;
	}

	public boolean isStopWordElimination() {
		return stopWordElimination;
	}
	
	public int getRougeNNumber(){
		return rougeNNumber;
	}
	
	public RougeNType getRougeNType(){
		return rougeNType;
	}

	public String getDocumentFolder() {
		return documentFolder;
	}
	
	public String getRefDocumentFolder() {
		return refDocumentFolder;
	}
	
	public double getSummaryProportion(){
		return summaryProportion;
	}
	
	public int getMutationRate(){
		
		return mutationRate;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public int getGenerationNumber() {
		
		return generationNumber;
	}

	public int getPopulationNumber() {
		
		return populationNumber;
	}
	public String getDocumentName(){
		return documentName;
	}
	public String getRefDocumentName(){
		return refDocumentName;
	}
	
	public boolean isTitle(){
		return title;
	}
	
	public String getWordNetFolder(){
		return wordNetFolder;
	}
	
	public int getMaxWordNumber(){
		
		return maxWordNumber;	
	}
	
	public String getOutputFolder(){
		return outputFolder;
	}
	/**
	 * @param crossoverRate the crossoverRate to set
	 */
	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}
	/**
	 * @param documentFolder the documentFolder to set
	 */
	public void setDocumentFolder(String documentFolder) {
		this.documentFolder = documentFolder;
	}
	/**
	 * @param documentName the documentName to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	/**
	 * @param generationNumber the generationNumber to set
	 */
	public void setGenerationNumber(int generationNumber) {
		this.generationNumber = generationNumber;
	}
	/**
	 * @param maxWordNumber the maxWordNumber to set
	 */
	public void setMaxWordNumber(int maxWordNumber) {
		this.maxWordNumber = maxWordNumber;
	}
	/**
	 * @param mutationRate the mutationRate to set
	 */
	public void setMutationRate(int mutationRate) {
		this.mutationRate = mutationRate;
	}
	/**
	 * @param outputFile the outputFile to set
	 */
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	/**
	 * @param populationNumber the populationNumber to set
	 */
	public void setPopulationNumber(int populationNumber) {
		this.populationNumber = populationNumber;
	}
	/**
	 * @param refDocumentFolder the refDocumentFolder to set
	 */
	public void setRefDocumentFolder(String refDocumentFolder) {
		this.refDocumentFolder = refDocumentFolder;
	}
	/**
	 * @param refDocumentName the refDocumentName to set
	 */
	public void setRefDocumentName(String refDocumentName) {
		this.refDocumentName = refDocumentName;
	}
	/**
	 * @param rougeNNumber the rougeNNumber to set
	 */
	public void setRougeNNumber(int rougeNNumber) {
		this.rougeNNumber = rougeNNumber;
	}
	/**
	 * @param rougeNType the rougeNType to set
	 */
	public void setRougeNType(RougeNType rougeNType) {
		this.rougeNType = rougeNType;
	}
	/**
	 * @param summaryProportion the summaryProportion to set
	 */
	public void setSummaryProportion(double summaryProportion) {
		this.summaryProportion = summaryProportion;
	}
	/**
	 * @param wordNetFolder the wordNetFolder to set
	 */
	public void setWordNetFolder(String wordNetFolder) {
		this.wordNetFolder = wordNetFolder;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(boolean title) {
		this.title = title;
	}
	/**
	 * @param isStemming the isStemming to set
	 */
	public void setStemming(boolean isStemming) {
		this.isStemming = isStemming;
	}
	/**
	 * @param stopWordElimination the stopWordElimination to set
	 */
	public void setStopWordElimination(boolean stopWordElimination) {
		this.stopWordElimination = stopWordElimination;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PropertyHandler [crossoverRate=" + crossoverRate
				+ ", documentFolder=" + documentFolder + ", documentName="
				+ documentName + ", generationNumber=" + generationNumber
				+ ", maxWordNumber=" + maxWordNumber + ", mutationRate="
				+ mutationRate + ", outputFolder=" + outputFolder
				+ ", populationNumber=" + populationNumber
				+ ", refDocumentFolder=" + refDocumentFolder
				+ ", refDocumentName=" + refDocumentName + ", rougeNNumber="
				+ rougeNNumber + ", rougeNType=" + rougeNType
				+ ", summaryProportion=" + summaryProportion
				+ ", wordNetFolder=" + wordNetFolder + ", title=" + title
				+ ", isStemming=" + isStemming + ", stopWordElimination="
				+ stopWordElimination + "]";
	}
	public int getClusterNumber() {
		return clusterNumber;
	}
	public void setClusterNumber(int clusterNumber) {
		this.clusterNumber = clusterNumber;
	}
	public List<SummaryStrategy> getSummaryStrategy() {
		return summaryStrategy;
	}
	public void setSummaryStrategy(List<SummaryStrategy> summaryStrategy) {
		this.summaryStrategy = summaryStrategy;
	}
	public boolean isAnalysisMode() {
		return analysisMode;
	}
	public void setAnalysisMode(boolean analysisMode) {
		this.analysisMode = analysisMode;
	}
	public String getSentenceOrder() {
		return sentenceOrder;
	}
	public void setSentenceOrder(String sentenceOrder) {
		this.sentenceOrder = sentenceOrder;
	}
	public int getRun() {
		return run;
	}
	public void setRun(int run) {
		this.run = run;
	}
	
	
	
}
