package com.example.yoyob.tp2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Graph {

    public int MAX_X, MAX_Y;
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
            node.setEtiquette("n"+(i+1));
            nodes.add(node);
        }
    }

    /**
     * @return ArrayList de tous les nodes
     */
    public List<Node> getNodes() {
        return nodes;
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

    /**
     * @param arc l'arc à ajouter
     */
    public void addArc(Arc arc){
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
