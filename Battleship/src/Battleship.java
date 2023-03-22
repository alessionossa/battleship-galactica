import java.util.Scanner;

public class Battleship {

    private static int playerTurn;
    static Scanner scanner = new Scanner(System.in);
    private static boolean singlePlayerMode;

    private static Player p1;
    private static Player p2;
    
    public static void main(String[] args) {

        Battleship game = new Battleship();

        Grid grid1 = new Grid();
        Grid grid2 = new Grid();

        
        System.out.println("Would you like to play agianst the computer? (y/n)");
        boolean singlePlayerModeSet = false;
        do {
            char resp = Character.toLowerCase(Battleship.scanner.nextLine().charAt(0));
            if (resp == 'y') {
            	singlePlayerMode = true;
            	singlePlayerModeSet = true;
            } else if (resp == 'n') {
            	singlePlayerMode = false;
            	singlePlayerModeSet = true;
            } else {
                System.out.println("Please only type 'y' or 'n' ");
            }
        } while (singlePlayerModeSet = false);

        if (singlePlayerMode) {
        	p1 = new Player("Player 1", grid1, grid2);
        	p1.placeShips();
        	p2 = new AI("CPU", grid2, grid1);
        	p2.placeShips();
        	
        } else {
        	 p1 = new Player("Player 1", grid1, grid2);
             p1.placeShips();
             p2 = new Player("Player 2", grid2, grid1);
             p2.placeShips();
        }
        
     // TODO: Add scanner to get player name
        
        
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