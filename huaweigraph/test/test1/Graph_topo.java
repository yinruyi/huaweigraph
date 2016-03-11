
package com.mapbar.structure;

/**
 * 
 * Class Graph_topo
 * 
 * Description ����ͼ����������
 * 
 * Company mapbar
 * 
 * author Chenll E-mail: Chenll@mapbar.com
 * 
 * Version 1.0
 * 
 * Date 2011-11-17 ����03:38:27
 */

//����ڵ�
class Vertex{
	public char label;
	
	public boolean isVisited;
	
	public Vertex(char label){
		this.label = label;
		this.isVisited = false;
	}
}

public class Graph_topo {
	//��������
	private Vertex[] vArr;
	
	//�����ڽӾ���
	private int[][] adjMat;
	
	 //����������Ŀ 
    private int maxSize; 
    
     //��ǰ���� �±�
    private int currVertex;
    
    private char[] sortedArr;
    
    //���췽��
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
    
    //���һ������
    public void addVertex(char label){
    	vArr[currVertex++] = new Vertex(label);
    }
    
    //���һ����
    public void addEdge(int start,int end){
    	adjMat[start][end] = 1;
    }
    
    //��ʾһ������
    public void disVertex(int v){
    	System.out.print(vArr[v].label+",");
    }
    
    //��������
   public void topo(){
	   int orig_nVert = currVertex;
	   while(currVertex>0){
		   int cuVertex = noSuc();
		   if(currVertex==-1){
			   System.out.println("ͼ���л�·�����ܽ�����������");
			   return;
		   }
		   sortedArr[currVertex-1] = vArr[cuVertex].label;
		   deleteVertex(cuVertex);
	   }
	   disAllVertex(orig_nVert);
   }
   
   //��һ��û�к�̽��
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
   
   //ɾ�����㣬����Ķ�����ǰ�ƶ���ͬʱ�к��дӾ�����ɾ����������к��ұߵ���
   public void deleteVertex(int delV){
	   if(delV != currVertex-1){
		   //ɾ������
		   for(int j = delV; j<currVertex; j++){
			   vArr[j] = vArr[j+1];
		   }
		   
		   //ɾ���������ڵ��ڽӾ���
		   //����
		   for(int row = delV; row<currVertex-1; row++){
			   for(int col = 0; col<currVertex;col++){
				   adjMat[row][col] = adjMat[row+1][col];
			   }
		   }
		   
		   //����
		   for(int col = delV; col<currVertex-1; col++){
			   for(int row = 0; row<currVertex;row++){
				   adjMat[row][col] = adjMat[row][col+1];
			   }
		   }
		   
	   }
	   currVertex--;
   }
   
	//��ʾ��������Ľ��
	public void disAllVertex(int org_length){
		for(int j = 0; j<org_length; j++){
			System.out.print(sortedArr[j]+",");
		}
	}
	   
    //��������
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

