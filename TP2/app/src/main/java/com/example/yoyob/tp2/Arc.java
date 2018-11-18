package com.example.yoyob.tp2;

import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.io.Serializable;

public class Arc implements Serializable {

    private Node nodeDep, nodeArr;
    private Path path;
    private int id;
    private int color;
    private int pathMidX, pathMidY;
    private String name;
    private float width = 10f;


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
            Log.d("boucle","boucle");
            if(nodeDep == nodeArr){
                Log.d("boucle","oui");
                Log.d("boucleMidX",pathMidX+"");
                Log.d("boucleMidY",pathMidY+"");
                this.path = new Path();
                int x = nodeDep.getX()+ (nodeDep.getWidth() / 2);
                int y = nodeDep.getY()+ (nodeDep.getHeight() / 2);
                if((pathMidX == 0 && pathMidY == 0)
                    ||((pathMidX < nodeDep.getX() + nodeDep.getWidth() && pathMidX > nodeDep.getX())
                        && (pathMidY < nodeDep.getY() + nodeDep.getHeight() && pathMidY > nodeDep.getY()))) {
                    pathMidX = x + 150;
                    pathMidY = y + 150;
                }
                this.path.moveTo(x, y);
                this.path.quadTo(x+(pathMidX-x), y, pathMidX, pathMidY);
                this.path.moveTo(pathMidX, pathMidY);
                this.path.quadTo(x, y+(pathMidY-y), x, y);
                Log.d("boucleMidX",pathMidX+"");
                Log.d("boucleMidY",pathMidY+"");
            }else{
                Log.d("boucle","non");
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
        Log.d("boucle","move");
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
        int depX = nodeDep.getX() + (nodeDep.getWidth() / 2);
        int depY = nodeDep.getY() + (nodeDep.getHeight() / 2);
        this.path.moveTo(depX,depY);
        int arrX = nodeArr.getX() + (nodeArr.getWidth() / 2);
        int arrY = nodeArr.getY() + (nodeArr.getHeight() / 2);
        if(nodeDep == nodeArr){
            this.path = new Path();
            int x = nodeDep.getX()+ (nodeDep.getWidth() / 2);
            int y = nodeDep.getY()+ (nodeDep.getHeight() / 2);
            if((movex < nodeDep.getX() + nodeDep.getWidth() && movex > nodeDep.getX())
                    && (movey < nodeDep.getY() + nodeDep.getHeight() && movey > nodeDep.getY())){
                Log.d("coordXmin",nodeDep.getX() - nodeDep.getWidth()/2+"");
                Log.d("coordXmax",nodeDep.getX() + nodeDep.getWidth()/2+"");
                Log.d("coordYmin",nodeDep.getY() - nodeDep.getHeight()/2+"");
                Log.d("coordYmax",nodeDep.getY() + nodeDep.getHeight()/2+"");
                Log.d("coordY",movex+"");
                Log.d("coordY",movey+"");
                pathMidX = x + 150;
                pathMidY = y + 150;
            }else{
                pathMidX = movex;
                pathMidY = movey;
            }
            this.path.moveTo(x, y);
            this.path.quadTo(x+(pathMidX-x), y, pathMidX, pathMidY);
            this.path.moveTo(pathMidX, pathMidY);
            this.path.quadTo(x, y+(pathMidY-y), x, y);
        }else{
            Log.d("distanceArrY",arrY+"");
            Log.d("distanceArrX",arrX+"");
            Log.d("distanceDepY",depY+"");
            Log.d("distanceDepX",depX+"");
            Log.d("distanceMoveY",movey+"");
            Log.d("distanceMoveX",movex+"");
            double arrYdouble = arrY;
            double arrXdouble = arrX;
            double depYdouble = depY;
            double depXdouble = depX;
            double droiteEqM = (arrYdouble-depYdouble)/(arrXdouble-depXdouble);
            double droiteEqP = arrY-droiteEqM*arrX;
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
            Log.d("distanceM",droiteEqM+"");
            Log.d("distanceP",droiteEqP+"");
            double distance = Math.abs(movey-(droiteEqM*movex)-droiteEqP)/Math.sqrt(1.0+(droiteEqM*droiteEqM));
            Log.d("distanceArc",distance+"");
            double perpenEqM = (1.0/droiteEqM)*-1.0;
            if(droiteEqM == 9999999999.0 || droiteEqM == -9999999999.0)
                perpenEqM = 0;
            double perpenEqP = ((depYdouble + arrYdouble)/2)-perpenEqM*((depXdouble + arrXdouble)/2);
            Log.d("distanceM",perpenEqM+"");
            Log.d("distanceP",perpenEqP+"");
            double sens = movey-(droiteEqM*movex)-droiteEqP;
            int tmpMidX = (depX + arrX)/2, tmpMidY = (depY + arrY)/2;
            boolean xIncrement;
            if((perpenEqM > 0 && sens > 0) || (perpenEqM < 0 && sens < 0))
                xIncrement = true;
            else
                xIncrement = false;
            double tmpDistance = 0;
            while(tmpDistance < distance){
                if(perpenEqM == 0.0){
                    tmpMidX = movex;
                    break;
                }if(droiteEqM == 0.0){
                    tmpMidY = movey;
                    break;
                }
                else{
                    Log.d("distanceTmp",tmpDistance+"");
                    Log.d("distanceDis",distance+"");
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
