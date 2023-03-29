package com.galactica.cli;

import com.galactica.model.Grid;

public class GridCLI {

    public static void printGrid(Grid grid, boolean showShip) {
        System.out.println("  | a b c d e f g h i j");
        System.out.println("--+--------------------");
        for (int i = 0; i < grid.getTiles().length; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < grid.getTiles()[i].length; j++) {
                String displayValue = grid.getTiles()[i][j].displayValue(showShip);
                System.out.print(displayValue + " ");
            }
            System.out.println();
        }
    }
}
