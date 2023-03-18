
public class Player {
    private String name; 
    private Ship[] ships;
    private Grid ownGrid; //Player's grid
    private Grid opponentGrid;

    public Player(String name, Grid ownGrid, Grid opponentGrid) {
        this.name = name;
        ships = new Ship[3]; //Placeholder 5
        this.ownGrid = ownGrid;
        this.opponentGrid = opponentGrid;

        initializeShips();
    }

    private void initializeShips() {
        ships[0] = new Ship(5, Ship.ShipType.DeathStar, 1);
        ships[1] = new Ship(3, Ship.ShipType.Cruiser, 2);
        ships[2] = new Ship(1, Ship.ShipType.Scout, 3);
    }

    void placeShips() {
        ownGrid.printGrid(true);
        for (Ship ship : ships) {
            placeShip(ship);
            ownGrid.printGrid(true);

            /*
            Scanner s = new Scanner(System.in);
            char direction;
            do {
                System.out.println("Do do you want to change the position of a ship? Enter Y (Yes) or N (No).");
                direction = Character.toLowerCase(s.next().charAt(0));
            } while (direction != 'y' && direction != 'n');
            if (direction == 'y') {
                System.out.println("You should have thought about that before.");
            }

            s.close();
            */
        }


    }

    void placeShip(Ship ship) {

        boolean isValidShipPosition;
        do {


            Coordinate coordinate;
            boolean isValidCoordinate = false;
            do {
                System.out.println(name + ", where do you want to place the ship?");
                System.out.println("Enter X-coordinate:");
                char x0 = Battleship.scanner.nextLine().charAt(0);
                System.out.println("Enter Y-coordinate:");
                int y0 = Integer.parseInt(Battleship.scanner.nextLine());

                coordinate = new Coordinate(x0, y0);
                isValidCoordinate = ownGrid.isValidCoordinate(coordinate);

                if (isValidCoordinate == false) {
                    System.out.println("The coordinates you entered are not valid.");
                }
            } while (!isValidCoordinate);

            char direction;
            do {
                System.out.println(name + ", which direction do you want to place the ship? Enter H for horizontal, V for vertical.");
                direction = Character.toLowerCase(Battleship.scanner.nextLine().charAt(0));
            } while (direction != 'h' && direction != 'v');

            isValidShipPosition = ownGrid.isValidShipPosition(ship, coordinate, direction);
            if (isValidShipPosition) {
                ownGrid.placeShip(ship, coordinate, direction);
            } else {
                System.out.println("You cannot place a ship here.");
            }


        } while (!isValidShipPosition);
    }

    void removeShip() {

        System.out.println(name + ", which ship do you want to remove?");
        System.out.println("Enter X-coordinate:");
        char x0 = Battleship.scanner.nextLine().charAt(0);
        System.out.println("Enter Y-coordinate:");
        int y0 = Integer.parseInt(Battleship.scanner.nextLine());

        Coordinate coordinate = new Coordinate(x0, y0);

        ownGrid.getTile(coordinate);
    }

    void shoot() {
        Coordinate coordinate;
        boolean isValidCoordinate;
        do {
            System.out.println("\n-------------\nOpponents grid:");
            opponentGrid.printGrid(false);

            System.out.println(name + ", where do you want to shoot?");
            System.out.println("Enter X-coordinate:");
            char x0 = Battleship.scanner.nextLine().charAt(0);
            System.out.println("Enter Y-coordinate:");
            int y0 = Integer.parseInt(Battleship.scanner.nextLine());

            coordinate = new Coordinate(x0, y0);
            isValidCoordinate = opponentGrid.isValidCoordinate(coordinate);

            if (isValidCoordinate == false) {
                System.out.println("The coordinates you entered are not valid.");
            }
        } while (!isValidCoordinate);

        opponentGrid.setTile(coordinate, true);

        boolean hitShip = opponentGrid.checkIfShipIsPresent(coordinate);
        if (hitShip)
            System.out.println("You hit a ship");
        else
            System.out.println("You missed");

        System.out.println("\n-------------\nOpponents grid:");
        opponentGrid.printGrid(false);
    }
}
