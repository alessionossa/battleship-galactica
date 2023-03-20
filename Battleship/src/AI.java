import java.util.Random;

public class AI extends Player {
	private Random random = new Random();
	private char direction;
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

			
			direction = sequence[random.nextInt(sequence.length)];


			isValidShipPosition = ownGrid.isValidShipPosition(ship, coordinate, direction);
			if (isValidShipPosition)
				ownGrid.placeShip(ship, coordinate, direction);
			
			System.out.println(coordinate.toString() + " " + direction + "\n");
			
		} while (!isValidShipPosition);
	}
	
	
	// Should not call the remove method on AI
	
    void shoot() {
        Coordinate coordinate;
        boolean isValidCoordinate;
        do {
            System.out.println("\n-------------\nAI's turn to shoot:");
            opponentGrid.printGrid(false);

            System.out.println(name + ", is shooting...");
            char x0 = (char)(random.nextInt(10) + 'a');
			int y0 = random.nextInt(11);
			coordinate = new Coordinate(x0, y0);

            isValidCoordinate = opponentGrid.isValidCoordinate(coordinate);
        } while (!isValidCoordinate);

        opponentGrid.setTile(coordinate, true);

        boolean hitShip = opponentGrid.checkIfShipIsPresent(coordinate);
        if (hitShip)
            System.out.println("The AI has hit a ship!");
        else
            System.out.println("The AI has missed!");
    }
	
}
