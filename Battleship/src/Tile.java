public class Tile {
    private Ship ship;
    private boolean hit;

    public Tile(Ship ship) { //Needs to be modified so we can have <T> type of ship, obstacles
        this.ship = ship;
        this.hit = false;
    }

    public Tile() {
        this.hit = false;
    }

    /*
    @Override
    public String toString() {
        if (this.ship == null) {
            if (this.hit)
                return "/";
            else
                return "0";
        } else {
            if (this.hit)
                return "X";
            else
                return Integer.toString(ship.getIdentifier());
        }
    }
     */

    public String displayValue(boolean showShip) {
        if (this.ship == null) {
            if (this.hit)
                return "/";
            else
                return "0";
        } else {
            if (this.hit)
                return "";
            else {
                if (showShip)
                    return Integer.toString(ship.getIdentifier());
                else
                    return "0";
            }

        }
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isHit() {
        return hit;
    }
}
