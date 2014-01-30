package com.can.summary.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.can.summarizer.interfaces.IStrategyDirector;
import com.can.summarizer.interfaces.SummaryStrategy;
import com.can.word.utils.PropertyHandler;

@Component
public class StrategyDirector implements IStrategyDirector {
	
	@Autowired
	PropertyHandler propertyHandler;
	
	@Override
	public List<SummaryStrategy> getSummaryStrategies() {
		return propertyHandler.getSummaryStrategy();
	}
	
	
	

	
	
}
