package com.galactica.model;

public class Human extends Player {

    public Human(Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        //System.out.println("Enter player name: ");
        //this.name = Player.sc.nextLine();
        this.name = "Joe"; // TODO make compatible with cucumber tests
    }

    public void shoot(Coordinate coordinate) {
        opponentGrid.setTile(coordinate, true);

        Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);
        Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);

        if (shipAtCoordinate != null) {
            boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
            if (isShipSunk) {
                shipAtCoordinate.setSunk(true);
                System.out.println("You sunk a ship! 💥🚢");
            } else
                System.out.println("You hit something!");
        } else if (asteroidAtCoordinate != null) {
            System.out.println("You hit something!");
        }
        else
            System.out.println("You missed :(");

    }
}
