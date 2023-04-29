package com.galactica.cli;

import com.galactica.model.Direction;
import com.galactica.model.Player;

public class DirectionCLI {
    public static Direction askDirection(CLI cli, Player player) {
        Direction direction = null;
        do {
            System.out.println(player.getName()
                    + ", which direction do you want to place the ship? Enter H for horizontal, V for vertical.");
            char directionChar = cli.scanner.nextLine().toLowerCase().trim().charAt(0);

            if (directionChar == Direction.Horizontal.getCharIdentifier()
                    || directionChar == Direction.Vertical.getCharIdentifier())
                direction = Direction.get(directionChar);
        } while (direction == null);

        return direction;
    }
}
