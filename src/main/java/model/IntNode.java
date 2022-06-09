package model;

import java.util.ArrayList;
import java.util.List;

public class IntNode implements Node<IntNode>{

    private IntNode parent;
    private IntNode gauche;
    private IntNode droite;
    private int valeur;
    private List<RowTree> liste;
    boolean color = true;//BLACK


    public IntNode(String valeur,RowTree row,IntNode parent){
        this.valeur=Integer.parseInt(valeur);
        this.liste=new ArrayList<>();
        liste.add(row);
        this.parent=parent;

    }

    public IntNode(int valeur,RowTree row,IntNode parent){
        this.valeur=valeur;
        this.liste=new ArrayList<>();
        liste.add(row);
        this.parent=parent;

    }

    public IntNode getParent() {
        return parent;
    }

    public IntNode getGauche() {
        return gauche;
    }

    public IntNode getDroite() {
        return droite;
    }

    public Integer getValeur() {
        return valeur;
    }

    public List<RowTree> getListe() {
        return liste;
    }

    public boolean isColor() {
        return color;
    }

    public IntNode grandparent(){
        if(this.parent==null){return null;}
        return this.parent.getParent();
    }

    public void rotateRight(IntNode root) {
        if (this != null) {
            IntNode l = this.gauche;
            this.gauche = l.droite;
            if (l.droite != null) l.droite.parent = this;
            l.parent = this.parent;
            if (this.parent == null)
                root = l;
            else if (this.parent.droite == this)
                this.parent.droite = l;
            else this.parent.gauche = l;
            l.droite = this;
            this.parent = l;
        }
    }

    public void rotateLeft(IntNode root) {
        if (this != null) {
            IntNode r = this.droite;
            this.droite = r.gauche;
            if (r.gauche != null)
                r.gauche.parent = this;
            r.parent = this.parent;
            if (this.parent == null)
                root = r;
            else if (this.parent.gauche == this)
                this.parent.gauche = r;
            else
                this.parent.droite = r;
            r.gauche = this;
            this.parent = r;
        }
    }

}
