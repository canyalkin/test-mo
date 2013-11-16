package com.can.summarizer.interfaces;

import com.can.summarizer.model.Document;

public interface IStopWord {
	public Document doStopWordElimination(Document aDocument);
}
