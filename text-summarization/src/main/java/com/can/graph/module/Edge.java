package com.can.graph.module;

public class Edge {
	
	private double weight=0.0;
	
	public Edge() {
	}
	
	public Edge(double w) {
		setWeight(w);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
