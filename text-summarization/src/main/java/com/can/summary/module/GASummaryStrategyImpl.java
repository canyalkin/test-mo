package com.can.summary.module;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.FixedBinaryGene;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.can.graph.module.Graph;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.Sentence;
import com.can.summary.GAFunctions.GeneHandler;
import com.can.summary.GAFunctions.SummaryCrossover;
import com.can.summary.GAFunctions.SummaryFitness;
import com.can.summary.GAFunctions.SummaryMutation;
import com.can.summary.calculations.FrequencyCalculator;
import com.can.word.utils.PropertyHandler;

@Component("GaStrategyBean")
@Scope("singleton")
public class GASummaryStrategyImpl extends AbstractSummarizer {
	
	private int generationNumber = 15;
	private double crossoverRate = 0.85;
	private int mutationRate = 1000;
	private int populationSize= 20 ;
	private double fitnessValue;
	
	
	private static final Logger LOGGER = Logger.getLogger(GASummaryStrategyImpl.class);
	private HashMap<String, Integer> freqTable=null;
	private HashMap<String, List<Double>> tfTable=null;
	private HashMap<String, Double> isf=null;
	
	@Autowired
	private PropertyHandler propertyHandler;
	
	public GASummaryStrategyImpl() {
		super();
	}

	public Document doSummary(Document aDocument) {
		getStopWordEliminationFromProperty();
		getStemmingFromProperty();
		extractSummPropFromProperties();
		long t1=System.currentTimeMillis();
		LOGGER.debug("summarization starts...");
		setDocumentToBeSummarized(aDocument);
		if(isStopWordElimination()){
			doStopWordElimination();
		}
		if(isStemming()){
			doStemming();
		}
		setNumberOfSentences(aDocument.getSentenceList().size());
		setDesiredNumberOfSentenceInSum((int)Math.round(getNumberOfSentences()*getSummaryProportion()));
		LOGGER.debug("DesiredNumberOfSentenceInSum:"+getDesiredNumberOfSentenceInSum());
		createStructuralProperties(aDocument);//frequency table ,tf, isf
		Graph graph=new Graph(getNumberOfSentences());
		graph=createGraph(graph, aDocument);
		graph.findMaximumLength(getDesiredNumberOfSentenceInSum());
		LOGGER.debug("Graph created...");
		LOGGER.debug(graph);
		/****
		 * GA Configuration
		 */
		Genotype genotype = createGAConfig(aDocument, graph);
		LOGGER.debug("generation number:"+generationNumber);
		LOGGER.debug("population number:"+populationSize);
		LOGGER.debug("crossover rate:"+crossoverRate);
		LOGGER.debug("mutation rate:"+mutationRate);
		doEvolution(genotype);
		
		List<Integer> indexes = GeneHandler.getSummaryIndexes(genotype.getFittestChromosome());
		LOGGER.info("summary indexes:"+indexes);
		Document summaryDocument = createSummaryDocument(aDocument, indexes);
		//Document summaryDocument = createSummaryDocument(aDocument, genotype);
		/******************************************************/
		long t2=System.currentTimeMillis();
		LOGGER.info("summarization takes "+(t2-t1)/1000.0+" seconds.");
		return summaryDocument;
	}

	private void doEvolution(Genotype genotype) {
		LOGGER.debug("GAConfig created...");
		generationNumber=propertyHandler.getGenerationNumber();
		for(int i=0;i<generationNumber;i++){
			genotype.evolve();
			double fitnesValue = genotype.getFittestChromosome().getFitnessValue();
			LOGGER.info("fitness value="+fitnesValue);
		}
		LOGGER.info("best fitness value:"+genotype.getFittestChromosome().getFitnessValue());
		String geneString=GeneHandler.showGene(genotype.getFittestChromosome().getGenes());
		LOGGER.info("best gene as string:"+geneString);
		setFitnessValue(genotype.getFittestChromosome().getFitnessValue());
	}


	private void createStructuralProperties(Document aDocument) {
		freqTable = FrequencyCalculator.createFrequencyTable(getDocumentToBeSummarized());
		LOGGER.debug("freq table created..."+freqTable);
		tfTable = FrequencyCalculator.calculateTermFreq(freqTable, aDocument);
		LOGGER.debug("tf Table created..."+tfTable);
		isf = FrequencyCalculator.calculateInverseSentenceFreqTable(freqTable, aDocument);
		LOGGER.debug("isf created..."+isf);
	}


	private Genotype createGAConfig(Document aDocument, Graph graph) {
		Genotype genotype = null;
		Configuration.reset();
		Configuration configuration=new DefaultConfiguration();
		setGeneralGAParameters(configuration);
		
		try {
			setGeneticOperators(graph, configuration);
			
			createGenesAndPopulation(aDocument, configuration);
			
			LOGGER.debug("population size:"+configuration.getPopulationSize());
			
			genotype = generatePopulation(configuration);
			//genotype.evolve();
		} catch (InvalidConfigurationException e) {
			LOGGER.error(e);
		}
		
		return genotype;
	}


	private Genotype generatePopulation(Configuration configuration)
			throws InvalidConfigurationException {
		Genotype genotype;
		genotype = Genotype.randomInitialGenotype(configuration);
		List chromosomeList = genotype.getPopulation().getChromosomes();
		RandomGenerator randomGenerator = configuration.getRandomGenerator();
		for (int i = 0; i < chromosomeList.size(); i++) {
			IChromosome iChromosome=(IChromosome) chromosomeList.get(i);
			Gene[] currGenes = iChromosome.getGenes();
			GeneHandler.updateGene(randomGenerator, currGenes, getDesiredNumberOfSentenceInSum(), getNumberOfSentences());
		}
		return genotype;
	}


	private void createGenesAndPopulation(Document aDocument,
			Configuration configuration) throws InvalidConfigurationException {
		FixedBinaryGene[] sampleGenes = new FixedBinaryGene[ aDocument.getSentenceList().size() ];
		for (int i = 0; i < sampleGenes.length; i++) {
			sampleGenes[i]=new FixedBinaryGene(configuration, 1);
		}
		Chromosome sampleChromosome = new Chromosome(configuration, sampleGenes );
		configuration.setSampleChromosome( sampleChromosome );
		configuration.setPopulationSize( (populationSize=propertyHandler.getPopulationNumber()) );
	}


	private void setGeneticOperators(Graph graph, Configuration configuration)
			throws InvalidConfigurationException {
		SummaryFitness myFitnessFunction = new SummaryFitness(graph,getDesiredNumberOfSentenceInSum());
		configuration.getGeneticOperators().clear();
		configuration.addGeneticOperator(new SummaryCrossover(getNumberOfSentences(), getDesiredNumberOfSentenceInSum(),configuration,(crossoverRate=propertyHandler.getCrossoverRate())));
		configuration.addGeneticOperator(new SummaryMutation(configuration,(mutationRate=propertyHandler.getMutationRate())));
		configuration.setFitnessFunction(myFitnessFunction);
	}


	private void setGeneralGAParameters(Configuration configuration) {
		configuration.setPreservFittestIndividual(true);
		configuration.setKeepPopulationSizeConstant(true);
	}


	private Graph createGraph(Graph graph,Document document) {
		int senteceNumber=document.getSentenceList().size();
		for(int i=0;i<senteceNumber;i++){
			for(int j=0;j<senteceNumber;j++){
				if(i<j){
					graph.setWeight(i, j, 0.0);
				}else if(i==j){
					graph.setWeight(i, j, 0.0);
				}else{
					graph.setWeight(i, j, calculateSimilarityForSentences(i,j,document));
				}
			}
		}
		return graph;
	}


	private double calculateSimilarityForSentences(int i, int j,Document aDocument) {
		
		Iterator<String> keyIterator = freqTable.keySet().iterator();
		double nominator=0.0;
		double denominatorLeft=0.0;
		double denominatorRight=0.0;
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			double wI=calculateWeight(key,i);
			double wJ=calculateWeight(key,j);
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


	private double calculateWeight(String key, int i) {
		List<Double> termFreqListOfIndexTerm = tfTable.get(key);
		Double tfValue = termFreqListOfIndexTerm.get(i);
		Double isfValue = isf.get(key);
		return tfValue*isfValue;
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	public void setFitnessValue(double fitnessValue) {
		this.fitnessValue = fitnessValue;
	}
}
