package com.example.yoyob.tp2;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    private int x;
    private int y;
    private String etiquette;
    private int color;
    private int width;
    private int height;

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

    public static String DEFAULT_ETIQ = "";

    /**
     * Constructeur
     * @param x coordonnées (x,y)
     * @param y coordonnées (x,y)
     * @param width largeur du noeud
     * @param color couleur du noeud
     */
    public Node(int x, int y, int width, String color, String etiquette) {
        this.x = x;
        this.y = y;
        this.color = colors.get(color);
        this.width = width;
        this.etiquette = etiquette;
    }

    /**
     * Constructeur
     * @param x coordonnées (x,y)
     * @param y coordonnées (x,y)
     * @param width largeur du noeud
     * @param height hauteur du noeud
     * @param color couleur du noeud
     */
    public Node(int x, int y, int width, int height, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.width = width;
        this.height = height;
        this.etiquette = "blabla";
    }
    /**
     * @return x la position x du noeud
     */
    public int getX() {
        return x;
    }

    /**
     * @param x la position x du noeud
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return la position y du noeud
     */
    public int getY() {
        return y;
    }

    /**
     * @param y la position y du noeud
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return etiquette l'étiquette du noeud
     */
    public String getEtiquette() {
        return etiquette;
    }

    /**
     * @param etiquette l'étiquette du noeud
     */
    public void setEtiquette(String etiquette) {
        this.etiquette = etiquette;
    }

    /**
     * @return la couleur du noeud
     */
    public int getColor() {
        return color;
    }

    /**
     * @param color la couleur du noeud en Integer
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * @param color la couleur du noeud en String
     */
    public void setColor(String color){
        this.color = colors.get(color);
    }

    /**
     * @return la largeur du noeud
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return l'hauteur du noeud
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param width la largeur du noeud
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height l'hauteur du noeud
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Met à jour les coordonnées du noeud
     * @param x coordonnées (x,y)
     * @param y coordonnées (x,y)
     */
    public void update(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", etiquette='" + etiquette + '\'' +
                ", color=" + color +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
