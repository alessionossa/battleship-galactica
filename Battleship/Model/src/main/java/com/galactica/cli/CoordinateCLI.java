package com.galactica.cli;

import com.galactica.controller.BattleshipCLI;
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
