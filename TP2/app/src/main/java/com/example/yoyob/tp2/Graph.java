package com.example.yoyob.tp2;

import android.graphics.PathMeasure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

public class Graph implements Serializable {

    public int MAX_X, MAX_Y, MAX_ID_NODE = -1, MAX_ID_ARC = -1;
    private List<Node> nodes = new ArrayList<Node>();/*Les nodes du graphe*/
    private List<Arc> arcs = new ArrayList<Arc>();/*Les arcs du graphe*/

    /**
     * Initialise un graphe avec neuf noeuds en fct de la taille de l'écran
     * @param height hauteur de l'écran
     * @param width largeur de l'écran
     */
    public Graph(int height, int width) {
        MAX_X = height;
        MAX_Y = width;
        int nodeSize = MAX_X / 15;
        int x = 0 ,y = 0;

        for(int i = 0; i < 9; i++){
            if(i == 0 || i == 3 || i == 6)
                x = 50;
            else if(i == 1 || i == 4 || i == 7)
                x = (MAX_X - 100 + nodeSize)/2;
            else
                x = MAX_X - 50 - nodeSize;
            if(i == 0 || i == 1 || i == 2)
                y = 50;
            else if(i == 3 || i == 4 || i == 5)
                y = (MAX_Y - 100 + nodeSize)/2;
            else
                y = MAX_Y - 50 - nodeSize;
            Node node = new Node(x,y,nodeSize,nodeSize,R.color.colorRectangle);
            node.setId(i+1);
            node.setEtiquette("n"+(i+1));
            nodes.add(node);
        }
        MAX_ID_NODE = 9;
    }

    /**
     * @return ArrayList de tous les nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes){
        this.nodes = nodes;
    }

    public void AddNode(Node n){
        if(n.getId() <= 0){
            if(MAX_ID_NODE == -1){
                n.setId(1);
                MAX_ID_NODE = 1;
            }else{
                n.setId(MAX_ID_NODE+1);
                MAX_ID_NODE++;
            }
        }
        n.setHeight(MAX_X / 15);
        this.nodes.add(n);
    }
    /**
     * @param node le noeud à supprimer
     */
    public void removeNode(Node node){
        this.nodes.remove(node);
        ArrayList<Arc> arcsToRemove = new ArrayList<Arc>();
        for(Arc arc : arcs){
            if(arc.getNodeDep() == node || arc.getNodeArr() == node){
                arcsToRemove.add(arc);
            }
        }
        for (Arc arc : arcsToRemove){
            removeArc(arc);
        }
    }
    /**
     * @return ArrayList de tous les arcs
     */
    public List<Arc> getArcs() {
        return arcs;
    }

    public void setArcs(ArrayList<Arc> arcs){
        this.arcs = arcs;
    }
    /**
     * @param arc l'arc à ajouter
     */
    public void addArc(Arc arc){
        if(arc.getId() <= 0){
            if(MAX_ID_ARC == -1){
                arc.setId(1);
                MAX_ID_ARC = 1;
            }else{
                arc.setId(MAX_ID_ARC+1);
                MAX_ID_ARC++;
            }
        }
        this.arcs.add(arc);
    }

    /**
     * @param arc l'arc à supprimer
     */
    public void removeArc(Arc arc){
        this.arcs.remove(arc);
    }

    /**
     * @param x coordonnées (x,y)
     * @param y coordonnées (x,y)
     * @return le noeud sélectionné s'il y en a un, null sinon
     */
    public Node nodeSelected(int x, int y){
        Node selected = null;
        for (Node n: nodes) {
            if(x > n.getX() && x < n.getX()+n.getWidth() && y > n.getY() && y < n.getY()+n.getWidth())
                selected = n;
        }
        return selected;
    }
    /**
     * @param x coordonnées (x,y)
     * @param y coordonnées (x,y)
     * @return le milieu de l'arc sélectionné s'il y en a un, null sinon
     */
    public Arc midArcSelected(int x, int y){
        Arc selected = null;
        for (Arc a: arcs) {
            if(x > a.getPathMidX()-30 && x < a.getPathMidX()+30 && y > a.getPathMidY()-30 && y < a.getPathMidY()+30)
                selected = a;
            float[] midPoint = {0f, 0f};
            float[] tangent = {0f, 0f};
            PathMeasure pm = new PathMeasure(a.getPath(), false);
            pm.getPosTan(pm.getLength() * 0.5f, midPoint, tangent);
            if(x > midPoint[0]-30 && x < midPoint[0]+30 && y > midPoint[1]-30 && y < midPoint[1]+30)
                selected = a;
        }
        return selected;
    }

    /**
     *
     * @param node
     * @return la liste des arcs en relation avec node
     */
    public ArrayList<Arc> getArcForNode(Node node){
        ArrayList<Arc> res = new ArrayList<Arc>();
        Iterator<Arc> it = this.arcs.iterator();
        Arc arc;
        while(it.hasNext()){
            arc = it.next();
            if(arc.getNodeArr() == node || arc.getNodeDep() == node){
                res.add(arc);
            }
        }
        return res;
    }
}
