package com.wu.server.navmesh.mesh;


import com.wu.server.navmesh.config.PointConfig;

public class Point {
    public float x ;
    public float y ;//高度坐标
    public float z ;
    // 传入参数可能是浮点数，需修改
    public Point(float x, float y, float z) {
        this.x =  (x * PointConfig.precision);
        this.y =  (y * PointConfig.precision);
        this.z =  (z * PointConfig.precision);
    }

    public static Point vector(Point A, Point B){
        return new Point(B.x - A.x, B.y - A.y, B.z - A.z);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    //获取点之间的距离
    public float getDistance(Point point){
      return (float) Math.sqrt(Math.pow(this.x - point.x , 2) + Math.pow(this.z - this.z ,2));
    }

    public static String midpointString(Point a, Point b){
        float x = (a.x + b.x) / 2;
        float y = (a.y + b.y) / 2;
        float z = (a.z + b.z) / 2;

        return x + " " + y + " " + z;
    }

    public static Point unitVector(Point A ,Point B){
        Point point = new Point(B.x - A.x,B.y - A.y,B.z -A.z);
        float distance = (float) Math.sqrt(Math.pow(B.x - A.x , 2) + Math.pow(B.z - A.z ,2));
        point.x /= distance;
        point.z /= distance;
        return point;
    }
}
