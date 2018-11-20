package com.example.yoyob.tp2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Region;
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
        arcNamePaint.setColor(arc.getColor());
        arcNamePaint.setTextAlign(Paint.Align.CENTER);
        arcNamePaint.setTextSize(arc.getTextSize());
        float[] midPoint = {0f, 0f};
        float[] tangent = {0f, 0f};
        PathMeasure pm = new PathMeasure(arc.getPath(), false);
        pm.getPosTan(pm.getLength() * 0.5f, midPoint, tangent);
        if(arc.getNodeDep() == arc.getNodeArr()){
            canvas.drawText(arc.getName(), arc.getPathMidX(), arc.getPathMidY(), arcNamePaint);
        }else{
            canvas.drawText(arc.getName(), midPoint[0], midPoint[1], arcNamePaint);
        }
        Path path = new Path();
        RectF rectFDep = new RectF(arc.getNodeDep().getX(),arc.getNodeDep().getY(),arc.getNodeDep().getX()+arc.getNodeDep().getWidth(),arc.getNodeDep().getY()+arc.getNodeDep().getHeight());
        path.addRoundRect(rectFDep, 0, 0, Path.Direction.CW);
        path.computeBounds(rectFDep, true);
        Region regionDep = new Region();
        regionDep.setPath(path, new Region(arc.getNodeDep().getX(),arc.getNodeDep().getY(),arc.getNodeDep().getX()+arc.getNodeDep().getWidth(),arc.getNodeDep().getY()+arc.getNodeDep().getHeight()));
        pm = new PathMeasure(arc.getPath(), false);
        float[] pointDep = {0f, 0f};
        float[] pointArr = {0f, 0f};
        pm.getPosTan(pm.getLength() * 0.5f, pointDep, null);
        boolean d = regionDep.contains((int) pointDep[0], (int) pointDep[1]);
        if(arc.getNodeArr() != null){
            while(!d){
                pointDep[0] = (pointDep[0]+arc.getNodeDep().getX()+(arc.getNodeDep().getWidth() / 2))/2;
                pointDep[1] = (pointDep[1]+arc.getNodeDep().getY()+(arc.getNodeDep().getHeight() / 2))/2;
                d = regionDep.contains((int) pointDep[0], (int) pointDep[1]);
            }
            RectF rectFArr = new RectF(arc.getNodeArr().getX(),arc.getNodeArr().getY(),arc.getNodeArr().getX()+arc.getNodeArr().getWidth(),arc.getNodeArr().getY()+arc.getNodeArr().getHeight());
            path.addRoundRect(rectFArr, 0, 0, Path.Direction.CW);
            path.computeBounds(rectFArr, true);
            Region regionArr = new Region();
            regionArr.setPath(path, new Region(arc.getNodeArr().getX(),arc.getNodeArr().getY(),arc.getNodeArr().getX()+arc.getNodeArr().getWidth(),arc.getNodeArr().getY()+arc.getNodeArr().getHeight()));
            pm.getPosTan(pm.getLength() * 0.5f, pointArr, null);
            d = regionArr.contains((int) pointArr[0], (int) pointArr[1]);
            while(!d){
                pointArr[0] = (pointArr[0]+arc.getNodeArr().getX()+(arc.getNodeArr().getWidth() / 2))/2;
                pointArr[1] = (pointArr[1]+arc.getNodeArr().getY()+(arc.getNodeArr().getHeight() / 2))/2;
                d = regionArr.contains((int) pointArr[0], (int) pointArr[1]);
            }
        }
        if(arc.getNodeArr() != null && (arc.getNodeDep() != arc.getNodeArr())){
            Path edgePath = new Path();
            edgePath.moveTo(pointDep[0],pointDep[1]);
            edgePath.quadTo(arc.getPathMidX(),arc.getPathMidY(),pointArr[0],pointArr[1]);
            canvas.drawPath(edgePath,arcPaint);
        }else if(arc.getNodeDep() == arc.getNodeArr()){
            Path edgePath = new Path();
            edgePath.moveTo(pointDep[0],pointDep[1]);
            edgePath.quadTo(pointDep[0]+(arc.getPathMidY()-pointDep[0]), pointDep[1], arc.getPathMidX(), arc.getPathMidY());
            edgePath.moveTo(arc.getPathMidX(), arc.getPathMidY());
            edgePath.quadTo(pointDep[0], pointDep[1]+(arc.getPathMidY()-pointDep[1]), pointDep[0], pointDep[1]);
            canvas.drawPath(edgePath,arcPaint);
        }
        else{
            canvas.drawPath(arc.getPath(),arcPaint);
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
