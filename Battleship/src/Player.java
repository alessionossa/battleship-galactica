import java.util.Scanner;

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
        }
    }

    void placeShip(Ship ship) {
        Scanner s = new Scanner(System.in);

        System.out.println("Where do you want to place the ship?");
        System.out.println("Enter X-coordinate:");
        char x0 = s.nextLine().charAt(0);
        System.out.println("Enter Y-coordinate:");
        int y0 = Integer.parseInt(s.nextLine());

        Coordinate coordinate = new Coordinate(x0, y0);

        char direction;
        do {
            System.out.println("Which direction do you want to place the ship? Enter H for horizontal, V for vertical.");
            direction = Character.toLowerCase(s.next().charAt(0));
        } while (direction != 'h' && direction != 'v');

        grid.placeShip(ship, coordinate, direction);
    }

    public void playTurn() {
        grid.printGrid();


    }

}
