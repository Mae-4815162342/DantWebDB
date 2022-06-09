package model;

import java.util.*;

public class StringTree implements Tree{
    private static final boolean RED   = false;
    private static final boolean BLACK = true;
    private StringNode root;
    public List<RowTree> nullList=new ArrayList<>();

    public StringTree(){}




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
                System.out.println(valeur +" "+key);
                return valeur;
            }
        } while (t != null);
        addEntry(key, row, parent, cmp < 0);
        System.out.println(key +" "+key);
        return key;
    }

    @Override
    public List<RowTree> getEqual(String key) {
        if(key==null||key.isBlank()){
            return nullList;
        }
        StringNode t = root;
        if (t == null) {
           return null;
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
                return  t.getListe();

            }
        } while (t != null);

        return null;

    }

    @Override
    public HashMap<String,List<RowTree>> getGroupBy() {
        HashMap<String,List<RowTree>> res= new HashMap<>();
        res.put(null,nullList);
        this.group(res,this.root);
        return res;
    }

    @Override
    public Set<RowTree> gt(String key) {
        if(key==null){
            return null;
        }
        Set<RowTree> res=new HashSet<>() ;
        return gt_sub(key,res,root);
    }

    private Set<RowTree> gt_sub(String key, Set<RowTree> res, StringNode root) {
        if (root == null) {
            return null ;
        }
        res.addAll(root.getListe());
        int cmp = key.compareTo(root.getValeur());
        if (cmp > 0){
            res.addAll(gt_sub(key,res, root.getGauche()));
        }
        res.addAll(gt_sub(key,res, root.getDroite()));
        return res;
    }

    @Override
    public Set<RowTree> lt(String key) {
        if(key==null){
            return null;
        }
        Set<RowTree> res=new HashSet<>() ;
        return lt_sub(key,res,root);
    }

    private Set<RowTree> lt_sub(String key, Set<RowTree> res, StringNode root) {
        if (root == null) {
            return null ;
        }
        res.addAll(root.getListe());
        int cmp = key.compareTo(root.getValeur());

        if (cmp > 0){
            res.addAll(gt_sub(key,res, root.getGauche()));
            res.addAll(gt_sub(key,res, root.getGauche()));
        }
        else{
            res.addAll(gt_sub(key,res, root.getGauche()));
        }


        return res;
    }


    public void group(HashMap<String,List<RowTree>> res,StringNode n){
        if (n == null) {
            return ;
        }
        res.put(n.getValeur(),n.getListe());
        this.group(res,n.getGauche());
        this.group(res,n.getDroite());
        return ;
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
