package com.galactica.model;

import java.util.Arrays;
import com.github.cliftonlabs.json_simple.JsonObject;

// Enum representing the direction of a ship or object on the grid
public enum Direction {
    Horizontal('h'), Vertical('v');

    // Character identifier for the direction
    private final char charIdentifier;

    // Constructor for setting the character identifier of the direction
    Direction(char charIdentifier) {
        this.charIdentifier = charIdentifier;
    }

    // Getter for the character identifier
    public char getCharIdentifier() {
        return charIdentifier;
    }

    // Method for getting the Direction enum from a character identifier
    public static Direction get(char identifier) {
        return Arrays.stream(Direction.values())
                .filter(env -> env.charIdentifier == identifier)
                .findFirst()
                .orElse(null);
    }

    // Method for converting the direction to a JsonObject
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("charIdentifier", String.valueOf(charIdentifier));
        return jo;
    }

    // Method for creating a Direction enum from a JsonObject
    public static Direction fromJsonObject(JsonObject jo) {
        char charIdentifier = ((String) jo.get("charIdentifier")).charAt(0);
        return Direction.get(charIdentifier);
    }
}
