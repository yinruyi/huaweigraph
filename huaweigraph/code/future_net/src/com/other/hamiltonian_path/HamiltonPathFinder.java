package com.other.hamiltonian_path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedSubgraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.other.FileOperation;

public class HamiltonPathFinder {

	DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> subgraph;
	public Map<Integer, Boolean> visitTag;
//	public boolean[] visitTag;
	public int[] stack;
	public List<Integer> path;
	public static Set<List<Integer>> paths;
	public HamiltonPathFinder(DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> graph){
		this.subgraph = graph;
		paths = new HashSet<List<Integer>>();
	}
	
	public int FirstAdjVex(int v){//find the first neighbor of v
		Set<DefaultWeightedEdge> outEdge = subgraph.outgoingEdgesOf(v);
		Iterator<DefaultWeightedEdge> iter = outEdge.iterator();
		List<Integer> neighbors = new ArrayList<Integer>();
		
		while(iter.hasNext()){
			
			DefaultWeightedEdge p = iter.next();
			neighbors.add((Integer) p.getTarget());
		}
		if(neighbors.size() > 0){
			return neighbors.get(0);
		}else{
			return -1;
		}
		
		
	}
	
	public int NextAdjVex(int v,int w){//find the next neighbor relative w of v
		
		
		Set<DefaultWeightedEdge> outEdge = subgraph.outgoingEdgesOf(v);
		Iterator<DefaultWeightedEdge> iter = outEdge.iterator();
		List<Integer> neighbors = new ArrayList<Integer>();
		
		while(iter.hasNext()){
			
			DefaultWeightedEdge p = iter.next();
			neighbors.add((Integer) p.getTarget());
		}
		int from = neighbors.indexOf(w);
		
		if(from < neighbors.size() - 1){
//			System.out.println(v + "的上一个邻居是" + w + ",下个邻居是"+neighbors.get(from+1));
			return neighbors.get(from+1);
		}else{
//			System.out.println("已经没有更多邻居了");
			return -1;
		}
		
	}
	public void initStatus(){
		
		path = new ArrayList<Integer>();
		visitTag = new HashMap<Integer, Boolean>();
		
		Set<Integer> vSet = subgraph.vertexSet();
		Iterator<Integer> iter = vSet.iterator();
		while(iter.hasNext()){
			int v = iter.next();
			visitTag.put(v, false);
		}
		stack = new int[vSet.size()];
		for(int i = 0; i < vSet.size(); i++){
			stack[i] = -1;
		}
	}
	public boolean testHami(){
		Iterator<Entry<Integer, Boolean>> iter = visitTag.entrySet().iterator();
		while(iter.hasNext()){
			if(!iter.next().getValue()){
				return false;
			}
		}
		
		return true;
	}
	
	
	public void backtrack(int t, int idxsum){
		
//		System.out.println("当前遍历结点为："+t);
		int i;
		visitTag.put(t, true);
		stack[idxsum] = t;
		path.add(t);
		boolean flag = true;
		int temp = idxsum;
		for(i = FirstAdjVex(t); i>= 0; i = NextAdjVex(t,i)){
			flag = false;
			if(!visitTag.get(i)){
//				
				int idx = temp;
//				System.out.println(t + "->"+i + " with index " + (idx+1));
				backtrack(i, ++idx);
			}
		}
		if(flag){
			
//			System.out.println("该结点没有next");
			
		}
		if(testHami()){
//			System.out.println("There is a hamiltonian path :");
//			System.out.println(path);
			List<Integer> fine = new ArrayList(path);
			paths.add(fine);
//			System.out.println(paths);
			
//			return;
		}
		visitTag.put(t, false);//backtrack
		stack[idxsum] = -1;//clear
		path.remove((Integer)t);
			
	}
	
	public void Hami(){
		if(subgraph.edgeSet().size() < subgraph.vertexSet().size()){
			System.out.println("it is not a connected graph.");
			return;
		}
		
		Set<Integer> vSet = subgraph.vertexSet();
//		System.out.println("The number of vertices is :" + vSet.size());
		Iterator<Integer> iter = vSet.iterator();
		while(iter.hasNext()){
			int v = iter.next();
			
//			System.out.println("Start from vertex " + v);
			initStatus();
			backtrack(v,0);
		}
		
//		if(paths.size() > 0){
////			System.out.println(paths);
////			System.out.println("There are " +  paths.size() + " hamiltonian paths.");
//		}else{
//			System.out.println("Can't find any path.");
//		}
		
		
	}
	
	public static void Main(String[] arg) throws IOException{
		
		String file = "0,0,1,1\n"
						+"1,0,2,2\n"
						+"2,0,3,1\n"
						+"3,2,1,3\n"
						+"4,3,1,1\n"
						+"5,2,3,1\n"
						+"6,3,2,1";
		
		System.out.println(file);
		String f = "F:\\contest\\huaweigraph\\test-case\\case1\\topo.csv";
		
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = FileOperation.getGraph(file);//loadDWGraph(f);
		System.out.println(graph);
		DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> subGraph = new DirectedWeightedSubgraph<Integer, DefaultWeightedEdge>(graph, graph.vertexSet(), graph.edgeSet());
		HamiltonPathFinder finder = new HamiltonPathFinder(subGraph);
		
		finder.Hami();
		System.out.println(finder.paths.size());
		Iterator<List<Integer>> iter = finder.paths.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
		
	}
	
}
