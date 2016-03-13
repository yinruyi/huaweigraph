package com.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FileOperation extends DefaultWeightedEdge{

	
	/**
	 * Function: Load file, and get the Graph we need.
	 * @param file
	 * @return a directed weighted graph
	 * @throws IOException
	 */
	public static SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> loadDWGraph(String file) throws IOException{
		
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		File f = new File(file);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while((line = br.readLine()) != null){
			line = line.trim();
			String[] str = line.split(",");
			
			
			Integer v1 = Integer.parseInt(str[1]);
			Integer v2 = Integer.parseInt(str[2]);
			if(!graph.containsVertex(v1)){
				graph.addVertex(v1);
			}
			if(!graph.containsVertex(v2)){
				graph.addVertex(v2);
			}
			double weight = Integer.parseInt(str[3]);
			DefaultWeightedEdge we = new DefaultWeightedEdge();
			graph.setEdgeWeight(we, weight);
			graph.addEdge(v1, v2, we);
			
			
			
		}	
		br.close();
		fr.close();
		System.out.println(graph);
		return graph;
	
	}
	
	
	public static SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> getGraph(String file){
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		String line = file;
		String[] cont = line.split("\n");
		for(int i = 0; i < cont.length; i++){
			String recond = cont[i].trim();
			String[] str = recond.split(",");
			
			Integer id = Integer.parseInt(str[0]);
			Integer v1 = Integer.parseInt(str[1]);
			Integer v2 = Integer.parseInt(str[2]);
			if(!graph.containsVertex(v1)){
				graph.addVertex(v1);
			}
			if(!graph.containsVertex(v2)){
				graph.addVertex(v2);
			}
			double weight = Integer.parseInt(str[3]);
			DefaultWeightedEdge we = new DefaultWeightedEdge();
			we.setId(id);
			graph.setEdgeWeight(we, weight);
			graph.addEdge(v1, v2, we);
			
			
			
		}
//		System.out.println(graph);
		return graph;
	}
	/**
	 * Function: Load the path demand into a list. In the list, the first element stands for the source vertex and the target vertex.
	 * the second element stands for the demand vertex set which the result path has to go through.
	 * @param file: input file path about the demand for the path.
	 * @return Source and target vertex and V' set.
	 * @throws IOException
	 */
	public static List<List<Integer>> loadDemand(String file) throws IOException{
		List<Integer> vSet = new ArrayList<Integer>();
		List<Integer> vD = new ArrayList<Integer>();
		File f = new File(file);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while((line = br.readLine()) != null){
			line = line.trim();
			String[] str = line.split(",");
			vSet.add(Integer.parseInt(str[0]));
			vSet.add(Integer.parseInt(str[1]));
			System.out.println(str[2]);
			String[] v_d = str[2].split("\\|");
//			System.out.println(v_d.length);
			
			for(int i = 0; i < v_d.length; i++){
//				System.out.println(v_d[i]);
				vD.add(Integer.parseInt(v_d[i]));
			}
		}
		List<List<Integer>> input = new ArrayList<List<Integer>>();
		input.add(vSet);
		input.add(vD);
		
//		System.out.println("The source and target vertex is:");
//		System.out.println("\t" + vSet);
//		
//		System.out.println("The subset v is:");
//		System.out.println("\t" + vD);
		
		return input;
	}
	public static List<List<Integer>> getCondition(String file) {
		List<Integer> vSet = new ArrayList<Integer>();
		List<Integer> vD = new ArrayList<Integer>();
		
		file = file.trim();
		String[] str = file.split(",");
		vSet.add(Integer.parseInt(str[0]));
		vSet.add(Integer.parseInt(str[1]));
		System.out.println(str[2]);
		String[] v_d = str[2].split("\\|");
//		System.out.println(v_d.length);
		
		for(int i = 0; i < v_d.length; i++){
//			System.out.println(v_d[i]);
			vD.add(Integer.parseInt(v_d[i]));
		}
		List<List<Integer>> input = new ArrayList<List<Integer>>();
		input.add(vSet);
		input.add(vD);
		
		System.out.println("The source and target vertex is:");
		System.out.println("\t" + vSet);
		
		System.out.println("The subset v is:");
		System.out.println("\t" + vD.toString());
		
		return input;
	}
	/**
	 * Save the final path into file
	 * @param path
	 * @param file
	 */
	public static void savePath(List<Integer> path, String file){
		
		List<Integer> vSeq = path;
		
		try{
			FileOutputStream fout = new FileOutputStream(new File(file)); 
			//fout.write(("Data ID\t Cluster ID").getBytes());
			fout.write(vSeq.get(0).toString().getBytes());
			
			for(int i=1;i< vSeq.size();i++){  
				fout.write(("\\|" + vSeq.get(i)).getBytes());  
			}
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } 
	}
	
	public static void main(String[] args) throws IOException{
		String file = "topo.csv";//the graph file path in your computer
		String demand = "demand.csv";
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = FileOperation.loadDWGraph(file);
		List<List<Integer>> pathD = FileOperation.loadDemand(demand);
		
//		System.out.println(graph.getEdge(0, 3));
		
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<Integer, DefaultWeightedEdge>(graph, pathD.get(0).get(0), pathD.get(0).get(1));
		List<DefaultWeightedEdge> path = dijkstra.getPathEdgeList();
		System.out.println(dijkstra.getPathLength());
		
		
	}
}
