public class Tile {
    private Ship ship;
    private boolean hit;
    private int i;

    public Tile(Ship ship) { //Needs to be modified so we can have <T> type of ship, obstacles
        this.ship = ship;
        this.hit = false;
    }

    public Tile() {
        this.hit = false;
    }

    @Override
    public String toString() {
        if (this.ship == null) {
            return "0";
        } else {
            return Integer.toString(ship.getIdentifier());
        }
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }
}
