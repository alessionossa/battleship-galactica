
public class Player {
    private String name; 
    private Ship[] ships;
    private Grid grid; //Player's grid

    public Player(String name) {
        this.name = name;
        ships = new Ship[3]; //Placeholder 5
        grid = new Grid();

        initializeShips();
    }

    private void initializeShips() {
        ships[0] = new Ship(5, Ship.ShipType.DeathStar, 1);
        ships[1] = new Ship(3, Ship.ShipType.Cruiser, 2);
        ships[2] = new Ship(1, Ship.ShipType.Scout, 3);
    }

    void placeShips() {
        grid.printGrid();
        for (Ship ship : ships) {
            placeShip(ship);
            grid.printGrid();

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
            boolean isValidCoordinate;
            do {
                System.out.println("Where do you want to place the ship?");
                System.out.println("Enter X-coordinate:");
                char x0 = Battleship.scanner.nextLine().charAt(0);
                System.out.println("Enter Y-coordinate:");
                int y0 = Integer.parseInt(Battleship.scanner.nextLine());

                coordinate = new Coordinate(x0, y0);
                isValidCoordinate = grid.isValidCoordinate(coordinate);

                if (isValidCoordinate == false) {
                    System.out.println("The coordinates you entered are not valid.");
                }
            } while (!isValidCoordinate);

            char direction;
            do {
                System.out.println("Which direction do you want to place the ship? Enter H for horizontal, V for vertical.");
                direction = Character.toLowerCase(Battleship.scanner.nextLine().charAt(0));
            } while (direction != 'h' && direction != 'v');

            isValidShipPosition = grid.isValidShipPosition(ship, coordinate, direction);
            if (isValidShipPosition) {
                grid.placeShip(ship, coordinate, direction);
            } else {
                System.out.println("You cannot place a ship here.");
            }


        } while (!isValidShipPosition);
    }

    void removeShip() {

        System.out.println("Which ship do you want to remove?");
        System.out.println("Enter X-coordinate:");
        char x0 = Battleship.scanner.nextLine().charAt(0);
        System.out.println("Enter Y-coordinate:");
        int y0 = Integer.parseInt(Battleship.scanner.nextLine());

        Coordinate coordinate = new Coordinate(x0, y0);

        grid.getTile(coordinate);
    }

    public void playTurn() {
        grid.printGrid();
    }

}
