package com.wu.server.navmesh.load;

import navtest.mesh.NavMeshInfo;
import navtest.mesh.Point;

public class testNavMeshLoad {
    public static void main(String[] args) {
       NavMeshLoad navMeshLoad = new NavMeshLoad();
       navMeshLoad.equals(null);
       NavMeshInfo navMeshInfo = navMeshLoad.load("s3-2.obj");
        long t = System.currentTimeMillis();
        System.out.println(navMeshInfo.getTriangleByPoint(new Point(31.89f ,0 ,11.05f)));
        System.out.println(System.currentTimeMillis() - t);
    }
}
