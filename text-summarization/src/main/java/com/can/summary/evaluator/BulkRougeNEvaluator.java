package com.can.summary.evaluator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.can.document.reader.BulkDocumentReader;
import com.can.success.calculations.RougeNCalculator;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.RougeNType;
import com.can.summarizer.model.Sentence;
import com.can.summary.calculations.NGramCalculator;
import com.can.summary.exceptions.MissingFileException;
import com.can.summary.exceptions.NotValidRougeNTypeException;


public class BulkRougeNEvaluator {
	private static final Logger LOGGER = Logger.getLogger(BulkRougeNEvaluator.class);
	private int n=3;
	private BulkDocumentReader systemSum, referenceSum;
	private RougeNType rougeNType=RougeNType.charBased;
	
	public BulkRougeNEvaluator(BulkDocumentReader systemSum,BulkDocumentReader referenceSum,int n,RougeNType rougeNType) {
		this.systemSum=systemSum;
		this.referenceSum=referenceSum;
		this.n=n;
		this.rougeNType=rougeNType;
	}
	
	public Map<String,Double> calculateRougeN() throws MissingFileException{
		
		RougeNCalculator rougeNCalculator=new RougeNCalculator();
		Map<String, Document> sysSumMap = systemSum.getDocumentMap();
		Map<String, Document> refSumMap = referenceSum.getDocumentMap();
		
		Map<String,Double> result=new HashMap<String, Double>();
		Set<String> fileSet = sysSumMap.keySet();
		for (String file : fileSet) {
			Document sysDocument =sysSumMap.get(file);
			Document refDocument =refSumMap.get(file);
			if(sysDocument==null || refDocument==null){
				LOGGER.error("sys Doc: "+sysDocument+" - ref doc: "+refDocument);
				throw new MissingFileException(file);
			}
			createNGramForDocument(sysDocument);
			createNGramForDocument(refDocument);
			
			rougeNCalculator.setReferenceSentences(refDocument.getSentenceList());
			rougeNCalculator.setSystemSentences(sysDocument.getSentenceList());
			
			Double value = rougeNCalculator.calculateRougeN(n);
			result.put(file, value);
			
		}
		return result;
		
	}

	private void createNGramForDocument(Document document) {
		List<Sentence> sentenceList = document.getSentenceList();
		for (Sentence sentence : sentenceList) {
			LinkedList<String> nGramList = NGramCalculator.findNGram(n, sentence, rougeNType);
			sentence.setNgramList(nGramList);
		}
	}
	

}
