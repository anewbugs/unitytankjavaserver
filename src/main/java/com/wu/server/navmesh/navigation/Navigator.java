package com.wu.server.navmesh.navigation;


import com.wu.server.navmesh.mesh.Point;
import com.wu.server.navmesh.mesh.Triangle;
import java.util.*;

/**
 * ������
 */
public class Navigator {
    Queue<Node> openList = new PriorityQueue<Node>(); // ���ȶ���(����) ��������ӿ�Comparable
    List<Node> closeList = new ArrayList<Node>();
    Route route;


    public Navigator() {
    }

    /**
     * ��ʼѰ·
     *
     */
    public void start(Route route){
        if (route.navMeshInfo == null) {
            // todo ��ӡ������־
            return;
        }

        //��ʼ��
        this.route = route;
        openList.clear();
        closeList.clear();

        //��ʼ
        openList.add(new Node(route.startTriangle,route.getH(route.startTriangle)));
        moveNodes();
        route.init();
    }

    private void moveNodes() {
        while ( !openList.isEmpty()){
            if (route.endNode != null) return;
            Node node = openList.poll();
            closeList.add(node);
            //Ѱ·����
            if (route.isEnd(node)){
                route.endNode =node;
                break;
            }

            Explore(node);
        }
    }
    //�ڽ�������̽��
    private void Explore(Node node) {
        //��ȡ�ڽ�������
        HashSet<Triangle> contiguousTriangle = new HashSet<Triangle>();
//        contiguousTriangle.addAll(route.navMeshInfo.pointIndexes.get(node.triangle.a));
//        contiguousTriangle.addAll(route.navMeshInfo.pointIndexes.get(node.triangle.b));
//        contiguousTriangle.addAll(route.navMeshInfo.pointIndexes.get(node.triangle.c));

        //todo test
        contiguousTriangle.addAll(route.navMeshInfo.edgeIndexes.get(Point.midpointString(node.triangle.a, node.triangle.b)));
        contiguousTriangle.addAll(route.navMeshInfo.edgeIndexes.get(Point.midpointString(node.triangle.a, node.triangle.c)));
        contiguousTriangle.addAll(route.navMeshInfo.edgeIndexes.get(Point.midpointString(node.triangle.b, node.triangle.c)));

        //�����ڽ�������

        for (Triangle triangle : contiguousTriangle) {
            //ȥ����ǰ������
            if (triangle.equals(node.triangle))  continue;
            //ȥ��closeList��
            if (closeListContains(triangle))   continue;

            Node child = findNodeInOpen(triangle);
            if (null == child){
                child = new Node(triangle,node,route.getH(triangle));

                openList.add(child);
                //�ҵ���·����
//                if (route.endTriangle.equals(triangle)){
//                    route.endNode = child;
//                    return;
//                }
            }else{
                //�ж��Ƿ��޸ĸ��ڵ�
                float G = node.calculateG(triangle);
                float H = route.getH(triangle);
                if (G + H < child.G + child.H){
                    //�޸�
                    child.parentNode = node;
                    child.G = G;
                    child.H = H;
                }

            }
            
        }

    }

    //�ж����Ѿ��رյĽڵ���
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
