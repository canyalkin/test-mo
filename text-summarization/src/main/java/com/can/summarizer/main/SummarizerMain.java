package com.can.summarizer.main;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.can.document.handler.module.BulkDocumentHandler;
import com.can.document.handler.module.SingleDocumentHandler;
import com.can.document.reader.BulkDocumentReader;
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

		/***
		 * Single file read
		 * log level
		 * off:2.94 sm
		 * info:2.94 sn
		 * debug:10.061 sn
		 * 
		 */
		/*StringBuffer stringBuffer=new StringBuffer();
		SingleDocumentHandler singleDocumentHandler=context.getBean(SingleDocumentHandler.class);
		singleDocumentHandler.readDocument(propertyHandler.getDocumentName());
		Document sysSum=singleDocumentHandler.summarize();
		Document refDocument=singleDocumentHandler.readRefDocument(propertyHandler.getRefDocumentName());
		Double result=singleDocumentHandler.calculateRougeN(sysSum,refDocument,propertyHandler.getRougeNType(),
				propertyHandler.getRougeNNumber());
		stringBuffer.append("orig word number:"+singleDocumentHandler.getOriginalDocumentWordNumber()+"\n");
		stringBuffer.append("ref word number:"+SummaryUtils.calculateOriginalSentenceWordNumber(refDocument)+"\n");
		stringBuffer.append("summary word number:"+SummaryUtils.calculateOriginalSentenceWordNumber(sysSum)+"\n");
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMaximumFractionDigits(5);
		//formatter.setMinimumFractionDigits(4);
		DecimalFormatSymbols dfs = formatter.getDecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		formatter.setDecimalFormatSymbols(dfs);
		stringBuffer.append("Rouge -N result:"+formatter.format(result)+"\n");
		stringBuffer.append(sysSum+"\n");
		output.write(stringBuffer.toString());*/
		
		
		
		BulkDocumentHandler bulkDocumentHandler=context.getBean(BulkDocumentHandler.class);
		
		/***
		 * Bulk Read
		 */
		BulkDocumentReader systemDocuments = bulkDocumentHandler.doBulkRead();
		
		/**
		 * Do bulk summarization, create system summaries and update system document map
		 */
		Map<String, Document> summaryDocs = bulkDocumentHandler.doBulkSummarization(systemDocuments);
		
		/***
		 * Bulk Read for reference
		 */
		BulkDocumentReader referenceDocuments = bulkDocumentHandler.doBulkReferenceRead();
		
		/**
		 * Bulk evaluation
		 */
		String report=bulkDocumentHandler.doBulkEvaluation(systemDocuments.getDocumentMap(),summaryDocs ,referenceDocuments.getDocumentMap());
		output.write(report);
		
		
	}
	

}
