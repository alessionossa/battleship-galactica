import java.util.Scanner;

public class Battleship {

    private static int playerTurn;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Battleship game = new Battleship();

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
                if (p2.areAllShipsSunk()) {
                    game.endGame(p1);
                    return;
                }

            } else {
                p2.shoot();
                if (p1.areAllShipsSunk()) {
                    game.endGame(p2);
                    return;
                }

            }
            if (playerTurn == 1) playerTurn = 2;
            else playerTurn = 1;
        }
    }

    void endGame(Player winner) {
        System.out.println("Congratulations " + winner.getName() + "! 🍾🎉");
    }
}