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
	Map<String,Double> documentMap;
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
		
		documentMap=new HashMap<String, Double>();
		Set<String> fileSet = sysSumMap.keySet();
		for (String file : fileSet) {
			Document sysDocument =sysSumMap.get(file);
			Document refDocument =refSumMap.get(file);
			if(sysDocument==null || refDocument==null){
				LOGGER.error("sys Doc: "+sysDocument+" - ref doc: "+refDocument);
				throw new MissingFileException(file);
			}
			NGramCalculator.createNGramForDocument(sysDocument, rougeNType, n);
			NGramCalculator.createNGramForDocument(refDocument, rougeNType, n);
			
			rougeNCalculator.setReferenceSentences(refDocument.getSentenceList());
			rougeNCalculator.setSystemSentences(sysDocument.getSentenceList());
			
			Double value = rougeNCalculator.calculateRougeN(n);
			documentMap.put(file, value);
			
		}
		return documentMap;
		
	}
	
	public double calculateAverageValue(){
		double avg=0.0;
		LOGGER.info("Bulk Rouge N Evaluator: Calculate Average");
		if(documentMap.size()!=0){
			LOGGER.info("Document number: "+documentMap.size());
			Set<String> evaluatedFiles = documentMap.keySet();
			for (String string : evaluatedFiles) {
				avg+=documentMap.get(string);
			}
			LOGGER.info("Document number sum: "+avg);
			avg=avg/documentMap.size();
		}
		return avg;
	}


}
