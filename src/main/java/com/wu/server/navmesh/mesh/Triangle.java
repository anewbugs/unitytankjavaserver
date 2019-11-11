package com.wu.server.navmesh.mesh;

public class Triangle {
    public static float Y_MAX = 0.5f;
    //三角形三点：a,b,c
    public Point a;
    public Point b;
    public Point c;

    //三角形重心坐标】
    public Point centroid;

    /**
     * 构造三角形
     * @param a
     * @param b
     * @param c
     */
    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
        //重心初始化
        //计算重心
        centroid = new Point( (a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3);
    }

    /**
     * 确定point这个点这这个三角形内
     * 三角形平面化，去除高度的影响 高度：y
     * @param point
     * @return
     */
    public boolean checkInThis(Point point){
       Point AP = Point.vector(this.a,point);
       Point AC = Point.vector(this.a, this.c);
       Point AB = Point.vector(this.a,this.b);

       float u = (AP.x * AB.z - AB.x * AP.z) / (AC.x * AB.z - AB.x * AC.z);
       float v = (AP.x * AC.z - AC.x * AP.z) / (AB.x * AC.z - AC.x * AB.z);
       return u >= 0 && v >= 0 && u + v <= 1 ;

    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", centroid=" + centroid +
                '}';
    }



}
