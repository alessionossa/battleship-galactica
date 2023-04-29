package com.galactica.cli;

import com.galactica.model.*;

public class ShipCLI {

    public static Ship askShip(CLI cli, Player player) {

        // to use more than one ship of each type could use d1, d2, d3 but won't matter
        // with gui
        while (true) {
            System.out.println(player.getName() + ", select a ship: 'd' for death, 'c' for cruiser, 's' for scout");
            char resp = Character.toLowerCase(cli.scanner.nextLine().charAt(0));
            if (resp == 'd') {
                if (player.areAllShipsSunk(player.getDeathstars())) {
                    System.out.println("You have no more deathstars");
                    continue;
                }

                for (Ship ship : player.getDeathstars()) {
                    if (!ship.isPlaced()) {
                        return ship;
                    }
                }
            } else if (resp == 'c') {
                if (player.areAllShipsSunk(player.getCruisers())) {
                    System.out.println("You have no more cruisers");
                    continue;
                }

                for (Ship ship : player.getCruisers()) {
                    if (!ship.isPlaced()) {
                        return ship;
                    }
                }
            } else if (resp == 's') {
                if (player.areAllShipsSunk(player.getScouts())) {
                    System.out.println("You have no more scouts");
                    continue;
                }

                for (Ship ship : player.getScouts()) {
                    if (!ship.isPlaced()) {
                        return ship;
                    }
                }
            } else
                System.out.println("Please only type 'd', 'c' or 's'");
        }
    }
}
