package com.galactica.model;

import java.util.Objects;

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

    public void setX(char newX){
        this.x = newX;
    }
    public void setY(int newY){
        this.y = newY;
    }
    
 // Override equals() method to compare values of x and y fields
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (obj == null || getClass() != obj.getClass()) 
        	return false;
        Coordinate that = (Coordinate) obj;
        return x == that.x && y == that.y;
    }
    
    // Override hashCode() method to generate unique hash code based on values of x and y fields
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
