package com.wu.server.navmesh.load;

import com.wu.server.navmesh.mesh.NavMeshInfo;
import com.wu.server.navmesh.mesh.Point;
import com.wu.server.navmesh.mesh.Triangle;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public  class NavMeshLoad {
    //List<Point> allPoint = new ArrayList<>();
    ArrayList<String> allStrPoint = new ArrayList<>();
    HashMap<String , Point> pointHashMap = new HashMap<>();

//    public static List<Index> allIndex = new ArrayList<Index>();


    /// 读取导航网格信息
    public NavMeshInfo load(String path)
    {
        List<String> fileInfo = loadFile(path);

        NavMeshInfo navMeshInfo = readInfo(fileInfo);

        return navMeshInfo;
    }

    public List<String> loadFile(String path)  {

        List list = new ArrayList<String>();

        try {

            File file = new File(path);
            InputStream inputStream = this.getClass().getClassLoader(). getResourceAsStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String strLine = null;
            while (null != (strLine = bufferedReader.readLine()) ){
                list.add(strLine);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return list;
    }

    //获取导航网格
    public NavMeshInfo readInfo(List<String> fileInfo){

        NavMeshInfo navMeshInfo = new NavMeshInfo();


        for (int i = 0; i < fileInfo.size(); i++)
        {
            String str = fileInfo.get(i);

            String[] Split = str.split(" ");

            if (Split[0].equals("v"))
            {
                allStrPoint.add(str);
                if (! pointHashMap.containsKey(str)){
                    Point point =new Point(Float.parseFloat(Split[2]), Float.parseFloat(Split[3]), Float.parseFloat(Split[4]));
                    pointHashMap.put(str,point);
                }

            }
            else if (Split[0].equals( "f"))
            {
                int a = Integer.parseInt(Split[1]);
                int b = Integer.parseInt(Split[2]);
                int c = Integer.parseInt(Split[3]);
                //todo
                //allIndex.add(new Index(a,b,c));

                Triangle triangle = createTriangle( a, b, c);
               // navMeshInfo.allCentroid.add(triangle.centroid);
                navMeshInfo.allTriangle.add(triangle);

                //三角形点索引
                addTrianglePointIndexes(navMeshInfo,triangle);

            }
        }

        return navMeshInfo;
    }

    private void addTrianglePointIndexes(NavMeshInfo navMeshInfo, Triangle triangle) {
        addPointIndexes(navMeshInfo.edgeIndexes, triangle.a, triangle.b, triangle);
        addPointIndexes(navMeshInfo.edgeIndexes, triangle.a, triangle.c, triangle);
        addPointIndexes(navMeshInfo.edgeIndexes, triangle.b, triangle.c, triangle);
    }

    //添加顶点索引
    void addPointIndexes(HashMap<String,ArrayList<Triangle>> navMeshInfo, Point a,Point b, Triangle triangle)
    {
        String edge = Point.midpointString(a, b);
        if (navMeshInfo.containsKey(edge))
        {
            navMeshInfo.get(edge).add(triangle);
        }
        else
        {
            ArrayList<Triangle> list = new ArrayList<Triangle>();
            list.add(triangle);
            navMeshInfo.put(edge,  list);
        }


    }

    //根据点索引构造三角形
    public Triangle createTriangle(int a , int b , int c){
        return new Triangle(
                pointHashMap.get(allStrPoint.get(a - 1)),
                pointHashMap.get(allStrPoint.get(b - 1)),
                pointHashMap.get(allStrPoint.get(c - 1)));

    }

}
