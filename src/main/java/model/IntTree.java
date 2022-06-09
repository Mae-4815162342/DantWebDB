package model;

import java.util.ArrayList;
import java.util.List;



public class IntTree {
        private static final boolean RED   = false;
        private static final boolean BLACK = true;
        private IntNode root;
        public List<RowTree> nullList=new ArrayList<>();

        public IntTree(){}




        public Integer put(String keyValue, RowTree row) {
            if(keyValue==null||keyValue.isBlank()){
                nullList.add(row);
                return null;
            }
            int key=Integer.parseInt(keyValue);
            IntNode t = root;
            if (t == null) {
                addEntryToEmptyMap(key, row);
                return key;
            }
            int cmp;
            IntNode parent;
            // split comparator and comparable paths
            List<RowTree> value;
            int valeur;
            do {
                parent = t;
                valeur=(int)t.getValeur();
                //cmp = key.compareTo(valeur);
                if (key < valeur)
                    t = t.getGauche();
                else if (valeur > key)
                    t = t.getDroite();
                else {
                    value = t.getListe();
                    value.add(row);
                    return valeur;
                }
            } while (t != null);
            addEntry(key, row, parent, key < valeur);
            return key;
        }

        private void addEntryToEmptyMap(int key, RowTree value) {
            root = new IntNode(key,value,null);

        }

        private void addEntry(int key, RowTree value, IntNode p, boolean addToLeft) {
            IntNode e = new IntNode(key, value, p);
            if (addToLeft) {IntNode g=p.getGauche(); g = e;}
            else {IntNode d=p.getDroite();d=e;}
            fixAfterInsertion(e);

        }

        private void fixAfterInsertion(IntNode x) {
            x.color = RED;

            while (x != null && x != root && x.getParent().color == RED) {
                if (x.getParent() == x.grandparent().getGauche()) {
                    IntNode y = x.grandparent().getDroite();
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
                    IntNode y = x.grandparent().getGauche();
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


