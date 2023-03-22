package com.battleshipgalactica.model;

public class Coordinate { //Send new coordinates to player
    private char x;
    private int y; 

    public Coordinate(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public char getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
