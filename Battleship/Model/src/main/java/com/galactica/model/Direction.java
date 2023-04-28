package com.galactica.model;

import java.util.Arrays;

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

    public Object toJasonObject() {
        return null;
    }
}
