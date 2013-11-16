package com.can.summarizer.interfaces;

import com.can.summarizer.model.Document;

public interface SummaryStrategy {
	
	Document doSummary(Document aDocument);

}
