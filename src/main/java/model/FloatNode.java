package model;

import java.util.ArrayList;
import java.util.List;

public class FloatNode implements Node<FloatNode> {

    private FloatNode parent;
    private FloatNode gauche;
    private FloatNode droite;
    private float valeur;
    private List<RowTree> liste;
    boolean color = true;//BLACK


    public FloatNode(String valeur,RowTree row,FloatNode parent){
        this.valeur=(float)Float.parseFloat(valeur);
        this.liste=new ArrayList<>();
        liste.add(row);
        this.parent=parent;

    }

    public FloatNode getParent() {
        return parent;
    }

    public FloatNode getGauche() {
        return gauche;
    }

    public FloatNode getDroite() {
        return droite;
    }

    public Float getValeur() {
        return valeur;
    }

    public List<RowTree> getListe() {
        return liste;
    }

    public boolean isColor() {
        return color;
    }

    public FloatNode grandparent(){
        if(this.parent==null){return null;}
        return this.parent.getParent();
    }

    public void rotateRight(FloatNode root) {
        if (this != null) {
            FloatNode l = this.gauche;
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

    public void rotateLeft(FloatNode root) {
        if (this != null) {
            FloatNode r = this.droite;
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
