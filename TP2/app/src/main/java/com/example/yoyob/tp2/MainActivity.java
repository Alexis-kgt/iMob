package com.example.yoyob.tp2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    DrawableGraph myDrawableGraph;
    ImageView backgroundImageView;
    RadioGroup rGroup;
    Graph graph;

    int largeur, hauteur;

    Node currentNode;
    Arc currentArc;

    int downx = 0,downy = 0, upx = 0, upy = 0;

    final static long LONG_TOUCH_DURATION = 300;
    long touchStartTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ImageView comportant le graphe
        backgroundImageView = findViewById(R.id.backgroundImageView);

        //Permet de basculer entre les trois modes "Noeud", "Arc", "Modification"
        rGroup = findViewById(R.id.radioGroup);
        rGroup.check(R.id.nodeRadioButton);

        //Ajout d'un listene    r sur l'ImageView
        ViewTreeObserver vto = backgroundImageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                backgroundImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                //Récupération de la taille de l'écran
                hauteur = backgroundImageView.getMeasuredHeight();
                largeur = backgroundImageView.getMeasuredWidth();

                //Instanciation des objets nécessaire à la création du graphe
                graph = new Graph(largeur,hauteur);
                myDrawableGraph = new DrawableGraph(graph);
                backgroundImageView.setImageDrawable(myDrawableGraph);
                addListener();
                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addListener(){
        backgroundImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    //Lorsqe l'utilisateur pose son doigt sur l'écran
                    case MotionEvent.ACTION_DOWN:
                        touchStartTime = (Long) System.currentTimeMillis();
                        //Coordonnées
                        downx = (int) event.getX();
                        downy = (int) event.getY();
                        //Si l'utilisateur à posé son doigt sur un noeud
                        if(graph.nodeSelected(downx,downy) != null){
                            //On récupère le noeud en question et on créer un arc temporaire
                            currentNode = graph.nodeSelected(downx,downy);
                            currentArc = new Arc("",new Path());
                            currentArc.setNodeDep(currentNode);
                            graph.addArc(currentArc);
                        }
                        // Sinon le noeud courant devient null
                        else{
                            currentNode = null;
                        }
                        break;
                    //Lorsque l'utilisateur bouge son doigt sur l'écran
                    case MotionEvent.ACTION_MOVE:
                        //Coordonnées du doigt en mouvement
                        int movex = (int)event.getX();
                        int movey = (int)event.getY();
                        //S'il a sélectionné un noeud
                        if(currentNode != null){
                            //Si le mode est "Noeud"
                            if( rGroup.getCheckedRadioButtonId() == R.id.nodeRadioButton){
                                //On modifie la postion du noeud en fonction de la position du doigt
                                currentNode.update(movex,movey);
                                //La position des arcs en relation avec le noeud sont modifiées aussi
                                Iterator<Arc> it = graph.getArcForNode(currentNode).iterator();
                                while(it.hasNext()){
                                    it.next().updatePath();
                                }
                            }
                            //Si le mode est "Arc"
                            if( rGroup.getCheckedRadioButtonId() == R.id.arcRadioButton){
                                //Si l'arc temporaire a été créé, on modifie sa postion d'arrivée
                                if(currentArc != null)
                                    currentArc.updatePath(movex,movey);
                            }
                        }
                        //Mise à jour de l'écran
                        backgroundImageView.invalidate();
                        break;
                    //Si le doigt de l'utilisateur est retiré
                    case MotionEvent.ACTION_UP:
                        //Coordonnées de l'emplacement du doigt
                        upx = (int) event.getX();
                        upy = (int) event.getY();
                        //Si le mode est "noeud", qu'un noeud est séléctionné, qu'il n'a pas été déplacé et que l'utilisateur reste appuyé plus d'une seconde dessus, on ouvre le menu de modification du noeud
                        if(currentNode != null && (Math.abs(downx - upx) < currentNode.getWidth())
                                && (Math.abs(downy - upy) < currentNode.getHeight())
                                && System.currentTimeMillis() - touchStartTime > LONG_TOUCH_DURATION
                                && rGroup.getCheckedRadioButtonId() == R.id.modificationRadioButton){
                            createDialogNodeModif(currentNode);
                        }
                        else if(currentNode == null && System.currentTimeMillis() - touchStartTime > LONG_TOUCH_DURATION
                                    && rGroup.getCheckedRadioButtonId() == R.id.nodeRadioButton){
                                createDialogNodeCreation(upx,upy);

                        }else{
                            //Si un arc a été créé, mais qu'il n'arrive pas sur un autre noeud, on le supprime
                            if(currentArc != null && graph.nodeSelected(upx,upy) == null && rGroup.getCheckedRadioButtonId() == R.id.arcRadioButton){
                                graph.removeArc(currentArc);
                            //Si l'arc arrive sur un autre noeud, on l'ajoute au graphe
                            }else if(rGroup.getCheckedRadioButtonId() == R.id.arcRadioButton){
                                Node nodeDepart = graph.nodeSelected(downx,downy);
                                Node nodeArrivee = graph.nodeSelected(upx,upy);
                                if(nodeDepart != null && nodeArrivee != null && currentArc != null){
                                    createDialogArcName(currentArc);
                                    currentArc.setNodeDep(nodeDepart);
                                    currentArc.setNodeArr(nodeArrivee);
                                    currentArc.updatePath();
                                    currentArc = null;
                                }
                            }
                        }
                        backgroundImageView.invalidate();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Créer une boite de dialogue pour renseigner l'étiquette de l'arc
     * @param arc l'arc en question
     */
    public void createDialogArcName(final Arc arc){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(false)
                //Valider l'étiquette
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                arc.setName(""+userInput.getText());
                            }
                        })
                //L'arc n'a pas d'étiquette
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Créer une boite de dialogue pour la modification d'un noeud
     * @param node le noeud en question
     */
    public void createDialogNodeModif(final Node node){
        LayoutInflater li = LayoutInflater.from(context);
        //Les différents champs "étiquette", "taille" et "couleur"
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View v = li.inflate(R.layout.node_modification,null);
        final TextInputEditText etiquette = v.findViewById(R.id.etiquetteEditText);
        etiquette.setText(node.getEtiquette());
        final TextInputEditText taille = v.findViewById(R.id.tailleEditText);
        taille.setText(""+node.getWidth());
        final Spinner color = v.findViewById(R.id.colorSpinner);
        Button delButton = v.findViewById(R.id.delNodeButton);
        alertDialogBuilder.setView(v);
        alertDialogBuilder
                .setCancelable(false)
                //Validation des modifications
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(""+etiquette.getText() != "")
                                    node.setEtiquette(""+etiquette.getText());
                                if(""+taille.getText() != "" && Integer.parseInt(""+taille.getText()) != node.getWidth())
                                    node.setWidth(Integer.parseInt(""+taille.getText()));
                                node.setColor(""+color.getSelectedItem().toString());
                                Iterator<Arc> it = graph.getArcForNode(node).iterator();
                                while(it.hasNext()){
                                    it.next().updatePath();
                                }
                                backgroundImageView.invalidate();
                            }
                        })
                //Annulation des modifications
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.removeNode(node);
                alertDialog.dismiss();
                backgroundImageView.invalidate();
            }
        });
        alertDialog.show();
    }

    public void createDialogNodeCreation(final int upx, final int upy){
        LayoutInflater li = LayoutInflater.from(context);
        //Les différents champs "étiquette", "taille" et "couleur"
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View v = li.inflate(R.layout.node_creation,null);
        final TextInputEditText etiquette = v.findViewById(R.id.etiquetteEditText);
        final TextInputEditText taille = v.findViewById(R.id.tailleEditText);
        final Spinner color = v.findViewById(R.id.colorSpinner);
        Button delButton = v.findViewById(R.id.delNodeButton);
        alertDialogBuilder.setView(v);
        alertDialogBuilder
                .setCancelable(false)
                //Validation des modifications
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String etiq = "";
                                if(""+etiquette.getText() != "")
                                    etiq = ""+etiquette.getText();
                                int width = 0;
                                if(""+taille.getText() != "")
                                    width = Integer.parseInt(""+taille.getText());
                                String col = "";
                                col = ""+color.getSelectedItem().toString();
                                Node node = new Node(upx,upy,width,col,etiq);
                                graph.AddNode(node);
                                backgroundImageView.invalidate();
                            }
                        })
                //Annulation des modifications
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}