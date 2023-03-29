package com.galactica.cli;

import com.galactica.model.Grid;
import com.galactica.model.Player;

import java.util.Scanner;

public class CLI {
    public Scanner scanner = new Scanner(System.in);

    public boolean getAsteroidModeResponse() {
        return askBooleanResponse("Would you like to play in obstacle mode? (y/n)");
    }

    public boolean askBooleanResponse(String question) {
        System.out.println(question);
        while (true) {
            char resp = Character.toLowerCase(this.scanner.nextLine().charAt(0));
            if (resp == 'y') {
                return true;
            } else if (resp == 'n') {
                return false;
            } else {
                System.out.println("Please only type 'y' or 'n' ");
            }
        }
    }

    public boolean getPlayerModeResponse() {
        return askBooleanResponse("Would you like to play against the computer? (y/n)");
    }

    public boolean getPlaceOrRemoveResponse(Player player) { // true = place, false = remove
        if (player.getOwnGrid().anyShipsPlaced()) {
            System.out
                    .println(player.getName() + ", would you like to place or remove a ship?: type 'p' for place, 'r' for remove");
            while (true) {
                char resp = Character.toLowerCase(this.scanner.nextLine().charAt(0));
                if (resp == 'p')
                    return true;
                else if (resp == 'r')
                    return false;
                else
                    System.out.println("Please only type 'p' or 'r'");
            }
        } else
            return true;
    }
}
