package com.galactica.model;

import java.util.Scanner;

public class Battleship {

	private static int playerTurn;
	private static boolean asteroidMode;

	static Scanner scanner = new Scanner(System.in);
	private static boolean singlePlayerMode;

	private static Player p1;
	private static Player p2;

	public static boolean getAsteroidModeResponse() {
		System.out.println("Would you like to play in obstacle mode? (y/n)");
		while (true) {
			char resp = Character.toLowerCase(Battleship.scanner.nextLine().charAt(0));
			if (resp == 'y') {
				return true;
			} else if (resp == 'n') {
				return false;
			} else {
				System.out.println("Please only type 'y' or 'n' ");
			}
		}

	}

	public static boolean getPlayerModeResponse() {
		System.out.println("Would you like to play against the computer? (y/n)");
		while (true) {
			char resp = Character.toLowerCase(Battleship.scanner.nextLine().charAt(0));
			if (resp == 'y') {
				return true;
			} else if (resp == 'n') {
				return false;
			} else {
				System.out.println("Please only type 'y' or 'n' ");
			}
		}

	}

	public static void main(String[] args) {

		Battleship game = new Battleship();

		Grid grid1 = new Grid();
		Grid grid2 = new Grid();

		asteroidMode = getAsteroidModeResponse();
		if (asteroidMode) {
			grid1.placeAsteroids();
			grid2.placeAsteroids();
		}

		singlePlayerMode = getPlayerModeResponse();
		p1 = new Human(grid1, grid2);
		p1.placeShips();

		if (singlePlayerMode) {
			p2 = new AI("CPU", grid2, grid1);

		} else {
			p2 = new Human(grid2, grid1);
		}
		p2.placeShips();

		playerTurn = 1;
		while (true) {

			if (playerTurn == 1) {
				p1.shoot();
				if (p2.areAllShipsSunk()) {
					game.endGame(p1);
					return;
				}

			} else {
				p2.shoot();
				if (p1.areAllShipsSunk()) {
					game.endGame(p2);
					return;
				}

			}
			if (playerTurn == 1)
				playerTurn = 2;
			else
				playerTurn = 1;
		}
	}

	void endGame(Player winner) {
		System.out.println("Congratulations " + winner.getName() + "! 🍾🎉");
	}

}