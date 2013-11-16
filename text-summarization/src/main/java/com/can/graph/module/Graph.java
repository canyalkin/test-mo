package com.can.graph.module;


public class Graph {
	
	private int vertexNumber;
	private Edge[][] vertex;
	
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
	

}
