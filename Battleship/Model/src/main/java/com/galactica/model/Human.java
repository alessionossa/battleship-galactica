package com.galactica.model;


public class Human extends Player {

	public Human(Grid ownGrid, Grid opponentGrid) {
		super(ownGrid, opponentGrid);
        System.out.println("Enter player name: ");
        this.name = Player.sc.nextLine();
	}

	void placeShip(Ship ship) {

		boolean isValidShipPosition;
		do {

			Coordinate coordinate;
			boolean isValidCoordinate = false;
			do {
				System.out.println("\n" + name + ", where do you want to place the ship?");
				System.out.println("Enter X-coordinate:");
				char x0 = Battleship.scanner.nextLine().charAt(0);
				System.out.println("Enter Y-coordinate:");
				int y0 = Integer.parseInt(Battleship.scanner.nextLine());

				coordinate = new Coordinate(x0, y0);
				isValidCoordinate = ownGrid.isValidCoordinate(coordinate);

				if (!isValidCoordinate) {
					System.out.println("The coordinates you entered are not valid.");
				}
			} while (!isValidCoordinate);

			Direction direction = null;
			do {
				System.out.println(name
						+ ", which direction do you want to place the ship? Enter H for horizontal, V for vertical.");
				char directionChar = Battleship.scanner.nextLine().toLowerCase().trim().charAt(0);

				if (directionChar == Direction.Horizontal.getCharIdentifier()
						|| directionChar == Direction.Vertical.getCharIdentifier())
					direction = Direction.get(directionChar);
			} while (direction == null);

			ship.setCoordinate(coordinate);
			ship.setDirection(direction);

			isValidShipPosition = ownGrid.isValidShipPosition(ship, coordinate, direction);
			if (isValidShipPosition) {
				ownGrid.placeShip(ship, coordinate, direction);
			} else {
				System.out.println("You cannot place a ship here.");
			}

		} while (!isValidShipPosition);
	}

	void removeShip() {
		boolean isValidCoordinate = false;
		Coordinate coordinate;

		System.out.println(name + ", would you like to remove any ships, before starting the game? (yes/no)");
		String shouldIremoveShip = Battleship.scanner.nextLine().toLowerCase();

		while (shouldIremoveShip.equals("yes")) {

			do {
				System.out.println("\n" + name + ", which ship would you like to remove? ");
				System.out.println("Enter X-coordinate:");
				char x0 = Battleship.scanner.nextLine().charAt(0);
				System.out.println("Enter Y-coordinate:");
				int y0 = Integer.parseInt(Battleship.scanner.nextLine());

				coordinate = new Coordinate(x0, y0);
				isValidCoordinate = ownGrid.isValidCoordinate(coordinate);

				if (!isValidCoordinate) {
					System.out.println("The coordinates you entered are not valid.");
				}
			} while (!isValidCoordinate);

			Direction direction = null;
			do {
				System.out.println(name
						+ ", which direction do you want to remove the ship? Enter H for horizontal, V for vertical.");
				char directionChar = Battleship.scanner.nextLine().toLowerCase().trim().charAt(0);

				if (directionChar == Direction.Horizontal.getCharIdentifier()
						|| directionChar == Direction.Vertical.getCharIdentifier())
					direction = Direction.get(directionChar);
			} while (direction == null);

			Ship shipAtCoordinate = ownGrid.getShipAtCoordinate(coordinate);
			// Tile tileAtCoordinate = ownGrid.getTile(coordinate);

			if (shipAtCoordinate != null) {
				System.out.println("Removing the ship.");
				// tileAtCoordinate.setShip(null);

				ownGrid.removeShip(shipAtCoordinate);
				boolean isValidShipPosition;
				do {

					do {
						System.out.println("Where would you like to place this removed ship?");
						System.out.println("Enter X-coordinate:");
						char x0 = Battleship.scanner.nextLine().charAt(0);
						System.out.println("Enter Y-coordinate:");
						int y0 = Integer.parseInt(Battleship.scanner.nextLine());

						coordinate = new Coordinate(x0, y0);
						isValidCoordinate = ownGrid.isValidCoordinate(coordinate);

						if (!isValidCoordinate) {
							System.out.println("The coordinates you entered are not valid.");
						}
					} while (!isValidCoordinate);

					Direction directionNew = null;
					do {
						System.out.println(name
								+ ", which direction do you want to place the ship? Enter H for horizontal, V for vertical.");
						char directionChar = Battleship.scanner.nextLine().toLowerCase().trim().charAt(0);

						if (directionChar == Direction.Horizontal.getCharIdentifier()
								|| directionChar == Direction.Vertical.getCharIdentifier())
							directionNew = Direction.get(directionChar);
					} while (directionNew == null);

					shipAtCoordinate.setCoordinate(coordinate);
					shipAtCoordinate.setDirection(directionNew);

					isValidShipPosition = ownGrid.isValidShipPosition(shipAtCoordinate, coordinate, directionNew);
					if (isValidShipPosition) {
						ownGrid.placeShip(shipAtCoordinate, coordinate, directionNew);
					} else {
						System.out.println("You cannot place a ship here.");
					}
				} while (!isValidShipPosition);
			}

			else {
				System.out.println("There is no ship at this coordinate.");
			}

			ownGrid.printGrid(isValidCoordinate);

			System.out.println(name + ", would you like to remove any ships, before starting the game? (yes/no)");
			shouldIremoveShip = Battleship.scanner.nextLine().toLowerCase();
		}
	}

	void shoot() {
		Coordinate coordinate;
		boolean isValidCoordinate;
		do {
			System.out.println("\n-----------------------------\nOpponents grid:");
			opponentGrid.printGrid(false);

			System.out.println(name + ", where do you want to shoot?");
			System.out.println("Enter X-coordinate:");
			char x0 = Battleship.scanner.nextLine().charAt(0);
			System.out.println("Enter Y-coordinate:");
			int y0 = Integer.parseInt(Battleship.scanner.nextLine());

			coordinate = new Coordinate(x0, y0);
			isValidCoordinate = opponentGrid.isValidCoordinate(coordinate);

			if (!isValidCoordinate) {
				System.out.println("The coordinates you entered are not valid.");
			}
		} while (!isValidCoordinate);

		opponentGrid.setTile(coordinate, true);

		Asteroid asteroidAtCoordinate = opponentGrid.getAsteroidAtCoordinate(coordinate);
		Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);
		if (shipAtCoordinate != null || asteroidAtCoordinate != null) {
			System.out.println("You hit something!");
			boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
			if (isShipSunk) {
				shipAtCoordinate.setSunk(true);
				System.out.println("You sunk a ship! 💥🚢");
			}
		} else
			System.out.println("You missed");

//       System.out.println("\n-------------\nOpponents grid:");
//       opponentGrid.printGrid(false);
	}
}
