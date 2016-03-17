/**
 * 实现代码文件
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.filetool.util.LogUtil;
import com.other.FileOperation;
import com.other.FindDemandPath;

public final class Route
{
	
	
	
    /**
     * 你需要完成功能的入口
     * 
     * @author XXX
     * @since 2016-3-4
     * @version V1
     */
    public static String searchRoute(String graphContent, String condition) throws DirectedAcyclicGraph.CycleFoundException
    {
        
        FindDemandPath fdp = new FindDemandPath();
		
        fdp.graph = FileOperation.getGraph(graphContent);
		
		fdp.demand = FileOperation.getCondition(condition);
		System.out.println(fdp.graph.edgeSet().size());

//		System.out.println(fdp.demand);
		
		
//		LogUtil.printLog("\n\nStep1:Create virtual edge->");
		fdp.createVirtualEdge();
		
		
//		LogUtil.printLog("\n\nStep2:Find hamiltonian path in V'->");
		fdp.findHamiltonianPath();
		
		fdp.replaceVirtualEdges();
	
		fdp.MinWeightPath();

		
		
//		fdp.combinePath();
		
		if(fdp.path.size() > 0){
			String path = fdp.formatString();
			System.out.println(path);
			return path;
		}else{
			return "";
		}
		
		
        
    }

}