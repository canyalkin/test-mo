package com.can.graph.module;

import org.apache.log4j.Logger;


public class Graph {
	private static final Logger LOGGER = Logger.getLogger(Graph.class);
	private int vertexNumber;
	private Edge[][] vertex;
	private double maxLength=0.0;
	
	public Graph(int vertexNumber) {
		
		this.setVertexNumber(vertexNumber);
		init(vertexNumber);
	}
	
	private void init(int vertexNumber) {
		vertex=new Edge[vertexNumber][vertexNumber];
		for(int i=0;i<vertexNumber;i++){
			for(int j=0;j<vertexNumber;j++){
				vertex[i][j]=new Edge();
			}
		}
	}
	public Graph() {
		
	}
	

	
	public int getVertexNumber() {
		return vertexNumber;
	}
	public void setVertexNumber(int vertexNumber) {
		this.vertexNumber = vertexNumber;
	}
	
	
	public void setWeight(int row,int column,double w){
		vertex[row][column].setWeight(w);
	}
	
	public void setEdge(int row,int column,Edge edge){
		vertex[row][column]=edge;
	}
	
	public Edge getEdge(int row,int column){
		return vertex[row][column];
	}
	
	@Override
	public String toString() {
		
		StringBuffer stringBuffer=new StringBuffer();
		for(int i=0;i<vertexNumber;i++){
			for(int j=0;j<vertexNumber;j++){
				stringBuffer.append(vertex[i][j].getWeight()+"\t");
			}
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}

	public double findMaximumLength(int desiredNumberOfSentenceInSum) {
		double length=0.0;
		
		double[][] A = new double[vertexNumber][];
		for (int i = 0; i < vertexNumber; i++) {
		  A[i] = new double[vertexNumber];
		  for(int j=0;j<vertexNumber;j++){
			  A[i][j]=0.0;
		  }
		}
		
		for(int j=1;j<vertexNumber;j++){
			for(int i=1;i<vertexNumber;i++){
				for(int k=0;k<i-1;k++){
					if(A[i][j]< A[k][j-1]+vertex[i][k].getWeight()){
						A[i][j] = A[k][j-1]+vertex[i][k].getWeight();
					}
				}
			}
		}
		if(LOGGER.isDebugEnabled()){
			StringBuffer stringBuffer=new StringBuffer();
			for (int i = 0; i < vertexNumber; i++) {
				for(int j=0;j<vertexNumber;j++){
					stringBuffer.append(A[i][j]+":");
				}
				stringBuffer.append("\n");
			}
			LOGGER.debug(stringBuffer.toString());
		}
		for(int i=0;i<vertexNumber;i++){
			if(length<A[i][desiredNumberOfSentenceInSum-1]){
				length=A[i][desiredNumberOfSentenceInSum-1];
			}
		}
		setMaxLength(length);
		return length;
		
		
	}

	public double getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(double maxLength) {
		this.maxLength = maxLength;
	}
	

}
