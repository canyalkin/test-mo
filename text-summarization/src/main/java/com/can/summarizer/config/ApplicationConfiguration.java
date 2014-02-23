package com.can.summarizer.config;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.can.analysis.AnalysisHandler;
import com.can.analysis.AnalysisProperty;
import com.can.cluster.chooser.MaxDiffStrategy;
import com.can.cluster.chooser.ProportionalClusterChooser;
import com.can.cluster.chooser.SimpleClusterChooseStrategy;
import com.can.cluster.handling.CosSimilarityForCluster;
import com.can.cluster.sentence.chooser.MaxUniqueWordChooser;
import com.can.cluster.sentence.chooser.SimpleSentenceChooser;
import com.can.document.handler.module.StopWordHandler;
import com.can.pos.tagger.OpenNLPPosTagger;
import com.can.summarizer.interfaces.ClusterChooseSentenceStrategy;
import com.can.summarizer.interfaces.ClusterChooseStrategy;
import com.can.summarizer.interfaces.ICalculateSimilarity;
import com.can.summarizer.interfaces.IPOSTagger;
import com.can.summarizer.interfaces.SentenceOrder;
import com.can.summary.module.AbstractSummarizer;
import com.can.summary.module.ClusterStrategy;
import com.can.summary.sentence.order.MaxConceptOrder;
import com.can.summary.sentence.order.MaxWordNumberOrder;
import com.can.summary.sentence.order.TfIdfOrder;
import com.can.word.utils.PropertyHandler;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.morph.WordnetStemmer;

@Configuration
@ComponentScan(basePackages="com.can")
@PropertySource("summarization.properties")
public class ApplicationConfiguration {
	private static final Logger LOGGER = Logger.getLogger(ApplicationConfiguration.class);
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	Environment environment;
	
	@Bean
	public File getFile(){
		return new File(environment.getProperty("file"));
	}
	
	@Bean
	public StopWordHandler getStopWordHandler(){
		File stopWordFile= new File(environment.getProperty("stopWordsFile"));
		return StopWordHandler.getInstance(stopWordFile);
	}
	
	@Bean(initMethod="init")
	public PropertyHandler getPropertyHandler(){
		return PropertyHandler.getInstance();
	}
	
	@Bean (initMethod="init")
	public AnalysisProperty getAnalysisProperty(){
		return AnalysisProperty.getInstance();
	}
	
	@Bean (initMethod="reset")
	public AnalysisHandler getAnalysisHandler(){
		return AnalysisHandler.getInstance();
	}
	
	@Bean(name="simFunctions")
	public List<ICalculateSimilarity> getSimilarityFunctions(){
		List<ICalculateSimilarity> list=new ArrayList<ICalculateSimilarity>();
		String simList = environment.getProperty("similarityFunctions");
		String[] simArray = simList.split(",");
		for (String simFunction : simArray) {
			list.add(applicationContext.getBean(simFunction, ICalculateSimilarity.class));
		}
		
		return list;
	}
	
	@Bean(name="clusterProportion")
	public List<Double> getMergeProportion(){
		List<Double> proportionList=new ArrayList<Double>();
		String propList = environment.getProperty("similarityProportion");
		if(propList==null || propList.equals("")){
			proportionList.add(1.00);
		}else{
			String[] propArray = propList.split(",");
			for (String string : propArray) {
				proportionList.add(Double.parseDouble(string));
			}
		}
		return proportionList;
	}
	
	@Bean
	public ClusterChooseStrategy getClusterChooseStrategy(){
		String propList = environment.getProperty("clusterChooseStrategy");
		if(propList.equals("maxdiff")){
			return new MaxDiffStrategy();
			
		}else if(propList.equals("proportional")){
			return new ProportionalClusterChooser();
		}else{
			return new SimpleClusterChooseStrategy();
		}
	}
	
	@Bean
	public ClusterChooseSentenceStrategy getClusterChooseSentenceStrategy(){
		String propList = environment.getProperty("clusterSentenceChoose");
		if(propList.equals("simple")){
			return new SimpleSentenceChooser();
		}else if(propList.equals("maxuniqueword")){
			return new MaxUniqueWordChooser();
		}else{
			return new SimpleSentenceChooser();
		}
		
	}
	
	@Bean 
	public IPOSTagger getPOSTagger(){
		String file=environment.getProperty("openNLPPosTaggerFile");
		return OpenNLPPosTagger.getInstance(file);
	}
	
	@Bean
	public IDictionary getDictionary(){
		String wnhome = environment.getProperty("wordNetFolder");
		String path = wnhome;
		URL url;
		IDictionary dict =null;
		try {
			url = new URL ( "file" , null , path );
			// construct the dictionary object and open it
			dict = new Dictionary ( url ) ;
			dict.open();	
		} catch (MalformedURLException e) {
			LOGGER.error("MalformedURLException");
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IOException in word stemmer");
			LOGGER.error(e.getMessage());
		}
		
		return dict;
		
	}
	
	@Bean
	public SentenceOrder getSentenceOrder(){
		String propList = environment.getProperty("sentenceOrder");
		if(propList.equals("TfIdfOrder")){
			return new TfIdfOrder();
		}else if(propList.equals("MaxConceptOrder")){
			return new MaxConceptOrder();
		}else if(propList.equals("MaxWordNumberOrder")){
			return new MaxWordNumberOrder();
		}
		return new TfIdfOrder();
	}
	
	
}
