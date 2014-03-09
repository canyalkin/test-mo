package com.can.summary.evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.can.success.calculations.PresicionRecallCalculator;
import com.can.summarizer.model.Document;
import com.can.summarizer.model.PresicionRecallData;

public class BulkPresicionCalculator {
	
	private Map<String, Document> systemSum;
	private Map<String, Document> referenceSum;
	private PresicionRecallCalculator presicionRecallCalculator=new PresicionRecallCalculator();
	
	public BulkPresicionCalculator(Map<String, Document> systemSum,Map<String, Document> referenceSum) {
		this.systemSum=systemSum;
		this.referenceSum=referenceSum;
		
	}
	
	public HashMap<String, PresicionRecallData> calculatePresicionvalues(){
		HashMap<String, PresicionRecallData> presicionMap=new HashMap<String, PresicionRecallData>();
		Set<String> keys = systemSum.keySet();
		PresicionRecallData presicionRecallData;
		for (String key : keys) {
			presicionRecallCalculator.setRefDocument(referenceSum.get(key));
			presicionRecallCalculator.setSysDocument(systemSum.get(key));
			presicionRecallCalculator.doCalulations();
			double presicion = presicionRecallCalculator.getPresicion();
			double recall = presicionRecallCalculator.getRecall();
			double f1 = presicionRecallCalculator.getF1();
			presicionRecallData=new PresicionRecallData(presicion, recall, f1);
			presicionMap.put(key, presicionRecallData);		
		}
		return presicionMap;
	}

	/**
	 * @param systemSum the systemSum to set
	 */
	public void setSystemSum(Map<String, Document> systemSum) {
		this.systemSum = systemSum;
	}

	/**
	 * @param referenceSum the referenceSum to set
	 */
	public void setReferenceSum(Map<String, Document> referenceSum) {
		this.referenceSum = referenceSum;
	}
	

}
