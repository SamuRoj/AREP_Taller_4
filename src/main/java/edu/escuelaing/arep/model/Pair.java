package edu.escuelaing.arep.model;

public class Pair<M, P> {
    private final M first;
    private final P second;

    public Pair(M first, P second){
        this.first = first;
        this.second = second;
    }

    public M getFirst() {
        return first;
    }

    public P getSecond() {
        return second;
    }
}
