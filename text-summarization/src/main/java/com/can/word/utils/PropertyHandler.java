package com.can.word.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyHandler {

	@Autowired
	private static Environment environment;
	
	public static boolean isStemming() {
		String stopWordElimination = (environment.getProperty("stopWordElimination"));
		if(stopWordElimination==null)
			stopWordElimination="true";
		if(stopWordElimination.equalsIgnoreCase("true")){
			return(true);
		}else{
			return (false);
		}
	}

	public static boolean isStopWordElimination() {
		String stopWordElimination = (environment.getProperty("stopWordElimination"));
		if(stopWordElimination==null)
			stopWordElimination="true";
		if(stopWordElimination.equalsIgnoreCase("true")){
			return(true);
		}else{
			return(false);
		}
	}
}
