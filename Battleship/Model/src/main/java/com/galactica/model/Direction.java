package com.galactica.model;

import java.util.Arrays;

import com.github.cliftonlabs.json_simple.JsonObject;

public enum Direction {
    Horizontal('h'), Vertical('v');

    private final char charIdentifier;

    Direction(char charIdentifier) {
        this.charIdentifier = charIdentifier;
    }

    public char getCharIdentifier() {
        return charIdentifier;
    }

    public static Direction get(char identifier) {
        return Arrays.stream(Direction.values())
                .filter(env -> env.charIdentifier == identifier)
                .findFirst()
                .orElse(null);
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("charIdentifier", String.valueOf(charIdentifier));
        return jo;
    }

    public static Direction fromJsonObject(JsonObject jo) {
        char charIdentifier = ((String) jo.get("charIdentifier")).charAt(0);
        return Direction.get(charIdentifier); 
    }
}
