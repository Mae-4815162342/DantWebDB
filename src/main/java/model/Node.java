package model;

import java.util.ArrayList;
import java.util.List;

public interface Node<T> {





    public T getParent() ;

    public T getGauche();

    public T getDroite();

    public Object getValeur();

    public List<RowTree> getListe();

    public boolean isColor();

    public T grandparent();

    public void rotateRight(T root) ;

    public void rotateLeft(T root) ;
}
