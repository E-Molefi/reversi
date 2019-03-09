
/**
 * This class is the main class of the "Reversi" application.
 * "Reversi" is a two-player game in Java using Swing.
 * 
 * To play this game, create an instance of this class.
 * 
 * This main class creates and initialises the Reversi class
 * which in turn creates and initialises the board and the game starts.
 * It also manages the sessions and game flow.
 *
 * @author Emmanuel Molefi
 * @version 10.05.2018
 */
public class Game
{
    // Fields
    private Reversi reversiGame;
    private boolean gameOver;
    
    /**
     * Create the game and initialise its attributes.
     */
    public Game()
    {
        reversiGame = new Reversi();
        gameOver = false;
        if (reversiGame.getPlaying()) {
            run();
        } else {
            // Set default names
            reversiGame.setDefaultNames();
            // Set playing to true then run the game
            reversiGame.setPlaying();
            run();
        }
        
    }
    
    /**
     * Main play routine. Loops until end of play.
     */
    private void run()
    {
        while(!gameOver) {
            
            if(reversiGame.getPlaying()) {
                if(reversiGame.getNewSession()) {
                    reversiGame.getStatusLabel().setText(reversiGame.getWelcomeMessage());
                }
                else {

                    if(reversiGame.getReversiBoard().getIllegalMove()) {
                        // Specify who made an illegal move as well.
                        reversiGame.getStatusLabel().setText("Illegal move made by " + reversiGame.who());
                    }
                    else {
                        reversiGame.getStatusLabel().setText(reversiGame.who() + ", it is your turn to play.");
                    }

                }
                // Update the game statistics
                reversiGame.gameUpdate();
            }            
            
            if(!reversiGame.getWinner().isEmpty()) {
                reversiGame.resetWinner();
                gameOver = !gameOver; // This is for the session to continue //
            }
        }        
        continueSession();
    }
    
    /**
     * This method allows the players to play many games is one session.
     */
    private void continueSession()
    {
        gameOver = false;
        run();
    }
}