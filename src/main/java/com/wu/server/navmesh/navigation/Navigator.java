package com.wu.server.navmesh.navigation;


import com.wu.server.navmesh.mesh.Point;
import com.wu.server.navmesh.mesh.Triangle;
import java.util.*;

/**
 * 导航仪
 */
public class Navigator {
    Queue<Node> openList = new PriorityQueue<Node>(); // 优先队列(升序) 根据这个接口Comparable
    List<Node> closeList = new ArrayList<Node>();
    Route route;


    public Navigator() {
    }

    /**
     * 开始寻路
     *
     */
    public void start(Route route){
        if (route.navMeshInfo == null) {
            // todo 打印报错日志
            return;
        }

        //初始化
        this.route = route;
        openList.clear();
        closeList.clear();

        //开始
        openList.add(new Node(route.startTriangle,route.getH(route.startTriangle)));
        moveNodes();
        route.init();
    }

    private void moveNodes() {
        while ( !openList.isEmpty()){
            if (route.endNode != null) return;
            Node node = openList.poll();
            closeList.add(node);
            //寻路结束
            if (route.isEnd(node)){
                route.endNode =node;
                break;
            }

            Explore(node);
        }
    }
    //邻接三角形探索
    private void Explore(Node node) {
        //获取邻接三角形
        HashSet<Triangle> contiguousTriangle = new HashSet<Triangle>();
//        contiguousTriangle.addAll(route.navMeshInfo.pointIndexes.get(node.triangle.a));
//        contiguousTriangle.addAll(route.navMeshInfo.pointIndexes.get(node.triangle.b));
//        contiguousTriangle.addAll(route.navMeshInfo.pointIndexes.get(node.triangle.c));

        //todo test
        contiguousTriangle.addAll(route.navMeshInfo.edgeIndexes.get(Point.midpointString(node.triangle.a, node.triangle.b)));
        contiguousTriangle.addAll(route.navMeshInfo.edgeIndexes.get(Point.midpointString(node.triangle.a, node.triangle.c)));
        contiguousTriangle.addAll(route.navMeshInfo.edgeIndexes.get(Point.midpointString(node.triangle.b, node.triangle.c)));

        //处理邻接三角形

        for (Triangle triangle : contiguousTriangle) {
            //去掉当前三角形
            if (triangle.equals(node.triangle))  continue;
            //去掉closeList表
            if (closeListContains(triangle))   continue;

            Node child = findNodeInOpen(triangle);
            if (null == child){
                child = new Node(triangle,node,route.getH(triangle));

                openList.add(child);
                //找到出路结束
//                if (route.endTriangle.equals(triangle)){
//                    route.endNode = child;
//                    return;
//                }
            }else{
                //判断是否修改父节点
                float G = node.calculateG(triangle);
                float H = route.getH(triangle);
                if (G + H < child.G + child.H){
                    //修改
                    child.parentNode = node;
                    child.G = G;
                    child.H = H;
                }

            }
            
        }

    }

    //判断是已经关闭的节点吗
    private boolean closeListContains(Triangle triangle){
        if (closeList.isEmpty())  return false;

        for (Node node : closeList) {
           if (node.triangle.equals(triangle)) return true;
        }

        return false;
    }

    private Node findNodeInOpen(Triangle triangle){
        if (openList.isEmpty())  return null;
        for (Node node : openList) {
            if (node.triangle.equals(triangle)) return node;
        }

        return null;
    }

}
