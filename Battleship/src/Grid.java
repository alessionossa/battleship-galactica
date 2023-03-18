
public class Grid {
    private Tile[][] tiles = new Tile[gridSize][gridSize];

    private static int gridSize = 10;

    public Grid(){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                tiles[i][j] = new Tile();
            }
        }
    }

    public void printGrid(boolean showShip){
        System.out.println("    a b c d e f g h i j");
        System.out.println("--+--------------------");
        for(int i = 0; i < tiles.length; i++){
            System.out.print(i + " | ");
            for(int j = 0; j < tiles[i].length; j++){
                String displayValue = tiles[i][j].displayValue(showShip);
                System.out.print(displayValue + " ");
            }
            System.out.println();
        }
    }

    boolean isValidCoordinate(Coordinate coordinate) {
        return coordinate.getX() >= 'a' && coordinate.getX() < 'a'+gridSize && coordinate.getY() >= 0 && coordinate.getY() < gridSize;
    }

    /**
     * This method checks if the position is a valid position
     */
    boolean isValidShipPosition(Ship ship, Coordinate coordinate, char direction) {

        if (direction == 'h') {
            if ((convertToXMatrixIndex(coordinate.getX()) + ship.getLength()) > gridSize) {
                return false;
            }

            for (int i = 0; i < ship.getLength(); i++) {
                char newX = (char) (coordinate.getX() + i);
                Coordinate tileCoordinate = new Coordinate(newX, coordinate.getY());
                if (getTile(tileCoordinate).getShip() != null) {
                    return false;
                }
            }
        } else {
            if ((coordinate.getY() + ship.getLength()) > gridSize) {
                return false;
            }

            for (int i = 0; i < ship.getLength(); i++) {
                int newY = coordinate.getY() + i;
                Coordinate tileCoordinate = new Coordinate(coordinate.getX(), newY);
                if (getTile(tileCoordinate).getShip() != null) {
                    return false;
                }
            }
        }

        return true;
    }

    void placeShip(Ship ship, Coordinate coordinate, char direction) {

        if (direction == 'h') {
            for (int i = 0; i < ship.getLength(); i++) {
                char newX = (char) (coordinate.getX() + i);
                Coordinate tileCoordinate = new Coordinate(newX, coordinate.getY());
                setTile(tileCoordinate, ship);
            }
        } else {
            for (int i = 0; i < ship.getLength(); i++) {
                int newY = coordinate.getY() + i;
                Coordinate tileCoordinate = new Coordinate(coordinate.getX(), newY);
                setTile(tileCoordinate, ship);
            }
        }

    }

    private int convertToXMatrixIndex(char x) {
        return x - 'a';
    }

    void setTile(Coordinate coordinate, Ship ship) {
        int xIndex = convertToXMatrixIndex(coordinate.getX());

        tiles[coordinate.getY()][xIndex].setShip(ship);
    }

    void setTile(Coordinate coordinate, boolean hit) {
        int xIndex = convertToXMatrixIndex(coordinate.getX());

        tiles[coordinate.getY()][xIndex].setHit(hit);
    }

    Tile getTile(Coordinate coordinate) {
        int xIndex = convertToXMatrixIndex(coordinate.getX());

        return tiles[coordinate.getY()][xIndex];
    }

    boolean checkIfShipIsPresent(Coordinate coordinate) {
        if (getTile(coordinate).getShip() == null) {
            return false;
        }
        else return true;
    }
}