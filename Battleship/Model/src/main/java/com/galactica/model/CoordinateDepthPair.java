package com.galactica.model;

public class CoordinateDepthPair<L, R> {
    private L l;
    private R r;

    public CoordinateDepthPair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public L getL() {
        return l;
    }

    public R getR() {
        return r;
    }

    public void setL(L l) {
        this.l = l;
    }

    public void setR(R r) {
        this.r = r;
    }

    @Override
    public boolean equals(Object o) {
        // TODO check if downcast possible
        CoordinateDepthPair<Coordinate, Integer> pair = (CoordinateDepthPair<Coordinate, Integer>) o;
        if (pair.getL().equals(this.getL()) && pair.getR().equals(this.getR())) {
            return true;
        }

        return false;

    }

}