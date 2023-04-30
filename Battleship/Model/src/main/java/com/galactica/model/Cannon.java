package com.galactica.model;

// Cannon class, extends the Weapon class
public class Cannon extends Weapon {

    Cannon() {
        this.amountOfUses = 1;
    }

    @Override
    public String toString() {
        return "Cannon";
    }
}
