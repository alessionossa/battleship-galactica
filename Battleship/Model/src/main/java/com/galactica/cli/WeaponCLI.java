package com.galactica.cli;

import com.galactica.model.Coordinate;
import com.galactica.model.Grid;
import com.galactica.model.Player;
import com.galactica.model.Weapon;

public class WeaponCLI {
    public static Weapon askWeaponToShoot(CLI cli, Player player) {
        System.out.println(player.getName() + ", select a weapon: 'c' for cannon, 'g' for grenade, 'l' for laser");
        boolean isValidWeaponToShoot;
        int whichWeaponToReturn = 0;

        do {
            char resp = Character.toLowerCase(cli.scanner.nextLine().charAt(0));
            if (resp == 'c') {
                isValidWeaponToShoot = true;
                whichWeaponToReturn = 0;
                //return player.getWeapons()[0];
            } else if (resp == 'g' && player.getWeapons()[1].getAmountOfUses() != 0) {
                player.getWeapons()[1].setAmountOfUses();
                whichWeaponToReturn = 1;
                isValidWeaponToShoot = true;
                //return player.getWeapons()[1];
            } else if (resp == 'l' && player.getWeapons()[2].getAmountOfUses() != 0) {
                player.getWeapons()[2].setAmountOfUses();
                System.out.println(player.getWeapons()[2].getAmountOfUses());
                whichWeaponToReturn = 2;
                isValidWeaponToShoot = true;
                //return player.getWeapons()[2];
            } else {
                isValidWeaponToShoot = false;
                System.out.println("Please only type 'c', 'g' or 'l'");
            }
        }while (!isValidWeaponToShoot);

        return player.getWeapons()[whichWeaponToReturn];
        }
    }
