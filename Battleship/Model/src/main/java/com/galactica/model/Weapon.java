// Declare the package and import statements
package com.galactica.model;
import com.github.cliftonlabs.json_simple.JsonObject;

// Declare an abstract class called Weapon
public abstract class Weapon {

    // Declare an instance variable called amountOfUses to store the number of uses remaining for the weapon
    protected int amountOfUses;

    // Declare a getter method for the amountOfUses variable
    public int getAmountOfUses() {
        return amountOfUses;
    }

    // Declare a method to decrement the amountOfUses variable by 1 each time the weapon is used
    public void decrementAmountOfUses() {
        this.amountOfUses = amountOfUses - 1;
    }

    // Declare a method to convert the Weapon object to a JsonObject for serialization
    public JsonObject toJsonObject() {
        // Create a new JsonObject to store the state of the Weapon object
        JsonObject jo = new JsonObject();
        // Add a property called "amountOfUses" to the JsonObject, with the current value of the amountOfUses variable
        jo.put("amountOfUses", amountOfUses);
        // Return the JsonObject
        return jo;
    }
}
