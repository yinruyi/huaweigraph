package com.other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedSubgraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import com.filetool.util.LogUtil;
import com.other.hamiltonian_path.HamiltonPathFinder;



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
	public List<List<DefaultWeightedEdge>> subPathList = new ArrayList<List<DefaultWeightedEdge>>();
	public List<List<DefaultWeightedEdge>> finalPathList = new ArrayList<List<DefaultWeightedEdge>>();
	public List<Double> weightList = new ArrayList<Double>();
	
	
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
//		System.out.println(M);
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
							e.setId(-1);
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
//			System.out.println("**The virtual edges**");
//			System.out.println(this.virSet);
		}
		
	}
	

	
	
	/**
	 * find all the Hamiltonian path for a directed weighted graph
	 * @param graph
	 */
	public void findHamiltonianPath(){
		
		
		
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = this.graph;
//		System.out.println(graph.edgeSet().size());
		Set<Integer> vSet = graph.vertexSet();
		Set<Integer> restSet = new HashSet(vSet);
		//construct a subgraph of V' in graph
		List<Integer> vertexs = this.demand.get(1);
		Set<Integer> vertexSubset = new HashSet<Integer>();
		Set<DefaultWeightedEdge> edgeSubset = new HashSet<DefaultWeightedEdge>();
		for(int i = 0; i < vertexs.size(); i++){
			vertexSubset.add(vertexs.get(i));
			
//			for(int j = 0; j < vertexs.size() && i != j; j++){
//				
//				if(graph.containsEdge(vertexs.get(i), vertexs.get(j))){
//					edgeSubset.add(graph.getEdge(vertexs.get(i), vertexs.get(j)));
//				}
//				if(graph.containsEdge(vertexs.get(j), vertexs.get(i))){
//					edgeSubset.add(graph.getEdge(vertexs.get(j), vertexs.get(i)));
//				}
//			}
		}
		
		restSet.removeAll(vertexSubset);
		DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> subgraph = this.constructSubgraph(restSet);
//		System.out.println(edgeSubset);
		
//		DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> subgraph = new DirectedWeightedSubgraph<Integer, DefaultWeightedEdge>(graph, vertexSubset, edgeSubset);
		
//		System.out.println("**test for subgraph**");
//		System.out.println(subgraph.vertexSet().size());
//		System.out.println(subgraph.edgeSet().size());
//		System.out.println(subgraph.edgeSet());
		
		
		
		HamiltonPathFinder finder = new HamiltonPathFinder(subgraph);
		finder.Hami();
		Set<List<Integer>> hpa = finder.paths;
		System.out.println(hpa);
		
		if(hpa.size() > 0){
			Iterator<List<Integer>> iter = hpa.iterator();
			while(iter.hasNext()){
				List<Integer> path = iter.next();
				List<DefaultWeightedEdge> p = new ArrayList<DefaultWeightedEdge>();
				for(int i = 0; i < path.size() - 1; i++){
					DefaultWeightedEdge e = subgraph.getEdge(path.get(i), path.get(i+1));
					
					p.add(e);
					
				}
				subPathList.add(p);
			}
			
		}else{
			System.out.println("Can't find the path.");
		}
		
		
		
		/*
		
		
		

		
		TarjanSimpleCycles finder = new TarjanSimpleCycles(subgraph);
		
		if(finder.findSimpleCycles().size() > 0){//有环
			
			List<List<Integer>> cycles = finder.findSimpleCycles();
			
			//有环的时候怎么寻找hamilton path
			
			System.out.println(cycles.size());
			
			for(int i = 0; i < cycles.size(); i++){
		
				
			}
			
			
		}else{
		
		//无环
		
			System.out.println("Use topological sort to find hamiltonian path in V'");
			TopologicalOrderIterator<Integer, DefaultWeightedEdge> tpSortIter = new TopologicalOrderIterator<Integer, DefaultWeightedEdge>(subgraph);
			List<Integer> sort = new ArrayList<Integer>();
			
			
			while(tpSortIter.hasNext()){			
				Integer ver = tpSortIter.next();
//				System.out.println(ver);
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
			
		}*/
		
		
//		catch(NoSuchElementException e){
//			System.out.println("There's a cycle in the directed weighted graph");
//			e.printStackTrace();
//		}
		
	}
	
	/**
	 * Inverse the virtual process, that is replacing virtual edge using the real path.
	 * when replacing the virtual edge, remove the virtual edge from the original graph.
	 */
	public void replaceVirtualEdge(){
		
		int num = subPathList.size();
		if(!(num >0)){
			System.out.println("repleaceVirtualEdge Failed.There's no subpath in V'.");
			return;
		}
		
		for(int j = 0; j < this.virSet.size(); j++){
			VirtualEdge tempVE = this.virSet.get(j);
			this.graph.removeEdge((Integer)tempVE.getSource(), (Integer)tempVE.getTarget());
			
		}
		
		System.out.println(graph.edgeSet().size());
		
		for(int i = 0; i < num; i++){
			List<DefaultWeightedEdge> subPath = new ArrayList<DefaultWeightedEdge>(subPathList.get(i));
			Set<DefaultWeightedEdge> virEdge = new HashSet<DefaultWeightedEdge>();
			

			
			Map<Integer, List<DefaultWeightedEdge>> vToR = new HashMap<Integer, List<DefaultWeightedEdge>>();
			List<Integer> veNum = new ArrayList<Integer>();
			for(int j = 0; j < subPath.size(); j++){
				if(subPath.get(j).getId() == -1){				
					DefaultWeightedEdge tempDE = subPath.get(j);
					veNum.add(j);
					boolean flag = true;
					for(int k = 0; k < this.virSet.size(); k++){
						VirtualEdge tempVE = this.virSet.get(k);
						if(tempDE.getSource() == tempVE.getSource() && tempDE.getTarget() == tempVE.getTarget()){
//							virEdge.add(tempDE);
							List<DefaultWeightedEdge> rPath = new ArrayList<DefaultWeightedEdge>(tempVE.getrPath().get(0));
							vToR.put(j, rPath);
							flag = false;
							break;
							
//							int index = 
//							subPath.set(index, rPath.get(0));//首先将原来那条虚拟边去掉，并替换成新的
//							for(int j = 1; j < rPath.size(); j++){
//								index = index + 1;
//								subPath.add(index, rPath.get(j));
//							}
							
						}
					}
					
					if(flag){
						System.out.println("存在结点 的ID为-1，但不在虚拟边集合里" + subPath.get(j).getSource() + "->" + subPath.get(j).getTarget());
					}
					
				}
			}
//			this.graph.removeAllEdges(virEdge);
//			System.out.println(this.graph.vertexSet().size());
//			System.out.println(this.graph.edgeSet().size());//检查图的边有没有缺失
			int increment = 0;
//			System.out.println(subPath);
			for(int j = 0; j < veNum.size(); j++){
				
				int index = veNum.get(j);
//				System.out.println(index);
				subPath.remove(increment + index);
				
				subPath.addAll(increment + index, vToR.get(index));
				increment += vToR.get(index).size() - 1;
				
			}
//			Iterator<Map.Entry<Integer, List<DefaultWeightedEdge>>> iter = vToR.entrySet().iterator();
//			
//			while(iter.hasNext()){
//				Map.Entry<Integer, List<DefaultWeightedEdge>> ele = iter.next();
//				int index = ele.getKey();
//				subPath.remove(increment + index);
//				
//				subPath.addAll(increment + index, ele.getValue());
//				increment += ele.getValue().size() - 1;
//			}
			
//			for(DefaultWeightedEdge e: subPath){
//				if(e.getId() == -1){
//					System.out.println(e.getSource() + "->" + e.getTarget());
//				}
//			}
			subPathList.set(i, subPath);
		}	
	}
	
	public Set<Integer> getAllVertexs(List<DefaultWeightedEdge> list){
		Set<Integer> set = new HashSet<Integer>();
		for(int i = 0; i < list.size(); i++){
			set.add((Integer) list.get(i).getSource());
			set.add((Integer) list.get(i).getTarget());
		}
		
		return set;
	}
	
	/**
	 * get the final path
	 */
	public double combinePath(){
		
		if(this.subPath == null){
//			System.out.println("combinePath failed.There's no subpath in V'.");
			return -1.0;
		}
		
//		System.out.println("First, we need to find the best source_from path and to_target path.");
		List<List<DefaultWeightedEdge>> result = new ArrayList<List<DefaultWeightedEdge>>();
		
//		System.out.println(this.subPath);
		int source = this.demand.get(0).get(0);
		int midS = (int)subPath.get(0).getSource();
		int midT = (int)subPath.get(subPath.size() - 1).getTarget();
		int target = this.demand.get(0).get(1);
		
		Set<Integer> vOfSubpath = this.getAllVertexs(subPath);
		vOfSubpath.remove(midS);
		vOfSubpath.remove(midT);
		
		
		double weight13 = 0;
		double weight31 = 0;
		boolean flag1 = true;
		boolean flag2 = true;
		//这里要处理一个问题：即dj1和dj2可能会经过V’中的点。所以要先将图中的V‘中的点去掉，再找shortest
		
		
		/*先1后3*/
		Set<Integer> remove1 = new HashSet<Integer>(vOfSubpath);
		remove1.add(midT);
		remove1.add(target);
		remove1.remove(source);
		DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> tempGraph1 = this.constructSubgraph(remove1);
		
		
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dj1 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(tempGraph1, source, midS);
		
		if(dj1.getPath() != null){
			List<DefaultWeightedEdge> prior = dj1.getPathEdgeList();
			
			Set<Integer> vOfPrior = this.getAllVertexs(prior);
			Set<Integer> remove2 = new HashSet<Integer>(vOfPrior);
			remove2.addAll(vOfSubpath);
			DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> tempGraph2 = this.constructSubgraph(remove2);

			
			
			DijkstraShortestPath<Integer, DefaultWeightedEdge> dj2 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(tempGraph2, midT, target);
			
			if(dj2.getPath() != null){
				List<DefaultWeightedEdge> next = dj2.getPathEdgeList();
				weight13 += dj1.getPathLength() + dj2.getPathLength();
				result.add(prior);
				result.add(subPath);
				result.add(next);
			}else{
				flag1 = false;
			}
		}else{
			flag1 = false;
		}
		
		
		/*先3后1*/
		Set<Integer> remove3 = new HashSet<Integer>(vOfSubpath);
		remove3.add(midS);
		remove3.add(source);
		remove3.remove(target);
		DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> tempGraph3 = this.constructSubgraph(remove3);
		
//		if(!tempGraph3.containsVertex(target)){
//			System.out.println("原图没有终点");
//		}
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dj3 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(tempGraph3, midT, target);
		
		if(dj3.getPath() != null){
			List<DefaultWeightedEdge> next = dj3.getPathEdgeList();
			
			Set<Integer> vOfNext = this.getAllVertexs(next);
			
			
			Set<Integer> remove4 = new HashSet<Integer>(vOfSubpath);
			remove4.addAll(vOfNext);
			remove4.remove(source);
			DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> tempGraph4 = this.constructSubgraph(remove4);
			
			
			DijkstraShortestPath<Integer, DefaultWeightedEdge> dj4 = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(tempGraph4, source, midS);
			
			if(dj4.getPath() != null){
				List<DefaultWeightedEdge> prior = dj4.getPathEdgeList();
				weight31 += dj3.getPathLength() + dj4.getPathLength();
				
				if(weight31 < weight13){
					weight13 = weight31;
					result.set(0,prior);
					result.set(2, next);
				}
			}else{
				flag2 = false;
			}
		}else{
			flag2 = false;
		}
		
		
		
		if(flag1 == false && flag2 == false){
//			System.out.println("CombinePath failed.\nThere's no suitable path.");
			return -1.0;
		}else{
//			System.out.println("Then, we need to combine the three paths into one.");
			for(int j = 0; j < result.size(); j++){
				this.path.addAll(result.get(j));
				
			}
//			System.out.println(path);
			finalPathList.add(new ArrayList<DefaultWeightedEdge>(path));
			weightList.add(weight13);
//			System.out.println("Get the path successfully. And the weight of path:");
//			System.out.println(path);
//			System.out.println(weight13);
			return weight13;
		}
	}
	
	public DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> constructSubgraph(Set<Integer> remove){
		Set<Integer> vSet = new HashSet<Integer>(graph.vertexSet());
		Set<DefaultWeightedEdge> eSet = new HashSet<DefaultWeightedEdge>(graph.edgeSet());
		
		vSet.removeAll(remove);
		Iterator<Integer> iter = remove.iterator();
		while(iter.hasNext()){
			int v = iter.next();
			eSet.removeAll(graph.incomingEdgesOf(v));
			eSet.removeAll(graph.outgoingEdgesOf(v));
		}
		DirectedWeightedSubgraph<Integer, DefaultWeightedEdge> subgraph = new DirectedWeightedSubgraph<Integer, DefaultWeightedEdge>(graph, vSet, eSet);
		return subgraph;
	}
	public int MinWeightPath(){
		
		int num = subPathList.size();
		if(!(num >0)){
			System.out.println(this.getClass().getName() + "Failed.There's no subpath in V'.");
			return 0;
		}
		
		
		
		
		
		for(int i = 0; i < num; i++){
			this.path.clear();
			this.subPath = subPathList.get(i);
			this.combinePath();			
		}
		
		if(!(finalPathList.size()> 0)){
			System.out.println("Can't find the path.");
			this.path = null;
			return -1;
		}
		double minWeight = Double.MAX_VALUE;
		int idx = -1;
		for(int i = 0; i < weightList.size(); i++){
			if(minWeight > weightList.get(i)){
				minWeight = weightList.get(i);
				idx = i;
			}
		}
		if(minWeight > 0){
			path = finalPathList.get(idx);
			pathLength = minWeight;
			System.out.println("*******************************");
			System.out.println("Successfully get the min weighted path.");
			System.out.println("The path is:" + path);
			System.out.println("The weight is :" + minWeight);
			return 1;
		}

		return -1;
	}
	public String formatString() {
		List<DefaultWeightedEdge> vSeq = this.path;
		System.out.println(vSeq);
		String result = "";
		result += ((Integer)(vSeq.get(0).getId())).toString();
		for(int i=1;i< vSeq.size();i++){  
//			if(vSeq.get(i).getId() == -1){
//				System.out.println(vSeq.get(i).getSource() + "->" + vSeq.get(i).getTarget());
//			}
			result += ("|" + ((Integer)(vSeq.get(i).getId())).toString());  
		} 
		return result;
	}
	
	public static void Main(String[] arg) throws IOException{
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
	
//	public static void main(String[] arg){
//		ArrayList<Integer> l = new ArrayList<Integer>();
//		for(int i = 0; i < 10; i++){
//			l.add(i);
//		}
//		l.set(2, -1);
//		l.set(5, -1);
//		l.set(8, -1);
//		System.out.println(l);
//		ArrayList<Integer> in1 = new ArrayList<Integer>();
//		in1.add(100);
//		ArrayList<Integer> in2 = new ArrayList<Integer>();
//		in2.add(101);in2.add(101);
//		ArrayList<Integer> in3 = new ArrayList<Integer>();
//		in3.add(102);in3.add(102);in3.add(102);
//		ArrayList<ArrayList<Integer>> t = new ArrayList<ArrayList<Integer>>();
//		t.add(in1);
//		t.add(in2);
//		t.add(in3);
//		int[] ids = {2,5,8};
//		int increment = 0;
//		for(int j = 0; j < ids.length; j++){
//			
//			int index = ids[j];
////			System.out.println(index);
//			l.remove(increment + index);
//			
//			l.addAll(increment + index, t.get(j));
//			System.out.println(l);
//			increment += (t.get(j)).size() - 1;
//			
//		}
//		
////		System.out.println(l);
//	}
}
