package com.wu.server.navmesh.navigation;

import com.wu.server.navmesh.mesh.Triangle;

public class Node implements Comparable<Node> {
    //������
    public Triangle triangle;
    //���ڵ�
    public Node parentNode = null;
    // G���Ǹ�׼ȷ��ֵ������㵽��ǰ���Ĵ��ۣ�δ����ֵΪ0
    public float G = 0;
    // H���Ǹ���ֵ����ǰ��㵽Ŀ�Ľ���Ԥ�ƴ�
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
     * �̳�Comparable�ӿڣ��ں������ȶ���������
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
