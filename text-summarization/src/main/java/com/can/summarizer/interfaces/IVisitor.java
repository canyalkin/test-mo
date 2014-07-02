package com.can.summarizer.interfaces;

import com.can.document.handler.module.BulkDocumentHandler;
import com.can.document.handler.module.SingleDocumentHandler;
import com.can.summary.module.ClusterStrategy;
import com.can.summary.module.ClusterStrategyNew;
import com.can.summary.module.GASummaryStrategyImpl;

public interface IVisitor {
	
	void visit(GASummaryStrategyImpl gaSummaryStrategyImpl);
	void visit(ClusterStrategy clusterStrategy);
	void visit(SingleDocumentHandler singleDocumentHandler);
	void visit(BulkDocumentHandler bulkDocumentHandler);
	void visit(ClusterStrategyNew clusterStrategyNew);

}
