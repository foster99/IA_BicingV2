package model;

public class Pair {
    public final int first;
    public int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public Pair clone() {
        return new Pair(first, second);
    }
}
