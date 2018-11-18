package com.example.yoyob.tp2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Iterator;

public class DrawableGraph extends Drawable {

    private Canvas canvas = new Canvas();
    public Graph graph;
    private Paint nodePaint = new Paint(), nodeEtiquettePaint = new Paint(), arcPaint = new Paint(), arcNamePaint = new Paint();

    /**
     * Constructeur
     * @param graph le graph à dessiner
     */
    public DrawableGraph(Graph graph) {
        this.graph = graph;
        this.draw(canvas);
    }

    /**
     * Méthode de la classe Drawable
     * @param canvas le canvas sur lequel le graphe est dessiné
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        this.canvas = canvas;
        drawNodes();
        drawArcs();
    }

    /**
     * Dessine les noeuds un par un
     */
    private void drawNodes() {
        for (Node node : graph.getNodes()) {
            drawNode(node);
        }
    }

    /**
     * Dessine les arcs un par un
     */
    private void drawArcs() {
        for (Arc arc : graph.getArcs()) {
            drawArc(arc);
        }
    }

    /**
     * Dessine un noeud en fonction d'un objet Node
     * @param node le noeud à dessiner
     */
    public void drawNode(Node node) {
        int nodeWidth = node.getWidth(), nodeHeight = node.getHeight();
        nodePaint.setColor(node.getColor());
        nodeEtiquettePaint.setColor(Color.BLACK);
        nodeEtiquettePaint.setTextSize(30);
        //Le noeud à dessiner
        if((int)nodeEtiquettePaint.measureText(node.getEtiquette()+"") > node.getWidth())
            node.setWidth((int)nodeEtiquettePaint.measureText(node.getEtiquette()+""));
        RectF r = new RectF(node.getX(),node.getY(),node.getX()+nodeWidth,node.getY()+nodeHeight);
        canvas.drawRoundRect(r,15,15, nodePaint);
        //L'etiquette à dessiner
        canvas.drawText(node.getEtiquette(), node.getX(), node.getY()+15+node.getHeight()/2, nodeEtiquettePaint);
        Iterator<Arc> it = graph.getArcForNode(node).iterator();
        while(it.hasNext()){
            it.next().updatePath();
        }
    }

    /**
     * Dessine un noeud en fonction d'un objet Node
     * @param arc l'arc à dessiner
     */
    public void drawArc(Arc arc){
        arcPaint.setColor(arc.getColor());
        arcPaint.setDither(true);
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeJoin(Paint.Join.ROUND);
        arcPaint.setStrokeWidth(arc.getWidth());
        Log.d("updatePath",arc.getPathMidX()+" / "+arc.getPathMidY());
        canvas.drawPath(arc.getPath(),arcPaint);
        arcNamePaint.setColor(arc.getColor());
        arcNamePaint.setTextAlign(Paint.Align.CENTER);
        arcNamePaint.setTextSize(arc.getNodeDep().getHeight() - arc.getNodeDep().getHeight()/3);
        float[] midPoint = {0f, 0f};
        float[] tangent = {0f, 0f};
        PathMeasure pm = new PathMeasure(arc.getPath(), false);
        pm.getPosTan(pm.getLength() * 0.5f, midPoint, tangent);
        Log.d("xxxxxmid0",""+midPoint[0]);
        Log.d("xxxxxmid1",""+midPoint[1]);
        Log.d("xxxxxtan0",""+tangent[0]);
        Log.d("xxxxxtan0",""+tangent[1]);
        if(arc.getNodeDep() == arc.getNodeArr()){
            canvas.drawText(arc.getName(), arc.getPathMidX(), arc.getPathMidY(), arcNamePaint);
        }else{
            canvas.drawText(arc.getName(), midPoint[0], midPoint[1], arcNamePaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
