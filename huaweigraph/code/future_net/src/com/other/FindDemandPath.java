package com.other;

import java.io.IOException;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;



public class FindDemandPath {

	//input attribute
	public SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph;
	public List<List<Integer>> demand;
	
	//output result
	public List<Integer> path;
	public double pathLength;
	
	//intermediate variable
	public List<VirtualEdge> virSet;
	public List<DefaultWeightedEdge> subPath;
	
	
	
	public void run(){
		this.replaceVirtualEdge();
		
		this.findHamiltonianPath();
		
		this.replaceVirtualEdge();
		
		this.combinePath(this.subPath);
	}
	
	/**
	 * Function: create a expand graph(add virtual edge in the base of original graph) of graph whose vertex set is M.
	 * 
	 */
	public void createVirtualEdge(){
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = this.graph;
		List<Integer> M = this.demand.get(1);
		for(int i = 0; i < M.size(); i++){
			
			for(int j = 0; j < M.size() && i != j; j++){
				
				//if the edge i->j is not in the graph,find a shortest path from i to j
				if(!graph.containsEdge(M.get(i), M.get(j))){
					DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(graph, i, j);
					
					if(dijkstra.getPath() == null){
						break;
					}else{
						List<DefaultWeightedEdge> path = dijkstra.getPathEdgeList();					
						double lengthOfPath = dijkstra.getPathLength();
						List<Integer> vSeq = Transform.vertexSeqOfPath(path);
						//if the path contains the vertex in M
						boolean isPass = false;
						for(int k = 0; k < M.size() && k != i && k != j; k++){
							if(vSeq.contains(M.get(k))){
								isPass = true;
								break;
							}
						}
						
						if(!isPass){
							
							DefaultWeightedEdge e = new DefaultWeightedEdge();
							e.setSource(i);
							e.setTarget(j);
							e.setWeight(lengthOfPath);
							graph.addEdge(i, j, e);
							VirtualEdge ve = new VirtualEdge(e);
							List<List<DefaultWeightedEdge>> rp = ve.getrPath();
							rp.add(path);
							ve.setrPath(rp);
						}else{
							break;
						}
					}
					
				}
			}
		}
	}
	

	
	
	/**
	 * find all the Hamiltonian path for a directed weighted graph
	 * @param graph
	 */
	public void findHamiltonianPath(){
		SimpleDirectedWeightedGraph graph = this.graph;
		
		
	}
	
	/**
	 * Inverse the virtual process, that is replacing virtual edge using the real path.
	 * when replacing the virtual edge, remove the virtual edge from the original graph.
	 */
	public void replaceVirtualEdge(){
		
		List<DefaultWeightedEdge> subPath = this.subPath;
		for(int i = 0; i < this.virSet.size(); i++){
			VirtualEdge tempVE = this.virSet.get(i);
			DefaultWeightedEdge tempDE = new DefaultWeightedEdge();
			tempDE.setSource(tempVE.getSource());
			tempDE.setTarget(tempVE.getTarget());
			tempDE.setWeight(tempVE.getWeight());
			
			if(subPath.contains(tempDE)){
				int index = subPath.indexOf(tempDE);
				
				//将虚拟边的实际路径插入到subPath中去
				List<DefaultWeightedEdge> rPath = tempVE.getrPath().get(0);
				subPath.set(index, rPath.get(0));//首先将原来那条虚拟边去掉，并替换成新的
				for(int j = 1; j < rPath.size(); j++){
					index = index + 1;
					subPath.add(index, rPath.get(j));
				}
			}
		}
		
		this.subPath = subPath;
	}
	
	
	
	/**
	 * get the final path
	 */
	public void combinePath(List<DefaultWeightedEdge> Path){
		List<Integer> subPath = Transform.vertexSeqOfPath(Path);
		int source = this.demand.get(0).get(0);
		int midS = subPath.get(0);
		int midT = subPath.get(subPath.size() - 1);
		int target = this.demand.get(0).get(1);
		
		
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dj1 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(this.graph, source, midS);
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dj2 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(this.graph, midT, target);
		
		if(dj1.getPath() != null && dj2.getPath() != null){
			List<Integer> prior = Transform.vertexSeqOfPath(dj1.getPathEdgeList());
			List<Integer> next = Transform.vertexSeqOfPath(dj2.getPathEdgeList());
			
			//要求权重。。还需要对权重进行处理，不能用现在的结构作为输入
			this.path.addAll(prior);
			this.path.addAll(subPath);
			this.path.addAll(next);
			
		}else{
			System.out.println("There's no specified path!");
			return;
		}
		
	}
	
	
	public static void main(String[] arg) throws IOException{
		String fileG = "test-case/case0/topo.csv";
		String fileD = "test-case/case1/demand.csv";
		String output = "test-case/case2/result.csv";
		
		FindDemandPath fdp = new FindDemandPath();
		
		fdp.graph = FileOperation.loadDWGraph(fileG);
		fdp.demand = FileOperation.loadDemand(fileD);
		
		fdp.run();
		
		
		FileOperation.savePath(fdp.path, output);
	}
}
