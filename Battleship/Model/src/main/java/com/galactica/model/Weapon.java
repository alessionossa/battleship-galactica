package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

public abstract class Weapon {

    protected int amountOfUses;

    public int getAmountOfUses() {
        return amountOfUses;
    }

    public void decrementAmountOfUses() {
        this.amountOfUses = amountOfUses - 1;
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("amountOfUses", amountOfUses);
        return jo;
    }
}
