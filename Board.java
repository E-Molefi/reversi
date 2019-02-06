import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;

/**
 * A model of a playing board for a game of "Reversi".
 *
 * @author Emmanuel Molefi
 * @version 0.2
 */
public class Board
{
     // Testing BEGIN
    ImageIcon blackDisc = new ImageIcon((new ImageIcon(Board.class.getResource("images/black.png")).getImage()));
    ImageIcon whiteDisc = new ImageIcon((new ImageIcon(Board.class.getResource("images/white.png")).getImage()));
    // Testing END
    
    // Final fields:
    // private final String BLACK_PLAYER = "\u25CF";
    // private final String WHITE_PLAYER = "\u25CB";
    // Final fields:
    //private final String BLACK_PLAYER = "\u25CF";
    private final ImageIcon BLACK_PLAYER = blackDisc;
    //private final String WHITE_PLAYER = "\u25CB";
    private final ImageIcon WHITE_PLAYER = whiteDisc;
    private static final int defaultBoardSize = 8; // Default board size.
    
    private final Coord NORTH = new Coord(-1,0);
    private final Coord SOUTH = new Coord(1,0);
    private final Coord EAST = new Coord(0,1);
    private final Coord WEST = new Coord(0,-1);
    private final Coord NORTHEAST = new Coord(-1,1);
    private final Coord NORTHWEST = new Coord(-1,-1);
    private final Coord SOUTHEAST = new Coord(1,1);
    private final Coord SOUTHWEST = new Coord(1,-1);

    private final Coord boardDirections[] = 
        { NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST };
    
    // Fields:
    private JPanel boardPanel;
    private JButton[][] discs;
    private ArrayList<JButton> discsArray;
    // Keep record of the current player with black as default starter.
    // private String currentPlayer = "\u25CF";  // Black initially  
    private ImageIcon currentPlayer = BLACK_PLAYER;
    private int boardSize;
    private boolean changingBoardSize = false;    

    // Moves report.
    private boolean illegalMove = false;
    private boolean availableMoves = true;
    private boolean over = false;
    // Scores report.
    private int totalWhite;
    private int totalBlack;

    private JButton source;
    
    private boolean blackMoves;
    private boolean whiteMoves;
    private boolean skipped;
    // private String skippedPlayer = "";
    private ImageIcon skippedPlayer = null;
    
    /**
     * Create a default Board with a default size of 8 rows and 8 columns.
     * And initialise the necessary fields.
     */
    public Board()
    {
        discsArray = new ArrayList<>();
        boardSize = defaultBoardSize;
        makeBoard();
    }

    /**
     * Create a Board with a specific size.
     */
    public Board(int boardSize)
    {        
        this.boardSize = boardSize;
        makeBoard();
    }

    /**
     * Create a board panel and set the initial four pieces on it.
     */
    private void makeBoard()
    {
        // Board panel.
        boardPanel = new JPanel();
        //boardPanel.setBackground(Color.GREEN);
        boardPanel.setLayout(new GridLayout(getBoardSize(), getBoardSize()));
        // Keep track of the locations between coordinates and JButtons.
        discs = new JButton[getBoardSize()][getBoardSize()];

        for(int x = 0; x < getBoardSize(); x++) {
            for(int y = 0; y < getBoardSize(); y++) {
                discs[x][y] = new JButton();

                discs[x][y].addActionListener(
                    (ActionEvent e) -> { 
                        // Reach the source component.
                        source = (JButton) e.getSource();                        
                        move();
                    }
                );
                boardPanel.add(discs[x][y]);
            }
        }
        
        // Set up for the four initial discs.
        // discs[((getBoardSize()/2) - 1)][((getBoardSize()/2) - 1)].setText("\u25CB"); // White disk
        // discs[((getBoardSize()/2) - 1)][(getBoardSize()/2)].setText("\u25CF"); // Black disk
        // discs[(getBoardSize()/2)][((getBoardSize()/2) - 1)].setText("\u25CF"); // Black disk
        // discs[(getBoardSize()/2)][(getBoardSize()/2)].setText("\u25CB"); // White disk

        // Set up for the four initial discs.
        // // discs[((getBoardSize()/2) - 1)][((getBoardSize()/2) - 1)].setText("\u25CF"); // Black disk
        // // discs[((getBoardSize()/2) - 1)][(getBoardSize()/2)].setText("\u25CB"); // White disk
        // // discs[(getBoardSize()/2)][((getBoardSize()/2) - 1)].setText("\u25CB"); // White disk
        // // discs[(getBoardSize()/2)][(getBoardSize()/2)].setText("\u25CF"); // Black disk
                
        // Disable interactivity with with the board if the play button has not been clicked
        //boardPanel.setVisible(false);
        // Set up for the four initial discs.
        discs[((getBoardSize()/2) - 1)][((getBoardSize()/2) - 1)].setIcon(whiteDisc); // White disk
        discs[((getBoardSize()/2) - 1)][(getBoardSize()/2)].setIcon(blackDisc); // Black disk
        discs[(getBoardSize()/2)][((getBoardSize()/2) - 1)].setIcon(blackDisc); // Black disk
        discs[(getBoardSize()/2)][(getBoardSize()/2)].setIcon(whiteDisc); // White disk
    }

    /**
     * Return the current size of the board for the current game.
     * @return The size of the current board.
     */
    public int getBoardSize()
    {
        return this.boardSize;
    }

    /**
     * @param boardSize The new board size.
     */
    public void setBoardSize(int boardSize)
    {        
        // Set the board size.
        if(getChangingBoardSize()) {
            // Set the board size according to the user input.
            this.boardSize = boardSize;
        }
        else {
            // Set the default board size.
            this.boardSize = defaultBoardSize;
        }
    }

    /**
     * Get the board panel.
     * @return The board panel.
     */
    public JPanel getBoardPanel()
    {
        return boardPanel;
    }

    /**
     * Get the current player.
     * @return The current player.
     */
    public ImageIcon getCurrentPlayer()
    {
        return currentPlayer;
    }
    
    /**
     * @param currentPlayer The player to make the current move.
     */
    public void setCurrentPlayer(ImageIcon currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }
    
    /**
     * @return The player who has been currently skipped.
     */
    public ImageIcon getSkippedPlayer()
    {
        return skippedPlayer;
    }

    /**
     * @return Returns true if the player has made an illegal move.
     *         Returns false otherwise.
     */
    public boolean getIllegalMove()
    {
        return illegalMove;
    }

    // public void setIllegalMove(boolean illegalMove)
    // {
        // this.illegalMove = illegalMove;
    // }

    /**
     * @return Returns true if there is an attempt in changing the board.
     *         Returns false otherwise.
     */
    public boolean getChangingBoardSize()
    {
        return changingBoardSize;
    }

    public void setChangingBoardSize(boolean changingBoardSize)
    {
        this.changingBoardSize = changingBoardSize;
    }

    /**
     * @return The player who uses the black discs.
     */
    public ImageIcon getBlackPlayer()
    {
        return BLACK_PLAYER;
    }

    /**
     * @return The player who uses the white discs.
     */
    public ImageIcon getWhitePlayer()
    {
        return WHITE_PLAYER;
    }

    /**
     * @return The current total number of black discs on the board.
     */
    public int getTotalBlack()
    {
        return totalBlack;
    }

    /**
     * @return The current total number of black discs on the board.
     */
    public int getTotalWhite()
    {
        return totalWhite;
    }
    
    /**
     * @return Returns true if the game is over.
     *         Returns false otherwise.
     */
    public boolean getGameOver()
    {
        return over;
    }
    
    /**
     * This is a helper method to enable us to get all the JButton components and add them to an
     * ArrayList.
     * 
     * @return The collection of JButtons.
     */
    public ArrayList<JButton> getDiscsArray()
    {        
        for(int x = 0; x < (getBoardSize()*getBoardSize()); x++) {
            discsArray.add((JButton) getBoardPanel().getComponent(x));
        }
        
        return discsArray;
    }
    
    /**
     * Specification for the move method.
     */
    private void move()
    {
        for(int i = 0; i < getBoardSize(); i++) {
            for(int j = 0; j < getBoardSize(); j++) {
                if(source == discs[i][j]) {
                    
                    if(canMove(getCurrentPlayer(), i, j)) {
                        // The player can move to the current position.
                        illegalMove = false;
                        // Play the position
                        discs[i][j].setIcon(getCurrentPlayer());
                        // Invoke the capture method.
                        capture(getCurrentPlayer(), i, j);
                        
                        // Find player moves: for previous player
                        int previous = validMoves(getCurrentPlayer());
                        changeTurn();
                        // Find player moves: for current player
                        int current = validMoves(getCurrentPlayer());
                        if(previous == 0 && current == 0) {
                            over = true;
                        }
                        else {
                            over = false;
                        }
                        // Confirm the current player has valid moves //
                        // The moves should not equal zero //
                        if(validMoves(getCurrentPlayer()) == 0) {
                            // A pop-up dialog should alert the current player that
                            // they cannot make any legal moves //
                            skipped = true;
                            discs[i][j].setBackground(null);
                        }
                        else {
                            skipped = false;
                            displayValidMoves(getCurrentPlayer());
                        }
                        
                    }
                    else {
                        // The player cannot move to the current position.
                        // But they have legal moves.
                        //illegalMove = true; // Update the status bar
                        if(discs[i][j].getIcon() == null) {
                            illegalMove = true;
                        }
                        
                    }
                    
                }
            }
        }
        
    }
    
    /**
     * This is a helper method to allow the players to change turns.
     */
    public void changeTurn()
    {
        if(getCurrentPlayer() == BLACK_PLAYER) {
            currentPlayer = WHITE_PLAYER;
        }
        else {
            currentPlayer = BLACK_PLAYER;
        }
    }

    /**
     * @return Returns true if the player is skipped.
     *         Returns false otherwise.
     */
    public boolean skipped()
    {
        return skipped;
    }
    
    /**
     * Reset the state of the skipped player.
     */
    public void resetSkipped()
    {
        skipped = false;
    }

    /**
     * Capture discs starting from the x and y position of the current element
     * in the board.
     * 
     * @param
     * @param
     * @param
     */
    private void capture(ImageIcon player, int verticalPosition, int horizontalPosition)
    {
        ImageIcon enemy = null;
        ImageIcon friend = null;

        if(player == BLACK_PLAYER) {
            enemy = WHITE_PLAYER;
            friend = BLACK_PLAYER;
        }
        else {
            enemy = BLACK_PLAYER;
            friend = WHITE_PLAYER;
        }

        for(int i = 0; i < boardDirections.length; i++) {
            Coord direction = boardDirections[i];
            int xDirection = direction.X;
            int yDirection = direction.Y;
            boolean potential = false;

            // If we are within the board.
            if((horizontalPosition+yDirection) > -1 && (horizontalPosition+yDirection) < getBoardSize() && (verticalPosition+xDirection) < getBoardSize() && (verticalPosition+xDirection) > -1 ) {
                // If we have an enemy next to us, in the direction we are going.
                if(discs[verticalPosition+xDirection][horizontalPosition+yDirection].getIcon() == (enemy)) {
                    // Then the direction has potential.
                    potential = true;
                }
            }

            if(potential) {
                int step = 2;
                while( (horizontalPosition+(step*yDirection)) > -1 && (horizontalPosition+(step*yDirection)) < getBoardSize()
                && (verticalPosition+(step*xDirection)) < getBoardSize() && (verticalPosition+(step*xDirection)) > -1 ) {
                    // Check if there is a friend
                    if(discs[verticalPosition+(step*xDirection)][horizontalPosition+(step*yDirection)].getIcon() == null) {                        
                        break;
                    }

                    if(discs[verticalPosition+(step*xDirection)][horizontalPosition+(step*yDirection)].getIcon() == friend) {

                        for(int z = 1; z < step; z++) {
                            discs[verticalPosition + (z * xDirection)][horizontalPosition + (z * yDirection)].setIcon(friend);
                        }
                        break;
                    }                    
                    step++;
                }                
            }
        }
    }

    /**
     * This is to test if a move is legal.
     * According to the rules of the game.
     * 
     * If the move is not legal,
     * show an appropriate message in the status bar.
     * 
     * A move is legal if it would capture at least one piece,
     * so this method should be similar to the capture method.
     * 
     * @param
     * @param
     * @param
     */
    public boolean isMoveLegal(ImageIcon player, int verticalPosition, int horizontalPosition)
    {
         boolean result = false;
        ImageIcon enemy = null;
        ImageIcon friend = null;

        if(player == BLACK_PLAYER) {
            enemy = WHITE_PLAYER;
            friend = BLACK_PLAYER;
        }
        else {
            enemy = BLACK_PLAYER;
            friend = WHITE_PLAYER;
        }

        // Check if the "move" points to an occupied place.
        if((discs[verticalPosition][horizontalPosition].getIcon() == null)) {

            for(int i = 0; i < boardDirections.length; i++) {
                Coord direction = boardDirections[i];
                int xDirection = direction.X;
                int yDirection = direction.Y;
                int step = 2;

                // If we are within the board.
                if((horizontalPosition+yDirection) > -1 && (horizontalPosition+yDirection) < getBoardSize() && (verticalPosition+xDirection) < getBoardSize() && (verticalPosition+xDirection) > -1 ) {
                    // If we have an enemy next to us, in the direction we are going.
                    if(discs[verticalPosition+xDirection][horizontalPosition+yDirection].getIcon() == enemy) {
                        // Then the direction has potential.

                        while( (horizontalPosition+(step*yDirection)) > -1 && (horizontalPosition+(step*yDirection)) < getBoardSize()
                        && (verticalPosition+(step*xDirection)) < getBoardSize() && (verticalPosition+(step*xDirection)) > -1 ) {
                            // Empty field no good.
                            if(discs[verticalPosition+(step*xDirection)][horizontalPosition+(step*yDirection)].getIcon() == null) {
                                break;                            
                            }
                            // Check if there is a friend
                            if(discs[verticalPosition+(step*xDirection)][horizontalPosition+(step*yDirection)].getIcon() == friend) {
                                result = true;
                            }
                            step++;
                        }
                    }
                }
            }
            
        }
        else {
            // Confirm it does not point to an occupied...
            result = false;
        }
        return result;
    }

    /**
     * Based on the isMoveLegal method,
     * check if the current player can make a valid move.
     * 
     * @param player The player whom to check if they can make a move.
     * @param x      The x component of the current position.
     * @param y      The y component of the current position.
     * 
     * @return       Returns true if the player can move.
     *               Returns false otherwise.
     */
    public boolean canMove(ImageIcon player, int x,int y)
    {
        boolean canMove = false;

        if(player == (BLACK_PLAYER)) {
            if(isMoveLegal(BLACK_PLAYER,x,y)) { // if this statement is false, a move cannot be made
                canMove = true;
            }
        }
        else {
            if(isMoveLegal(WHITE_PLAYER,x,y)) {
                canMove = true;                
            }
        }

        return canMove;
    }

    /**
     * @param player The player whom to count the valid moves for.
     * @return       The total number of moves available for a particular player.
     */
    public int validMoves(ImageIcon player)
    {
        int counter = 0;
        // Going through the array, to find valid coords.
        for(int x = 0; x < discs.length; x++) {
            for(int y = 0; y < discs.length; y++) {
                // Set up the coords for testing
                Coord testCoord = new Coord(x,y);
                // Test for valid move.
                boolean validMove = isMoveLegal(player, x, y);
                if(validMove) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * This is a helper method to give a player hints of their available valid moves.
     * 
     * @param player The player whom to display their valid moves.
     */
    public void displayValidMoves(ImageIcon player)
    {
        for(int x = 0; x < discs.length; x++) {
            for(int y = 0; y < discs.length; y++) {
                // Set up the coords for testing
                Coord testCoord = new Coord(x,y);
                // Test for valid move.
                boolean validMove = isMoveLegal(player, x, y);
                if(validMove) {
                    // We display valid moves only.
                    discs[x][y].setBackground(Color.GREEN);
                }
                else {
                    discs[x][y].setBackground(null);
                }
            }
        }
    }

    /**
     * This is a helper method to update the scores of the players.
     */
    public void score()
    {
        int scoreBlack = 0;
        int scoreWhite = 0;

        for(int x = 0; x < getBoardSize(); x++) {
            for(int y = 0; y < getBoardSize(); y++) {
                if(discs[x][y].getIcon() == BLACK_PLAYER) {
                    scoreBlack++;
                }

                if(discs[x][y].getIcon() == (WHITE_PLAYER)) {
                    scoreWhite++;
                }
            }
        }
        totalBlack = scoreBlack;
        totalWhite = scoreWhite;
    }
    
    /**
     * This is a helper method to record the contents of the board as text.
     * 
     * @return Returns a String representation of the contents of the board.
     */
    public String boardToString()
    {
        String boardString = "";
        
        for(int x = 0; x < (getBoardSize()*getBoardSize()); x++) {
            JButton disc = (JButton) getBoardPanel().getComponent(x);
            boardString += disc.getText() + ",";
        }
        
        return boardString;
    }

    /**
     * This is a helper method to check if the board is full.
     * 
     * @return Returns true if the board is full.
     *         Returns false otherwise.
     */
    public boolean fullBoard()
    {
        int count = 0;

        for(int x = 0; x < discs.length; x++) {
            for(int y = 0; y < discs.length; y++) {
                if(!discs[x][y].getText().equals("")) {
                    count++;
                }
            }
        }
        return count == (getBoardSize()*getBoardSize()) ? true : false;
    }

    /**
     * A helper class to model coordinates of the board.
     */
    private static class Coord
    {        
        private final int X, Y;    // Instance variables.

        /**
         * Create an instance of class Coord and initialise its contents.
         */
        public Coord (int X, int Y)
        {
            this.X = X;
            this.Y = Y;
        }
    }
}