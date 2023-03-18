public class Ship {

    enum ShipType {
        Cruiser, DeathStar, Scout
    }

    private int length;
    private boolean sunk;  //Status of the ship, true if ship is sunk, false if not
    private ShipType shipType;
    private int identifier;

    public Ship(int length, ShipType shipType, int identifier){
        this.length = length;
        this.shipType = shipType;
        this.identifier = identifier;
        this.sunk = false;
    }

    public int getLength() {
        return length;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public int getIdentifier() {
        return identifier;
    }
}