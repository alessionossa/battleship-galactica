import java.util.Scanner;

public class Battleship {

    private static int playerTurn;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Grid grid1 = new Grid();
        Grid grid2 = new Grid();

        // TODO: Add scanner to get player name
        Player p1 = new Player("Player 1", grid1, grid2);
        p1.placeShips();

        Player p2 = new Player("Player 2", grid2, grid1);
        p2.placeShips();

        playerTurn = 1;
        while (true) {
            if (playerTurn == 1) {
                p1.shoot();

            } else if (playerTurn == 2) {
                p2.shoot();
            }
            if (playerTurn == 1) playerTurn = 2;
            else playerTurn = 1;
        }
    }
}