
package com.mapbar.structure;

/**
 * 
 * Class Graph_topo
 * 
 * Description 有向图的拓扑排序
 * 
 * Company mapbar
 * 
 * author Chenll E-mail: Chenll@mapbar.com
 * 
 * Version 1.0
 * 
 * Date 2011-11-17 下午03:38:27
 */

//定义节点
class Vertex{
	public char label;
	
	public boolean isVisited;
	
	public Vertex(char label){
		this.label = label;
		this.isVisited = false;
	}
}

public class Graph_topo {
	//顶点数组
	private Vertex[] vArr;
	
	//定义邻接矩阵
	private int[][] adjMat;
	
	 //顶点的最大数目 
    private int maxSize; 
    
     //当前顶点 下标
    private int currVertex;
    
    private char[] sortedArr;
    
    //构造方法
    public Graph_topo(int maxSize){
    	sortedArr = new char[maxSize];
    	this.maxSize = maxSize;
    	vArr = new Vertex[maxSize];
    	adjMat = new int[maxSize][maxSize];
    	for (int i = 0; i<adjMat.length; i++){
    		for(int j = 0; j<adjMat.length; j++){
    			adjMat[i][j] = 0;
    		}
    	}
    	currVertex = 0;
    }
    
    //添加一个顶点
    public void addVertex(char label){
    	vArr[currVertex++] = new Vertex(label);
    }
    
    //添加一个边
    public void addEdge(int start,int end){
    	adjMat[start][end] = 1;
    }
    
    //显示一个顶点
    public void disVertex(int v){
    	System.out.print(vArr[v].label+",");
    }
    
    //拓扑排序
   public void topo(){
	   int orig_nVert = currVertex;
	   while(currVertex>0){
		   int cuVertex = noSuc();
		   if(currVertex==-1){
			   System.out.println("图中有环路，不能进行拓扑排序");
			   return;
		   }
		   sortedArr[currVertex-1] = vArr[cuVertex].label;
		   deleteVertex(cuVertex);
	   }
	   disAllVertex(orig_nVert);
   }
   
   //找一个没有后继结点
   public int noSuc(){
	   boolean isEdge;
	   for(int i = 0; i<currVertex; i++){
		   isEdge = false;
		   for(int j = 0; j<currVertex; j++){
			   if(adjMat[i][j]>0){
				   isEdge = true;
				   break;
			   }
		   }
		   if(!isEdge){
			   return i;
		   }
	   }
	   return -1;
   }
   
   //删除顶点，后面的顶点向前移动，同时行和列从矩阵中删除，下面的行和右边的列
   public void deleteVertex(int delV){
	   if(delV != currVertex-1){
		   //删除顶点
		   for(int j = delV; j<currVertex; j++){
			   vArr[j] = vArr[j+1];
		   }
		   
		   //删除顶点所在的邻接矩阵
		   //上移
		   for(int row = delV; row<currVertex-1; row++){
			   for(int col = 0; col<currVertex;col++){
				   adjMat[row][col] = adjMat[row+1][col];
			   }
		   }
		   
		   //左移
		   for(int col = delV; col<currVertex-1; col++){
			   for(int row = 0; row<currVertex;row++){
				   adjMat[row][col] = adjMat[row][col+1];
			   }
		   }
		   
	   }
	   currVertex--;
   }
   
	//显示拓扑排序的结果
	public void disAllVertex(int org_length){
		for(int j = 0; j<org_length; j++){
			System.out.print(sortedArr[j]+",");
		}
	}
	   
    //主调函数
    public static void main(String[] args){
    	Graph_topo g = new Graph_topo(10);
    	g.addVertex('a');
    	g.addVertex('b');
    	g.addVertex('c');
    	g.addVertex('d');
    	g.addVertex('e');
    	g.addVertex('f');   	
    	g.addEdge(0,1);
    	g.addEdge(0,2);
    	g.addEdge(1,3);
    	g.addEdge(4,5);
    	g.addEdge(5,1);
    	g.topo();
    }
}

