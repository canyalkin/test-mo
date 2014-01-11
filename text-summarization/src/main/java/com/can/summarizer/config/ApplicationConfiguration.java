package com.can.summarizer.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.can.analysis.AnalysisHandler;
import com.can.analysis.AnalysisProperty;
import com.can.document.handler.module.StopWordHandler;
import com.can.word.utils.PropertyHandler;

@Configuration
@ComponentScan(basePackages="com.can")
@PropertySource("summarization.properties")
public class ApplicationConfiguration {

	
	
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
	
	
}
