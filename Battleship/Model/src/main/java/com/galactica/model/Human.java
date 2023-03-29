package com.galactica.model;

public class Human extends Player {

    public Human(Grid ownGrid, Grid opponentGrid) {
        super(ownGrid, opponentGrid);
        System.out.println("Enter player name: ");
        this.name = Player.sc.nextLine();
    }

    boolean getPlaceOrRemoveResponse() { // true = place, false = remove
        if (ownGrid.anyShipsPlaced()) {
            System.out
                    .println(name + ", would you like to place or remove a ship?: type 'p' for place, 'r' for remove");
            while (true) {
                char resp = Character.toLowerCase(Battleship.scanner.nextLine().charAt(0));
                if (resp == 'p')
                    return true;
                else if (resp == 'r')
                    return false;
                else
                    System.out.println("Please only type 'p' or 'r'");
            }
        } else
            return true;
    }

    Ship selectShip() {
        System.out.println(name + ", select a ship: 'd' for death, 'c' for cruiser, 's' for scout");
        // to use more than one ship of each type could use d1, d2, d3 but won't matter
        // with gui
        while (true) {
            char resp = Character.toLowerCase(Battleship.scanner.nextLine().charAt(0));
            if (resp == 'd')
                return ships[0];
            else if (resp == 'c')
                return ships[1];
            else if (resp == 's')
                return ships[2];
            else
                System.out.println("Please only type 'd', 'c' or 's'");
        }
    }

    void placeShips() {
        boolean allShipsPlaced = false;
        while (!allShipsPlaced) {
            ownGrid.printGrid(true);
            boolean placeOrRemove = getPlaceOrRemoveResponse();

            Ship ship = selectShip();
            if (!placeOrRemove && !ship.isPlaced()) {
                System.out.println("Cannot remove a ship that hasn't been placed");
            } else if (placeOrRemove && ship.isPlaced()) {
                System.out.println("Cannot place a ship that is already on the grid");
            } else if (!placeOrRemove && ship.isPlaced()) {
                removeShip(ship);
            } else if (placeOrRemove && !ship.isPlaced()) {
                placeShip(ship);
            }

            allShipsPlaced = hasAllShipsPlaced();

        }

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

    void removeShip(Ship ship) {
        ownGrid.removeShip(ship);
        ship.setCoordinate(null);
        ship.setDirection(null);
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
            if (shipAtCoordinate != null) {
                boolean isShipSunk = opponentGrid.checkIfShipIsSunk(shipAtCoordinate);
                if (isShipSunk) {
                    shipAtCoordinate.setSunk(true);
                    System.out.println("You sunk a ship! 💥🚢");
                }
            } else
                System.out.println("You hit something!");
        } else
            System.out.println("You missed");

    }
}
