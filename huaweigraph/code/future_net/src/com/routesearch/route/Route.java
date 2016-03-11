/**
 * 实现代码文件
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

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
    public static String searchRoute(String graphContent, String condition)
    {
//        return "hello world!";
        
        FindDemandPath fdp = new FindDemandPath();
		
		fdp.graph = FileOperation.getGraph(graphContent);
		fdp.demand = FileOperation.getCondition(condition);
		
		fdp.run();
		
		return fdp.path.toString();
        
    }

}