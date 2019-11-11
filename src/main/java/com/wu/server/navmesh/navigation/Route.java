package com.wu.server.navmesh.navigation;

import com.wu.server.navmesh.mesh.NavMeshInfo;
import com.wu.server.navmesh.mesh.Point;
import com.wu.server.navmesh.mesh.Triangle;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Route {
    public NavMeshInfo navMeshInfo;
    public LinkedList<Triangle> viaTriangle = new LinkedList<Triangle>();
    public List viaPoint = new ArrayList<Point>(); //todo 路径 优化使用

    public Point startPoint;
    public Point endPoint;
    public Triangle startTriangle;
    public Triangle endTriangle ;
    public Node endNode = null;

    public Route(NavMeshInfo navMeshInfo, Point startPoint, Point endPoint, Triangle startTriangle, Triangle endTriangle) {
        this.navMeshInfo = navMeshInfo;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTriangle = startTriangle;
        this.endTriangle = endTriangle;


    }

    public static Route routeFactory(NavMeshInfo navMeshInfo, Point startPoint, Point endPoint){

        Triangle startTriangle = navMeshInfo.getTriangleByPoint(startPoint);
        Triangle endTriangle = navMeshInfo.getTriangleByPoint(endPoint);

        if (startTriangle != null && endTriangle != null){
            return new Route( navMeshInfo,  startPoint,  endPoint,  startTriangle,  endTriangle);
        }

        return null;

    }

    public boolean isEnd(Node node){
        return node.triangle.equals(endTriangle);
    }

    public float getH(Triangle triangle) {
        return Math.abs(triangle.centroid.x - endPoint.x) + Math.abs(triangle.centroid.z - endPoint.z);
    }

    public void init() {
        Node node = endNode;
        while(node != null){
            viaTriangle.addFirst(node.triangle);
            node = node.parentNode;
        }
    }
}
