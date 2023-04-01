package com.galactica.cli;

import com.galactica.controller.BattleshipCLI;
import com.galactica.model.*;

public class ShipCLI {

    public static Ship askShip(CLI cli, Player player) {
        System.out.println(player.getName() + ", select a ship: 'd' for death, 'c' for cruiser, 's' for scout");
        // to use more than one ship of each type could use d1, d2, d3 but won't matter
        // with gui
        while (true) {
            char resp = Character.toLowerCase(cli.scanner.nextLine().charAt(0));
            if (resp == 'd')
                // return new DeathStar(IdGenerator.get());
                return player.getShips()[0];
            else if (resp == 'c')
                // return new Cruiser(IdGenerator.get());
                return player.getShips()[1];
            else if (resp == 's')
                // return new Scout(IdGenerator.get());
                return player.getShips()[2];
            else
                System.out.println("Please only type 'd', 'c' or 's'");
        }
    }
}
