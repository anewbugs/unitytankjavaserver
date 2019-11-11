package com.wu.server.navmesh.mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavMeshInfo {

    //三角形列表
    public List<Triangle> allTriangle = new ArrayList<Triangle> ();
    //三角形重心
    //public List<Point> allCentroid  = new ArrayList<Point> ();
//    // 三角形索引 key点 .value点构成的所有三角形  正常情况为三个
//    public HashMap<Point,ArrayList<Triangle>> pointIndexes = new HashMap<>();
    //三角形边索引
    public HashMap<String,ArrayList<Triangle>> edgeIndexes = new HashMap<>();

    public HashMap<String, ArrayList<Triangle>> strIndexes = new HashMap<>();

    public Triangle getTriangleByPoint(Point point){
        for (Triangle triangle : allTriangle) {
            if (triangle.centroid.y < Triangle.Y_MAX  && triangle.checkInThis(point)){

                return triangle;
            }
        }
        return null;
    }
}
