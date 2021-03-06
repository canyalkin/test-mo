package com.can.reporter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.can.document.handler.module.BulkDocumentHandler;
import com.can.document.handler.module.SingleDocumentHandler;
import com.can.summarizer.interfaces.IVisitor;
import com.can.summarizer.model.AnalysisData;
import com.can.summary.module.ClusterStrategy;
import com.can.summary.module.ClusterStrategyNew;
import com.can.summary.module.GASummaryStrategyImpl;
import com.can.word.utils.SummaryUtils;

public class SummaryReport implements IVisitor {
	
	private StringBuffer stringBuffer=new StringBuffer();
	private DecimalFormat formatter = new DecimalFormat();
	
	private double fitnessValue=0.0;
	
	public SummaryReport() {
		formatter.setMaximumFractionDigits(5);
		DecimalFormatSymbols dfs = formatter.getDecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		formatter.setDecimalFormatSymbols(dfs);
	}
	
	

	@Override
	public void visit(GASummaryStrategyImpl gaSummaryStrategyImpl) {
		fitnessValue+=gaSummaryStrategyImpl.getFitnessValue();
		stringBuffer.append("fitness value:"+formatter.format(gaSummaryStrategyImpl.getFitnessValue())+"\n");
		stringBuffer.append("generation number:"+gaSummaryStrategyImpl.getGenerationNumber()+"\n");
		stringBuffer.append("population size:"+gaSummaryStrategyImpl.getPopulationSize()+"\n");
		stringBuffer.append("crossover rate:"+gaSummaryStrategyImpl.getCrossoverRate()+"\n");
		stringBuffer.append("mutation rate:"+gaSummaryStrategyImpl.getMutationRate()+"\n");
	}

	@Override
	public void visit(ClusterStrategy clusterStrategy) {
		

	}
	
	@Override
	public void visit(SingleDocumentHandler singleDocumentHandler) {
		stringBuffer.append("summarization takes:"+singleDocumentHandler.getSummarizationTime()+" seconds\n");
		stringBuffer.append("ref word number:"+singleDocumentHandler.getRefWordNumber()+"\n");
		stringBuffer.append("summary word number:"+singleDocumentHandler.getSummarizedWordNumber()+"\n");
		stringBuffer.append("orig word number:"+singleDocumentHandler.getOriginalDocumentWordNumber()+"\n");
		stringBuffer.append("Rouge -N result:"+formatter.format(singleDocumentHandler.getRougeNResult())+"\n");
		stringBuffer.append("presicion:"+singleDocumentHandler.getPresicion()+"\n");
		stringBuffer.append("recall:"+singleDocumentHandler.getRecall()+"\n");
		stringBuffer.append("f1:"+singleDocumentHandler.getF1()+"\n");
		stringBuffer.append(singleDocumentHandler.getSummarizedDocument()+"\n");
	}

	@Override
	public void visit(BulkDocumentHandler bulkDocumentHandler) {
		
		stringBuffer.append("all summaries take:"+formatter.format(bulkDocumentHandler.getSummaryTime())+" sn\n");
		stringBuffer.append("evaluation take:"+formatter.format(bulkDocumentHandler.getEvaluationTime())+" sn\n");
		stringBuffer.append("rouge n type:"+(bulkDocumentHandler.getRougeNType())+"\n");
		stringBuffer.append("rouge n number:"+(bulkDocumentHandler.getRougeNNumber())+"\n");
		stringBuffer.append("summary proportion:"+formatter.format(bulkDocumentHandler.getSummaryProportion())+"\n");
		stringBuffer.append("max Word number:"+bulkDocumentHandler.getMaxWordNumber()+"\n");
		stringBuffer.append("population size:"+bulkDocumentHandler.getPopSize()+"\n");
		stringBuffer.append("genration number:"+bulkDocumentHandler.getGenerationNumber()+"\n");
		stringBuffer.append("Crossover Rate:"+formatter.format(bulkDocumentHandler.getXoverRate())+"\n");
		stringBuffer.append("Mutation Rate:"+formatter.format(bulkDocumentHandler.getMutationRate())+"\n");
		stringBuffer.append("Cluster Numebr:"+(bulkDocumentHandler.getClusterNumber())+"\n");
		Map<String, AnalysisData> analysisData = bulkDocumentHandler.getBulkDataAnalysis();
		Set<String> keySet = analysisData.keySet();
		stringBuffer.append("file:rouge-n:# of words in original doc:# of words in reference doc:# of words in summary doc:fitness value:presicion:recall:f1:sentence_presicion:sentence_recall:sentence_f1:ref_sentence_num:sum_sentence_num"+"\n");
		double total=0.0;
		for (String key : keySet) {
			AnalysisData curData = analysisData.get(key);
			stringBuffer.append(
					curData.getName()+":"
					+formatter.format(curData.getRougeNValue()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getOriginalWordNumber())+":"
					+formatter.format(curData.getRefWordNumber())+":"
					+formatter.format(curData.getSummWordNumber()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getFitnessValue()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getPresicion()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getRecall()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getF1()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getSentencePrecision()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getSentenceRecall()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getSentenceF1()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getRefSentenceNumber()/bulkDocumentHandler.getRun())+":"
					+formatter.format(curData.getSumSentenceNumber()/bulkDocumentHandler.getRun())
					+"\n");
			
					total+=curData.getRougeNValue();
			
		}
		List<Double> averageRougeNValueList = bulkDocumentHandler.getAverageRougeN();
		for (int i = 0; i < averageRougeNValueList.size(); i++) {
			stringBuffer.append("Rouge ("+(i+1)+"): "+averageRougeNValueList.get(i)+"\n");
			
		}
		double average = total/(keySet.size()*bulkDocumentHandler.getRun());
		stringBuffer.append("\nAverage:"+formatter.format(average)+"\n");
		bulkDocumentHandler.clearData();
		
	}
	
	public String createReport(){
		return stringBuffer.toString();
	}



	@Override
	public void visit(ClusterStrategyNew clusterStrategyNew) {
		// TODO Auto-generated method stub
		
	}


}
