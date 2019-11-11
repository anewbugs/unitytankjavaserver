package com.wu.server.navmesh.navigation;

import com.wu.server.navmesh.mesh.Triangle;

public class Node implements Comparable<Node> {
    //三角形
    public Triangle triangle;
    //父节点
    public Node parentNode = null;
    // G：是个准确的值。是起点到当前结点的代价，未定义值为0
    public float G = 0;
    // H：是个估值。当前结点到目的结点的预计代
    public float H;

    public Node(Triangle triangle, float H) {
        this.triangle = triangle;
        H = H;
    }

    public Node(Triangle triangle, Node parentNode,float h) {
        this.triangle = triangle;
        this.parentNode = parentNode;
        G = parentNode.G + parentNode.triangle.centroid.getDistance(triangle.centroid);
        H = h;
    }

    public float calculateG(Triangle triangle){
        return this.G + this.triangle.centroid.getDistance(triangle.centroid);
    }


    /**
     * 继承Comparable接口，在后面优先队列中运用
     * @param o
     * @return
     */
    @Override
    public int compareTo(Node o) {
        if (o == null) return -1;
        if (G + H > o.G + o.H)
            return 1;
        else if (G + H < o.G + o.H) return -1;
        return 0;
    }
}
