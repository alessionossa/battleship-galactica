
public class Grid {
    private Tile[][] tiles = new Tile[10][10];

    public Grid(){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                tiles[i][j] = new Tile(0);
            }
        }
    }
    public void printGrid(){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                System.out.print(tiles[i][j] + " ");
            }
            System.out.println();
        }
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

    void setTile(Coordinate coordinate, Ship ship) {
        int xIndex = coordinate.getX() - 'a';

        tiles[xIndex][coordinate.getY()].setShip(ship);
    }

}