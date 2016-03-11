package com.other;

import java.util.ArrayList;
import java.util.List;

//import preliminary.test.DefaultWeightedEdge;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Transform {

	/**
	 *
	 * @param path
	 * @return
	 */
	public static List<Integer> vertexSeqOfPath(List<DefaultWeightedEdge> path){
		List<Integer> vSeq = new ArrayList<Integer>();
		for(int i = 0; i < path.size(); i++){
			Integer source = (Integer) path.get(i).getSource();
			vSeq.add(source);
			if( i == path.size() - 1){
				vSeq.add((Integer)path.get(i).getTarget());
			}
		}
		
		return vSeq;
		
	}
}
