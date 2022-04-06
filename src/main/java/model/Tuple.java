package model;

public class Tuple {
    public Object first;
    public Object second;

    public Tuple(Object f, Object s) {
        first = f;
        second = s;
    }

    public String toString() {
        return "{" + first + "," + second + "}";
    }
}
