package com.example.yoyob.tp2;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class Arc implements Serializable {

    private Node nodeDep, nodeArr;
    private Path path;
    private int id;
    private int color;
    private int pathMidX, pathMidY;
    private String name;
    private float width = 10f;
    private float textSize = 40f;

    private static HashMap<String,Integer> colors = new HashMap<String,Integer>();

    /**
     * Met en relation le texte d'une couleur avec sa valeur en Integer
     */
    static {
        colors.put("Rouge",Color.RED);
        colors.put("Vert",Color.GREEN);
        colors.put("Bleu",Color.BLUE);
        colors.put("Orange",Color.rgb(255,165,0));
        colors.put("Cyan",Color.CYAN);
        colors.put("Magenta",Color.MAGENTA);
        colors.put("Noir",Color.BLACK);
    }

    public Arc(){

    }

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
     * @return la taille du texte
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * @param textSize la taille du texte
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
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
        //Si l'arc relie bien deux noeud
        if(nodeArr != null){
            //Si l'arc est une boucle
            if(nodeDep == nodeArr){
                this.path = new Path();
                //Les coordonées du milieu du noeud de départ
                int x = nodeDep.getX()+ (nodeDep.getWidth() / 2);
                int y = nodeDep.getY()+ (nodeDep.getHeight() / 2);
                //Si l'arc est plus petit que le noeud, on donne des valeurs par défaut
                if((pathMidX == 0 && pathMidY == 0)
                    ||((pathMidX < nodeDep.getX() + nodeDep.getWidth() && pathMidX > nodeDep.getX())
                        && (pathMidY < nodeDep.getY() + nodeDep.getHeight() && pathMidY > nodeDep.getY()))) {
                    pathMidX = x + 150;
                    pathMidY = y + 150;
                }
                //Création de la boucle
                this.path.moveTo(x, y);
                this.path.quadTo(x+(pathMidX-x), y, pathMidX, pathMidY);
                this.path.moveTo(pathMidX, pathMidY);
                this.path.quadTo(x, y+(pathMidY-y), x, y);
            }
            //Si l'arc n'est pas une boucle
            else{
                this.path = new Path();
                int depX = nodeDep.getX() + (nodeDep.getWidth() / 2);
                int depY = nodeDep.getY() + (nodeDep.getHeight() / 2);
                int arrX = nodeArr.getX() + (nodeArr.getWidth() / 2);
                int arrY = nodeArr.getY() + (nodeArr.getHeight() / 2);
                this.path.moveTo(depX,depY);
                this.path.quadTo(pathMidX,pathMidY,arrX,arrY);
            }
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
        pathMidX = (depX + movex)/2;
        pathMidY = (depY + movey)/2;
        this.path.quadTo(pathMidX,pathMidY,movex,movey);
    }

    /**
     * Met à jour la courbure d'un arc
     * @param movex la coordonnée x du doigt de l'utilisateur
     * @param movey la coordonnée y du doigt de l'utilisateur
     */
    public void updateMidPath(int movex, int movey){
        this.path = new Path();
        //Les coordonées du milieu du noeud de départ
        int depX = nodeDep.getX() + (nodeDep.getWidth() / 2);
        int depY = nodeDep.getY() + (nodeDep.getHeight() / 2);
        this.path.moveTo(depX,depY);
        //Les coordonées du milieu du noeud d'arrivée
        int arrX = nodeArr.getX() + (nodeArr.getWidth() / 2);
        int arrY = nodeArr.getY() + (nodeArr.getHeight() / 2);
        //Si l'arc est une boucle
        if(nodeDep == nodeArr){
            this.path = new Path();
            if((movex < nodeDep.getX() + nodeDep.getWidth() && movex > nodeDep.getX())
                    && (movey < nodeDep.getY() + nodeDep.getHeight() && movey > nodeDep.getY())){
                pathMidX = depX + 150;
                pathMidY = depY + 150;
            }else{
                pathMidX = movex;
                pathMidY = movey;
            }
            this.path.moveTo(depX, depY);
            this.path.quadTo(depX+(pathMidX-depX), depY, pathMidX, pathMidY);
            this.path.moveTo(pathMidX, pathMidY);
            this.path.quadTo(depX, depY+(pathMidY-depY), depX, depY);
        }
        //Si l'arc n'est pas une boucle
        else{
            //Conversion des coordonées de int en double;
            double arrYdouble = arrY;
            double arrXdouble = arrX;
            double depYdouble = depY;
            double depXdouble = depX;
            //Equation de la droite passant par les deux noeuds de la forme y = mx+p
            double droiteEqM = (arrYdouble-depYdouble)/(arrXdouble-depXdouble);
            double droiteEqP = arrY-droiteEqM*arrX;
            //Cas où l'arc est vertical
            if(droiteEqM == Double.POSITIVE_INFINITY){
                droiteEqM = 9999999999.0;
            }else if(droiteEqM == Double.NEGATIVE_INFINITY){
                droiteEqM = -9999999999.0;
            }
            if(droiteEqP == Double.POSITIVE_INFINITY){
                droiteEqP = 9999999999.0;
            }else if(droiteEqP == Double.NEGATIVE_INFINITY){
                droiteEqP = -9999999999.0;
            }
            //Distance du doigt par rapport à la droite passant par les deux noeuds
            double distance = Math.abs(movey-(droiteEqM*movex)-droiteEqP)/Math.sqrt(1.0+(droiteEqM*droiteEqM));
            //Equation de la perpendiculaire à la droite passant par les deux noeuds de la forme y = mx+p
            double perpenEqM = (1.0/droiteEqM)*-1.0;
            if(droiteEqM == 9999999999.0 || droiteEqM == -9999999999.0)
                perpenEqM = 0;
            double perpenEqP = ((depYdouble + arrYdouble)/2)-perpenEqM*((depXdouble + arrXdouble)/2);
            //Permet d'obtenir une valeur indiquant le sens de l'arc
            double sens = movey-(droiteEqM*movex)-droiteEqP;
            //Coordonnées du milieu de l'arc
            int tmpMidX = (depX + arrX)/2, tmpMidY = (depY + arrY)/2;
            //Permet de savoir dans quel sens incrémenter x pour obtenir le milieu de l'arc qui sera à la même distance que le doigt
            boolean xIncrement;
            if((perpenEqM > 0 && sens > 0) || (perpenEqM < 0 && sens < 0))
                xIncrement = true;
            else
                xIncrement = false;
            double tmpDistance = 0;
            //Tant que la distance entre la droite des noeuds et le milieu est inférieure à celle entre le doigt et la droite des noeud, incrémenter (ou décrémenter) x
            while(tmpDistance < distance){
                //Cas ou l'arc est vertical
                if(perpenEqM == 0.0){
                    tmpMidX = movex;
                    break;
                }
                //Cas ou l'arc est horizontal
                if(droiteEqM == 0.0){
                    tmpMidY = movey;
                    break;
                }
                //Autres cas
                else{
                    tmpDistance = Math.abs(tmpMidY-(droiteEqM*tmpMidX)-droiteEqP)/Math.sqrt(1.0+(droiteEqM*droiteEqM));
                    if(xIncrement)
                        tmpMidX++;
                    else
                        tmpMidX--;
                    tmpMidY = (int)(perpenEqM*tmpMidX+perpenEqP);
                }
            }
            pathMidX = tmpMidX;
            pathMidY = tmpMidY;
            this.path.quadTo(pathMidX,pathMidY,arrX,arrY);
        }
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
     * @return pathMidX la coordonnée x du milieu de l'arc
     */
    public int getPathMidX() {
        return pathMidX;
    }

    /**
     * @param pathMidX la coordonnée x du milieu de l'arc
     */
    public void setPathMidX(int pathMidX) {
        this.pathMidX = pathMidX;
    }
    /**
     * @return pathMidY la coordonnée y du milieu de l'arc
     */
    public int getPathMidY() {
        return pathMidY;
    }

    /**
     * @param pathMidY la coordonnée y du milieu de l'arc
     */
    public void setPathMidY(int pathMidY) {
        this.pathMidY = pathMidY;
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
     * @param color la couleur de l'arc en String
     */
    public void setColor(String color){
        this.color = colors.get(color);
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
