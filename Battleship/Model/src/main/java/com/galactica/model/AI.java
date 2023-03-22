package com.galactica.model;

import java.util.Random;

public class AI extends Player {
	private Random random = new Random();
	private final char[] sequence = {'v', 'h'};
	
	public AI(String name, Grid ownGrid, Grid opponentGrid) {
		super(name, ownGrid, opponentGrid);
	}
	
	@Override	
    void placeShip(Ship ship) {

        boolean isValidShipPosition;
        do {
        	
            Coordinate coordinate;
            boolean isValidCoordinate = false;
            do {
            	System.out.println("--------------------------------------------- ");
				System.out.println(name + " is placing the ships... \n");
				char x0 = (char)(random.nextInt(10) + 'a');
				int y0 = random.nextInt(11);

                coordinate = new Coordinate(x0, y0);
                isValidCoordinate = ownGrid.isValidCoordinate(coordinate);

            } while (!isValidCoordinate);

            Direction direction = null;
            do {
                
                char directionChar = sequence[random.nextInt(sequence.length)];
                direction = Direction.get(directionChar);
            } while (direction == null);

            ship.setCoordinate(coordinate);
            ship.setDirection(direction);

            isValidShipPosition = ownGrid.isValidShipPosition(ship, coordinate, direction);
            if (isValidShipPosition)
                ownGrid.placeShip(ship, coordinate, direction);
            
        } while (!isValidShipPosition);
    }
	
	// Should not call the "remove" method on AI
	
	@Override
    void shoot() {
        Coordinate coordinate;
        boolean isValidCoordinate;
        do {
            System.out.println("\n-------------\nAI's turn to shoot:");
            //opponentGrid.printGrid(false);

            System.out.println(name + ", is shooting...");
            char x0 = (char)(random.nextInt(10) + 'a');
			int y0 = random.nextInt(11);
			coordinate = new Coordinate(x0, y0);
			System.out.println("The " + name + " has shot in " + x0 + "-" + y0);
            isValidCoordinate = opponentGrid.isValidCoordinate(coordinate);
        } while (!isValidCoordinate);

        opponentGrid.setTile(coordinate, true);

        Ship shipAtCoordinate = opponentGrid.getShipAtCoordinate(coordinate);
        if (shipAtCoordinate != null) {
            System.out.println("The AI has hit a ship!");
            boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
            if (isShipSunk) {
                shipAtCoordinate.setSunk(true);
                System.out.println("The AI has sunk a ship! 💥🚢");
            }
        } else
            System.out.println("The AI has missed...");
	}
}
