package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

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

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("areaOfEffect", areaOfEffect);
        jo.put("amountOfUses", amountOfUses);
        return jo;
    }
}
