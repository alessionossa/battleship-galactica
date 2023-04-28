package com.galactica.model;

public class Human extends Player {
    public Human(Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        // System.out.println("Enter player name: ");
        // this.name = Player.sc.nextLine();
        this.name = "Joe"; // TODO make compatible with cucumber tests
    }

    public void shoot(Coordinate coordinate, Weapon weaponToShoot, boolean gravityMode, boolean gravityUsed) {
        int damageArea = weaponToShoot.getAreaOfEffect();

        if (damageArea == 1) {
            shootCannon(coordinate, gravityMode, gravityUsed);
        } else if (damageArea == 2) {shootGrenade(coordinate, (Grenade) weaponToShoot);}

    }

}
