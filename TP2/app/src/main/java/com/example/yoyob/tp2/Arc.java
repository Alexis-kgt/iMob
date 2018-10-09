package com.example.yoyob.tp2;

import android.graphics.Paint;
import android.graphics.Path;

public class Arc {

    private Node nodeDep, nodeArr;
    private Path path;
    private int id;
    private int color;
    private String name;
    private float width = 10f;

    /**
     * Constructeur
     * @param name le nom de l'arc, correspond à son étiquette
     * @param path le chemin correspondant
     */
    public Arc(String name, Path path){
        this.path = path;
        this.name = name;
        this.color = R.color.colorArc;
    }

    /**
     * @return nodeDep le noeud de départ
     */
    public Node getNodeDep() {
        return nodeDep;
    }

    /**
     * @param nodeDep le noeud de départ
     */
    public void setNodeDep(Node nodeDep) {
        this.nodeDep = nodeDep;
    }

    /**
     * @return nodeArr le noeud d'arrivée
     */
    public Node getNodeArr() {
        return nodeArr;
    }

    /**
     * @param nodeArr le noeud d'arrivée
     */
    public void setNodeArr(Node nodeArr) {
        this.nodeArr = nodeArr;
    }

    /**
     * @return l'epaisseur de l'arc
     */
    public float getWidth() {
        return width;
    }

    /**
     * @param width l'epaisseur de l'arc
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Met à jour l'objet Path en fonction de la position des noeuds
     */
    public void updatePath(){
        if(nodeArr != null){
            this.path = new Path();
            int depX = nodeDep.getX() + (nodeDep.getWidth() / 2);
            int depY = nodeDep.getY() + (nodeDep.getHeight() / 2);
            int arrX = nodeArr.getX() + (nodeArr.getWidth() / 2);
            int arrY = nodeArr.getY() + (nodeArr.getHeight() / 2);
            this.path.moveTo(depX,depY);
            int midX = (depX + arrX) / 2;
            int midY = (depY + arrY) / 2;
            this.path.quadTo(midX,midY,arrX,arrY);
        }
    }

    /**
     * Met à jour le chemin d'un arc temporaire
     * @param movex la coordonnée x du doigt de l'utilisateur
     * @param movey la coordonnée y du doigt de l'utilisateur
     */
    public void updatePath(int movex, int movey){
        this.path = new Path();
        int depX = nodeDep.getX() + (nodeDep.getWidth() / 2);
        int depY = nodeDep.getY() + (nodeDep.getHeight() / 2);
        this.path.moveTo(depX,depY);
        int midX = (depX + movex)/2;
        int midY = (depY + movey)/2;
        this.path.quadTo(midX,midY,movex,movey);
    }

    /**
     * @return path le chemin de l'arc
     */
    public Path getPath() {
        return path;
    }

    /**
     * @param path le chemin de l'arc
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * @return id l'id de l'arc
     */
    public int getId() {
        return id;
    }

    /**
     * @param id l'id de l'arc
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return la couleur de l'arc
     */
    public int getColor(){
        return this.color;
    }

    /**
     * @param color la couleur de l'arc
     */
    public void setColor(int color){
        this.color = color;
    }

    /**
     * @return le nom de l'arc
     */
    public String getName() {
        return name;
    }

    /**
     * @param name le nom de l'arc
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return la string descriptive de l'arc
     */
    @Override
    public String toString() {
        return "Arc{" +
                "nodeDep=" + nodeDep +
                ", nodeArr=" + nodeArr +
                ", path=" + path +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
