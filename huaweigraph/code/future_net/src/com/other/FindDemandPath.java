package com.other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedSubgraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import com.filetool.util.LogUtil;



public class FindDemandPath {

	//input attribute
	public SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph;
	public List<List<Integer>> demand;
	
	//output result
	public List<DefaultWeightedEdge> path = new ArrayList<DefaultWeightedEdge>();
	public double pathLength;
	public String result;
	
	//intermediate variable
	public List<VirtualEdge> virSet = new ArrayList<VirtualEdge>();
	public List<DefaultWeightedEdge> subPath = new ArrayList<DefaultWeightedEdge>();
	
	
	
	
	public void run(){
		
	}
	
	public void dijkstra(){
		
	}
	/**
	 * Function: create a expand graph(add virtual edge in the base of original graph) of graph whose vertex set is M.
	 * 
	 */
	public void createVirtualEdge(){
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = this.graph;
		List<Integer> M = this.demand.get(1);
		System.out.println(M);
		for(int i = 0; i < M.size(); i++){
			
			for(int j = 0; j < M.size() && i != j; j++){
				
				//if the edge i->j is not in the graph,find a shortest path from i to j
				if(!graph.containsEdge(M.get(i), M.get(j))){
					DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(graph, M.get(i), M.get(j));
					
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
							e.setSource(M.get(i));
							e.setTarget(M.get(j));
							e.setWeight(lengthOfPath);
							graph.addEdge(M.get(i), M.get(j), e);
							VirtualEdge ve = new VirtualEdge(e);
							List<List<DefaultWeightedEdge>> rp = ve.getrPath();
							rp.add(path);
							ve.setrPath(rp);
							this.virSet.add(ve);
						}else{
							break;
						}
					}
					
				}
			}
		}
		if(this.virSet == null){
//			System.out.println("There is no vir");
			LogUtil.printLog("There is no vir.");
		}else{
			System.out.println("The number of virtual edge is " + this.virSet.size());
			System.out.println("**The virtual edges**");
			System.out.println(this.virSet);
		}
		
	}
	

	
	
	/**
	 * find all the Hamiltonian path for a directed weighted graph
	 * @param graph
	 */
	public void findHamiltonianPath(){
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = this.graph;
		
		//construct a subgraph of V' in graph
		List<Integer> vertexs = this.demand.get(1);
		Set<Integer> vertexSubset = new HashSet<Integer>();
		Set<DefaultWeightedEdge> edgeSubset = new HashSet<DefaultWeightedEdge>();
		for(int i = 0; i < vertexs.size(); i++){
			vertexSubset.add(vertexs.get(i));
			
			for(int j = 0; j < vertexs.size() && i != j; j++){
				
				DefaultWeightedEdge edge = new DefaultWeightedEdge();
				
				if(graph.containsEdge(vertexs.get(i), vertexs.get(j))){
					edgeSubset.add(graph.getEdge(vertexs.get(i), vertexs.get(j)));
				}
				if(graph.containsEdge(vertexs.get(j), vertexs.get(i))){
					edgeSubset.add(graph.getEdge(vertexs.get(j), vertexs.get(i)));
				}
			}
		}
//		System.out.println(edgeSubset);
		
		DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> subgraph = new DirectedWeightedSubgraph<Integer, DefaultWeightedEdge>(graph, vertexSubset, edgeSubset);
		
		System.out.println("**test for subgraph**");
		System.out.println(subgraph.vertexSet().size());
		System.out.println(subgraph.edgeSet().size());
		System.out.println(subgraph.edgeSet());
		

		TopologicalOrderIterator<Integer, DefaultWeightedEdge> tpSortIter = new TopologicalOrderIterator<Integer, DefaultWeightedEdge>(subgraph);
		List<Integer> sort = new ArrayList<Integer>();
		while(tpSortIter.hasNext()){
			Integer ver = tpSortIter.next();
			System.out.println(ver);
			sort.add(ver);
		}
		boolean hasHPath = true;
		double weight = 0;
		for(int i = 0; i < sort.size() - 1; i++){
			if(!subgraph.containsEdge(sort.get(i), sort.get(i+1))){
				hasHPath = false;
				break;
			}else{
				
				weight += subgraph.getEdge(sort.get(i), sort.get(i+1)).getWeight();
				subPath.add(subgraph.getEdge(sort.get(i), sort.get(i+1)));
			}
		}
//		
		if(hasHPath){
			pathLength = weight;
		}else{
			subPath = null;
			pathLength = 0;
		}
	}
	
	/**
	 * Inverse the virtual process, that is replacing virtual edge using the real path.
	 * when replacing the virtual edge, remove the virtual edge from the original graph.
	 */
	public void replaceVirtualEdge(){
		
		if(this.subPath == null){
			LogUtil.printLog("replaceVirtualEdge failed.\nThere's no subpath in V'.");
			return;
		}
		
		List<DefaultWeightedEdge> subPath = this.subPath;
		
//		System.out.println(subPath.size());
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
		
//		this.subPath = subPath;
	}
	
	
	
	/**
	 * get the final path
	 */
	public void combinePath(){
		if(this.subPath == null){
			LogUtil.printLog("combinePaht failed.\nThere's no subpath in V'.");
			return;
		}
		
//		System.out.println(this.subPath);
		int source = this.demand.get(0).get(0);
		int midS = (int)subPath.get(0).getSource();
		int midT = (int)subPath.get(subPath.size() - 1).getTarget();
		int target = this.demand.get(0).get(1);
		
		//这里要处理一个问题：即dj1和dj2可能会经过V’中的点。所以要先将图中的V‘中的点去掉，再找shortest
		
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dj1 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(this.graph, source, midS);
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dj2 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(this.graph, midT, target);
		
		if(dj1.getPath() != null && dj2.getPath() != null){
			
			//
			List<DefaultWeightedEdge> prior = dj1.getPathEdgeList();
			List<DefaultWeightedEdge> next = dj2.getPathEdgeList();
			
			this.pathLength += dj1.getPathLength() + dj2.getPathLength();
			
			//要求权重。。还需要对权重进行处理，不能用现在的结构作为输入
			System.out.print(prior);
			System.out.println("->" + dj1.getPathLength());
			System.out.print(next);
			System.out.println("->" + dj2.getPathLength());
			
			this.path.addAll(prior);
			this.path.addAll(subPath);
			this.path.addAll(next);
			
			
			
			System.out.println("The path is:" + this.path);
			System.out.println("The weight of this path is :" + this.pathLength);
			
		}else{
			System.out.println("There's no specified path!");
			return;
		}
		
	}
	
	public String formatString() {
		List<DefaultWeightedEdge> vSeq = this.path;
		String result = "";
		result += ((Integer)(vSeq.get(0).getId())).toString();
		for(int i=1;i< vSeq.size();i++){  
			result += ("|" + ((Integer)(vSeq.get(i).getId())).toString());  
		} 
		return result;
	}
	
	public static void main(String[] arg) throws IOException{
		String fileG = "F:\\contest\\huaweigraph\\test-case\\case0/topo.csv";
		String fileD = "F:\\contest\\huaweigraph\\test-case\\case0/demand.csv";
		String output = "F:\\contest\\huaweigraph\\test-case\\case0/result.csv";
		
		FindDemandPath fdp = new FindDemandPath();
		
		fdp.graph = FileOperation.loadDWGraph(fileG);
		fdp.demand = FileOperation.loadDemand(fileD);
		
		fdp.graph.removeVertex(fdp.demand.get(0).get(0));
		fdp.graph.removeVertex(fdp.demand.get(0).get(1));
		TopologicalOrderIterator<Integer, DefaultWeightedEdge> tpSortIter = new TopologicalOrderIterator<Integer, DefaultWeightedEdge>(fdp.graph);
		List<Integer> sort = new ArrayList<Integer>();
		while(tpSortIter.hasNext()){
			Integer ver = tpSortIter.next();
			System.out.println(ver);
			sort.add(ver);
		}
		
		List<DefaultWeightedEdge> subPath = new ArrayList<DefaultWeightedEdge>();
		boolean hasHPath = true;
		double weight = 0;
		for(int i = 0; i < sort.size() - 1; i++){
			if(!fdp.graph.containsEdge(sort.get(i), sort.get(i+1))){
				hasHPath = false;
				break;
			}else{
				double temp = 
				weight += fdp.graph.getEdge(sort.get(i), sort.get(i+1)).getWeight();
				subPath.add(fdp.graph.getEdge(sort.get(i), sort.get(i+1)));
			}
		}
//		
		if(!hasHPath){
			subPath = null;
			weight = 0;
		}else{
			System.out.println(subPath);
			System.out.println(weight);
		}
	}
}
