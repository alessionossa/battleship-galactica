package com.galactica.cli;

import com.galactica.model.Cannon;
import com.galactica.model.Coordinate;
import com.galactica.model.Laser;
import com.galactica.model.Weapon;
import com.galactica.model.AI;

public class AICLI {
    public static void printShootingTurn(AI ai, Coordinate coordinate, Weapon weapon, char c) {
        String name = ai.getName();
        System.out.println("\n-------------\nAI's turn to shoot:");
        if (weapon instanceof Laser)
            System.out.println(name + ", is shooting a laser...");
        if (c == 'r') {
            String message = "The " + name + " has shot in row: " + coordinate.getY();
            System.out.println(message);
        } else if (c == 'c') {
            String message = "The " + name + " has shot in column: " + coordinate.getX();
            System.out.println(message);
        }

        else if (weapon instanceof Cannon) {
            System.out.println(name + ", is shooting with the cannon...");
            String message = "The " + name + " has shot in: " + coordinate.getX() + "-" + coordinate.getY();
            System.out.println(message);
        } else {
            System.out.println(name + ", is shooting a grenade...");
            String message = "The " + name + " has shot in: " + coordinate.getX() + "-" + coordinate.getY();
            System.out.println(message);
        }
        GridCLI.printGrid(ai.getOpponentGrid(), false);
    }
}
