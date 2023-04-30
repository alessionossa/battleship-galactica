package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.List;
import com.github.cliftonlabs.json_simple.JsonArray;

// Human class representing a human player, extends the Player class
public class Human extends Player {
    // Constructor for creating a new Human object with name, ownGrid, and
    // opponentGrid
    public Human(String name, Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        this.name = name;
    }

    // Constructor for creating a new Human object with additional parameters:
    // ships, laser, and grenade
    public Human(String name, Grid ownGrid, Grid opponentGrid, List<Ship> ships, Laser laser, Grenade grenade) {
        super(ownGrid, opponentGrid);
        this.name = name;
        this.ships = ships;
        this.cannon = new Cannon();
        this.laser = laser;
        this.grenade = grenade;
    }

    // Method for shooting a weapon at a given coordinate
    public void shoot(Coordinate coordinate, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed) {
        if (weaponToShoot instanceof Cannon) {
            shootCannon(coordinate, gravityMode, gravityUsed);
        } else if (weaponToShoot instanceof Grenade) {
            shootGrenade(coordinate, (Grenade) weaponToShoot);
        }
    }

    // Method for creating a Human object from a JsonObject
    public static Human fromJsonObject(JsonObject jo, Grid ownGrid, Grid opponentGrid) {
        String name = (String) jo.get("name");
        List<Ship> ships = Player.fromJsonArraytoShipList((JsonArray) jo.get("ships"));
        Laser laser = Laser.fromJsonObject((JsonObject) jo.get("laser"));
        Grenade grenade = Grenade.fromJsonObject((JsonObject) jo.get("grenade"));

        return new Human(name, ownGrid, opponentGrid, ships, laser, grenade);
    }
}
