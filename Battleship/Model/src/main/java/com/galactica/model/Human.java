package com.galactica.model;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.JsonArray;


public class Human extends Player {
    public Human(Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        // System.out.println("Enter player name: ");
        // this.name = Player.sc.nextLine();
        this.name = "Joe"; // TODO make compatible with cucumber tests
    }

    public Human(String name, Grid ownGrid, Grid opponentGrid, Ship[] ships, Laser laser, Grenade grenade) {
        super(ownGrid, opponentGrid);
        this.name = name;
        this.ships = ships;
        this.cannon = new Cannon();
        this.laser = laser;
        this.grenade = grenade;

    }
    
    public void shoot(Coordinate coordinate, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed) {
        if (weaponToShoot instanceof Cannon) {
            shootCannon(coordinate, gravityMode, gravityUsed);
        } else if (weaponToShoot instanceof Grenade) {shootGrenade(coordinate, (Grenade) weaponToShoot);}

    }

    public static Human fromJsonObject(JsonObject jo, Grid ownGrid, Grid opponentGrid) {
        String name = (String) jo.get("name");
        // Grid ownGrid = Grid.fromJsonObject((JsonObject)jo.get("ownGrid"));
        // Grid opponentGrid = Grid.fromJsonObject((JsonObject)jo.get("opponentGrid"));
        Ship[] ships = Player.fromJsonArraytoShipList((JsonArray)jo.get("ships"));
        Laser laser = Laser.fromJsonObject((JsonObject)jo.get("laser"));
        Grenade grenade = Grenade.fromJsonObject((JsonObject)jo.get("grenade"));

        return new Human(name, ownGrid, opponentGrid, ships, laser, grenade);

    }

}
