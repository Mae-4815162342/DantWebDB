package model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StringNode implements NodeTree{
    private StringNode parent;
    private StringNode gauche;
    private StringNode droite;
    private String valeur;
    private List<RowTree> liste;
    boolean color = true;//BLACK


    public StringNode(String valeur,RowTree row,StringNode parent){
        this.valeur=valeur;
        this.liste=new ArrayList<>();
        liste.add(row);
        this.parent=parent;

    }

    public StringNode getParent() {
        return parent;
    }

    public StringNode getGauche() {
        return gauche;
    }

    public StringNode getDroite() {
        return droite;
    }

    public String getValeur() {
        return valeur;
    }

    public List<RowTree> getListe() {
        return liste;
    }

    public boolean isColor() {
        return color;
    }

    public StringNode grandparent(){
        if(this.parent==null){return null;}
        return this.parent.getParent();
    }

    public void rotateRight(StringNode root) {
        if (this != null) {
            StringNode l = this.gauche;
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

    public void rotateLeft(StringNode root) {
        if (this != null) {
            StringNode r = this.droite;
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
