package com.can.summarizer;

import com.can.summarizer.interfaces.SummaryStrategy;
import com.can.summarizer.model.Document;

public class TextSummarizer {
	
	private SummaryStrategy summaryStrategy;
	
	public TextSummarizer(SummaryStrategy aSummaryStrategy) {
		this.summaryStrategy=aSummaryStrategy;
	}
	
	public Document doSummarization(Document aDocument){
		return summaryStrategy.doSummary(aDocument);
	}
	

}
