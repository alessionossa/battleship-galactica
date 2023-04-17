package com.galactica.cli;

import com.galactica.model.Grid;

public class GridCLI {

    public static void printGrid(Grid grid, boolean showShip) {
        System.out.print("   |");
        for (int i = 0; i < grid.getGridSize(); i++) {
            System.out.print(" " + (char) ('a' + i));
        }
        System.out.print("\n---+");
        for (int i = 0; i < grid.getGridSize(); i++) {
            System.out.print("--");
        }
        System.out.print("\n");
        for (int i = 0; i < grid.getGridSize(); i++) {
            if (i < 10) {
                System.out.print(i + "  | ");
            } else {
                System.out.print(i + " | ");
            }
            for (int j = 0; j < grid.getTiles()[i].length; j++) {
                String displayValue = grid.getTiles()[i][j].displayValue(showShip);
                System.out.print(displayValue + " ");
            }
            System.out.println();
        }
    }
}
