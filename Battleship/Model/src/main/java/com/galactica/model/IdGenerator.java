package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;

public class IdGenerator {

    private static int generator = 1;

    public static int get() {
        return generator++;
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.put("generator", generator);
        return jo;
    }
}
