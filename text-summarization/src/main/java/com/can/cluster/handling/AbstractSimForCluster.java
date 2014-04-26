package com.can.cluster.handling;

public class AbstractSimForCluster {

	
	
	protected double[][] createMatrix(int numberOfSentence) {
		double [][]simMatrix=new double[numberOfSentence][];
		for(int i=0;i<numberOfSentence;i++){
			simMatrix[i]=new double[numberOfSentence];
			for(int j=0;j<numberOfSentence;j++){
				simMatrix[i][j]=0.0;
			}
		}
		return simMatrix;
	}

}
