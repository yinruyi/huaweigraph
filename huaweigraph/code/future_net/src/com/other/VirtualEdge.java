package com.other;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;

public class VirtualEdge extends DefaultWeightedEdge{

	
	private List<List<DefaultWeightedEdge>> rPath;
	private List<Double> lenrP;
	public VirtualEdge(DefaultWeightedEdge e){
		this.setSource(e.getSource());
		this.setTarget(e.getTarget());
		this.setWeight(e.getWeight());
		this.rPath = new ArrayList<List<DefaultWeightedEdge>>();
		this.lenrP = new ArrayList<Double>();
	}

	public List<List<DefaultWeightedEdge>> getrPath() {
		return rPath;
	}

	public void setrPath(List<List<DefaultWeightedEdge>> rPath) {
		this.rPath = rPath;
	}

	public List<Double> getLenrP() {
		return lenrP;
	}

	public void setLenrP(List<Double> lenrP) {
		this.lenrP = lenrP;
	}
		
}
