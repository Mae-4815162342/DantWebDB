package model;

import java.util.Arrays;

public class IndexList {
    private int[] liste;

    public IndexList(){

    }

    public IndexList(int e){
        liste=new int[1];
        liste[0]=e;

    }

    public boolean check(int e){
        for(int i=0;i<liste.length;i++) {
            if (liste[i] == e) {
                return false;
            }
        }
        return true;
    }

    public static IndexList addElement(int e,IndexList l){
        if (l==null){
            return new IndexList(e);
        }
        if(l.check(e)) {
            l.liste = Arrays.copyOf(l.liste, l.liste.length + 1);
            l.liste[l.liste.length - 1] = e;
        }
        return l;
    }
}
