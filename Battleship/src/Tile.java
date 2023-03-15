public class Tile {
    private Ship ship;
    private boolean hit;
    private int i;

    public Tile(Ship ship) { //Needs to be modified so we can have <T> type of ship, obsticals
        this.ship = ship;
        hit = false;
    }

    public Tile(int i) { 
        this.i = 0;
    }
    @Override
    public String toString() {
        return Integer.toString(i);
    } 
    
}
