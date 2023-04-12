package com.galactica.cli;

import com.galactica.model.Coordinate;
import com.galactica.model.Grid;
import com.galactica.model.Player;
import com.galactica.model.Weapon;

public class WeaponCLI {
    public static Weapon askWeaponToShoot(CLI cli, Player player) {
        System.out.println(player.getName() + ", select a ship: 'c' for cannon, 'g' for grenade, 'l' for lazer");

        while (true) {
            char resp = Character.toLowerCase(cli.scanner.nextLine().charAt(0));
            if (resp == 'c')
                // return new DeathStar(IdGenerator.get());
                return player.getWeapons()[0];
            else if (resp == 'g')
                // return new Cruiser(IdGenerator.get());
                return player.getWeapons()[1];
            else if (resp == 'l')
                // return new Scout(IdGenerator.get());
                return player.getWeapons()[2];
            else
                System.out.println("Please only type 'c', 'g' or 'l'");
        }
    }
}
