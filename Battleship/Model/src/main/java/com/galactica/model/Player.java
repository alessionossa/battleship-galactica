package com.galactica.model;

import java.util.Arrays;
import java.util.Scanner;

public abstract class Player {
	protected String name;
	private Ship[] ships;
	protected Grid ownGrid;
	protected Grid opponentGrid;
	static Scanner sc = new Scanner(System.in);

	public Player(Grid ownGrid, Grid opponentGrid) {
		ships = new Ship[3]; // Placeholder 5
		this.ownGrid = ownGrid;
		this.opponentGrid = opponentGrid;

		initializeShips();
	}

	private void initializeShips() {
		ships[0] = new Ship(5, Ship.ShipType.DeathStar, 1);
		ships[1] = new Ship(3, Ship.ShipType.Cruiser, 2);
		ships[2] = new Ship(1, Ship.ShipType.Scout, 3);
	}

	public void placeShips() {
		ownGrid.printGrid(true);
		for (Ship ship : ships) {
			placeShip(ship);
			ownGrid.printGrid(true);

			/*
			 * Scanner s = new Scanner(System.in); char direction; do { System.out.
			 * println("Do do you want to change the position of a ship? Enter Y (Yes) or N (No)."
			 * ); direction = Character.toLowerCase(s.next().charAt(0)); } while (direction
			 * != 'y' && direction != 'n'); if (direction == 'y') {
			 * System.out.println("You should have thought about that before."); }
			 * 
			 * s.close();
			 */
		}

	}

	abstract void placeShip(Ship ship);

	abstract void shoot();

	public boolean areAllShipsSunk() {
		return Arrays.stream(ships).allMatch(ship -> ship.isSunk());
	}

	public String getName() {
		return name;
	}
}
