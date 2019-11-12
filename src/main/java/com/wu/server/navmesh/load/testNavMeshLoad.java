package com.wu.server.navmesh.load;


import com.wu.server.navmesh.mesh.NavMeshInfo;
import com.wu.server.navmesh.mesh.Point;

public class testNavMeshLoad {
    public static void main(String[] args) {
       NavMeshLoad navMeshLoad = new NavMeshLoad();
       navMeshLoad.equals(null);
       NavMeshInfo navMeshInfo = navMeshLoad.load("Mian.obj");
        long t = System.currentTimeMillis();
        System.out.println(navMeshInfo.getTriangleByPoint(new Point(0f ,0 ,0f)));
        System.out.println(System.currentTimeMillis() - t);
    }
}
