package com.can.summarizer.main;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.can.analysis.AnalysisHandler;
import com.can.document.handler.module.BulkDocumentHandler;
import com.can.document.handler.module.SingleDocumentHandler;
import com.can.document.reader.BulkDocumentReader;
import com.can.reporter.SummaryReport;
import com.can.summarizer.config.ApplicationConfiguration;
import com.can.summarizer.interfaces.IOutput;
import com.can.summarizer.model.Document;
import com.can.word.utils.PropertyHandler;
import com.can.word.utils.SummaryUtils;

public class SummarizerMain {

	private static final Logger LOGGER = Logger.getLogger(SummarizerMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		PropertyHandler propertyHandler=context.getBean(PropertyHandler.class);
		IOutput output=(IOutput) context.getBean("FileBean");
		AnalysisHandler analysisHandler=(AnalysisHandler)context.getBean(AnalysisHandler.class);
		/***
		 * Single file read
		 * log level
		 * off:2.94 sm
		 * info:2.94 sn
		 * debug:10.061 sn
		 * 
		 */
		
		/*SingleDocumentHandler singleDocumentHandler=context.getBean(SingleDocumentHandler.class);
		singleDocumentHandler.readDocument(propertyHandler.getDocumentName());
		Document refDocument=singleDocumentHandler.readRefDocument(propertyHandler.getRefDocumentName());
		
		SummaryReport summaryReport;
		int i=0;
		while(analysisHandler.setNextValues()){
			summaryReport=new SummaryReport();
			//ilk ozetlemeden sonra stemming ve stopword kapa
			long t1=System.currentTimeMillis();
			Document sysSum=singleDocumentHandler.summarize();
			long t2=System.currentTimeMillis();
			Double result=singleDocumentHandler.calculateRougeN(sysSum,refDocument,propertyHandler.getRougeNType(),
					propertyHandler.getRougeNNumber());
			singleDocumentHandler.accept(summaryReport);
			String report = summaryReport.createReport();
			output.write("output"+i+".txt",report);
			i++;
		}*/
		
		
		
		SummaryReport summaryReport;
		BulkDocumentHandler bulkDocumentHandler=context.getBean(BulkDocumentHandler.class);
		
		/***
		 * Bulk Read for reference
		 */
		BulkDocumentReader referenceDocuments = bulkDocumentHandler.doBulkReferenceRead();
		
		/***
		 * Bulk Read
		 */
		BulkDocumentReader systemDocuments = bulkDocumentHandler.doBulkRead();
		
		
		int i=0;
		while(analysisHandler.setNextValues() && analysisHandler.isAnalysisMode()){
			summaryReport=new SummaryReport();
			/**
			 * Do bulk summarization, create system summaries and update system document map
			 */
			Map<String, Document> summaryDocs = bulkDocumentHandler.doBulkSummarization(systemDocuments);
			/**
			 * Bulk evaluation
			 */
			bulkDocumentHandler.doBulkEvaluation(systemDocuments.getDocumentMap(),summaryDocs ,referenceDocuments.getDocumentMap());
			bulkDocumentHandler.accept(summaryReport);
			String report=summaryReport.createReport();
			output.write("output"+(i+1)+".txt",report);
			i++;
			propertyHandler.setStemming(false);
			propertyHandler.setStopWordElimination(false);
		}
		
		
	}
	

}
