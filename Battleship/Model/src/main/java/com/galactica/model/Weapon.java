package com.galactica.model;

public abstract class Weapon {

    public int areaOfEffect;
    protected int amountOfUses;

    public int getAreaOfEffect() {
        return areaOfEffect;
    }

    public int getAmountOfUses() {
        return amountOfUses;
    }

    public void decrementAmountOfUses() {
        this.amountOfUses = amountOfUses - 1;
    }
}
