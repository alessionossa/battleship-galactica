
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

}