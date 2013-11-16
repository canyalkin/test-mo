package com.can.summarizer.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.can.document.handler.module.StopWordHandler;

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
	
	
	
	
	
	
}
