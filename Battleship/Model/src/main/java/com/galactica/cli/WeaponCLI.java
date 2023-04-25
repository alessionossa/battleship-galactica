package com.galactica.cli;

import com.galactica.model.Cannon;
import com.galactica.model.Grenade;
import com.galactica.model.Laser;
import com.galactica.model.Player;
import com.galactica.model.Weapon;

public class WeaponCLI {
    public static Weapon askWeaponToShoot(CLI cli, Player player) {
        Cannon cannon = player.getCannon();
        Grenade grenade = player.getGrenade();
        Laser laser = player.getLaser();
        char resp;

        for (;;) {
            System.out
                    .println("\n" + player.getName() + ", select a weapon: 'c' for cannon, 'g' for grenade ("
                            + player.getGrenade().getAmountOfUses() + " use(s) left), 'l' for laser ("
                            + player.getLaser().getAmountOfUses() + " use(s) left)");
            resp = Character.toLowerCase(cli.scanner.nextLine().charAt(0));

            if (resp == 'c') {
                return cannon;
            } else if (resp == 'g' && grenade.getAmountOfUses() != 0) {
                grenade.decrementAmountOfUses();
                return grenade;
            } else if (resp == 'l' && laser.getAmountOfUses() != 0) {
                laser.decrementAmountOfUses();
                return laser;
            }
        }

    }
}
