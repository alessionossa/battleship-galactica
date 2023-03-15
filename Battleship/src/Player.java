public class Player {
    private String name; 
    private Ship[] ships;
    private Grid grid; //Player's grid

    public Player(String name) {
        this.name = name;
        ships = new Ship[5]; //Placeholder 5 
        grid = new Grid();
    }

}
