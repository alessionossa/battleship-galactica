package com.galactica.cli;

import com.galactica.model.Grid;
import com.galactica.model.Player;
import com.galactica.model.Coordinate;

public class CoordinateCLI {
    public static Coordinate askCoordinateToShoot(CLI cli, Player player, Grid opponentsGrid) {
        Coordinate coordinate;
        boolean isValidCoordinate;
        do {
            System.out.println("\n-----------------------------\nOpponents grid:");
            GridCLI.printGrid(opponentsGrid, false);

            System.out.println(player.getName() + ", where do you want to shoot?");
            System.out.println("Enter X-coordinate:");
            char x0 = cli.scanner.nextLine().charAt(0);
            System.out.println("Enter Y-coordinate:");
            int y0 = Integer.parseInt(cli.scanner.nextLine());

            coordinate = new Coordinate(x0, y0);
            isValidCoordinate = opponentsGrid.isValidCoordinate(coordinate);

            if (!isValidCoordinate) {
                System.out.println("The coordinates you entered are not valid.");
            }
        } while (!isValidCoordinate);

        return coordinate;
    }

    public static char askRowOrColumnToShoot(CLI cli, Player player, Grid opponentsGrid) {
        char line;
        boolean validLetter = false;
        System.out.println("\n-----------------------------\nOpponents grid:");
        GridCLI.printGrid(opponentsGrid, false);
        do {

            System.out.println(player.getName() + ", do you want to laser down a row or a column?");
            System.out.println("Options 'r' row or 'c' column?");
            line = Character.toLowerCase(cli.scanner.nextLine().charAt(0));

            if (line == 'r' || line == 'c') {
                validLetter = true;
            }
        } while (!validLetter);
        return line;
    }

    public static Coordinate askLaserCoordinateToShoot(CLI cli, Player player, Grid opponentsGrid, char rowOrColumn) {
        Coordinate coordinate = null;
        boolean isValidCoordinate = false;
        do {
            if (rowOrColumn == 'r') {
                System.out.println("Enter Y row you want to laser down:");

                int y0 = Integer.parseInt(cli.scanner.nextLine());

                if (y0 >= 0 && y0 < opponentsGrid.getGridSize()) {

                    coordinate = new Coordinate('a', y0);
                    isValidCoordinate = opponentsGrid.isValidCoordinate(coordinate);
                }
            } else {
                System.out.println("Enter X column you want to laser down:");

                char x0 = cli.scanner.nextLine().charAt(0);

                if (x0 >= 'a' && x0 < 'a' + opponentsGrid.getGridSize()) {
                    coordinate = new Coordinate(x0, 0);
                    isValidCoordinate = opponentsGrid.isValidCoordinate(coordinate);
                }
            }

            if (!isValidCoordinate) {
                System.out.println("The coordinates you entered are not valid.");
            }

        } while (!isValidCoordinate);

        return coordinate;
    }

    public static Coordinate askCoordinateToPlaceShip(CLI cli, Player player) {
        Coordinate coordinate;
        boolean isValidCoordinate;
        Grid grid = player.getOwnGrid();
        do {
            System.out.println("\n-----------------------------\n" + player.getName() + "'s grid:");
            GridCLI.printGrid(grid, true);

            System.out.println(player.getName() + ", where do you want to place the ship?");
            System.out.println("Enter X-coordinate:");
            char x0 = cli.scanner.nextLine().charAt(0);
            System.out.println("Enter Y-coordinate:");
            int y0 = Integer.parseInt(cli.scanner.nextLine());

            coordinate = new Coordinate(x0, y0);
            isValidCoordinate = grid.isValidCoordinate(coordinate);

            if (!isValidCoordinate) {
                System.out.println("The coordinates you entered are not valid.");
            }
        } while (!isValidCoordinate);

        return coordinate;
    }
}