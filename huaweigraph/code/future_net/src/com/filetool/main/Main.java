package com.filetool.main;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import com.filetool.util.FileUtil;
import com.filetool.util.LogUtil;
import com.routesearch.route.Route;

/**
 * 工具入口
 * 
 * @author
 * @since 2016-3-1
 * @version v1.0
 */
public class Main
{
    public static void main(String[] args) throws CycleFoundException
    {
//        if (args.length != 3)
//        {
//            System.err.println("please input args: graphFilePath, conditionFilePath, resultFilePath");
//            return;
//        }

        //String graphFilePath = args[0];
//    	String path = "F:/test-case/newcase/test2/";
    	String path = "F:/test-case/newcase/test100 700 5/";
//    	String path = "F:\\contest\\huaweigraph\\test-case\\case0\\";
        String graphFilePath = path + "topo.csv";
        //String conditionFilePath = args[1];
        String conditionFilePath = path + "demand.csv";
        //String resultFilePath = args[2];
        String resultFilePath = path + "myresult.csv";

        LogUtil.printLog("Begin");

        // 读取输入文件
        
        System.out.println("读入的图为：");
        String graphContent = FileUtil.read(graphFilePath, null);
        
        System.out.println("读入的条件为：");
        String conditionContent = FileUtil.read(conditionFilePath, null);

        // 功能实现入口
        String resultStr = Route.searchRoute(graphContent, conditionContent);

        // 写入输出文件
        FileUtil.write(resultFilePath, resultStr, false);

        LogUtil.printLog("End");
    }

}
