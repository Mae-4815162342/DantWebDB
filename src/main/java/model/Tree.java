package model;

import java.util.*;

public class Tree {
    private static final boolean RED   = false;
    private static final boolean BLACK = true;
    private StringNode root;
    public List<RowTree> nullList=new ArrayList<>();

    public Tree(){}




    public String put(String key, RowTree row) {
        if(key==null||key.isBlank()){
            nullList.add(row);
            return null;
        }
        StringNode t = root;
        if (t == null) {
            addEntryToEmptyMap(key, row);
            return key;
        }
        int cmp;
        StringNode parent;
        // split comparator and comparable paths
        List<RowTree> value;
        do {
            parent = t;
            String valeur=t.getValeur();
            cmp = key.compareTo(valeur);
            if (cmp < 0)
                t = t.getGauche();
            else if (cmp > 0)
                t = t.getDroite();
            else {
                value = t.getListe();
                value.add(row);
                return valeur;
            }
        } while (t != null);
        addEntry(key, row, parent, cmp < 0);
        return key;
    }

    private void addEntryToEmptyMap(String key, RowTree value) {
        root = new StringNode(key,value,null);

    }

    private void addEntry(String key, RowTree value, StringNode p, boolean addToLeft) {
        StringNode e = new StringNode(key, value, p);
        if (addToLeft) {StringNode g=p.getGauche(); g = e;}
        else {StringNode d=p.getDroite();d=e;}
        fixAfterInsertion(e);

    }

    private void fixAfterInsertion(StringNode x) {
        x.color = RED;

        while (x != null && x != root && x.getParent().color == RED) {
            if (x.getParent() == x.grandparent().getGauche()) {
                StringNode y = x.grandparent().getDroite();
                if (y.color == RED) {
                    x.getParent().color=BLACK;
                    y.color=BLACK;
                    x.grandparent().color=RED;
                    x = x.grandparent();
                } else {
                    if (x == x.getParent().getDroite()) {
                        x = x.getParent();
                        x.rotateLeft(root);
                    }
                    x.getParent().color=BLACK;
                    x.grandparent().color=RED;
                    x.grandparent().rotateRight(root);
                }
            } else {
                StringNode y = x.grandparent().getGauche();
                if (y.color == RED) {
                    x.getParent().color=BLACK;
                    y.color=BLACK;
                    x.grandparent().color=RED;
                    x = x.grandparent();
                } else {
                    if (x == x.getParent().getGauche()) {
                        x =x.getParent();
                        x.rotateRight(root);
                    }
                    x.getParent().color=BLACK;
                    x.grandparent().color=RED;
                    x.grandparent().rotateLeft(root);
                }
            }
        }
        root.color = BLACK;
    }


}
