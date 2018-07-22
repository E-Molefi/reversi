import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.File;

import java.util.ArrayList;

/**
 * Reversi is the class that builds and displays the application GUI
 * and initialises all other components.
 *
 * @author Emmanuel Molefi
 * @version 10.05.2018
 */
public class Reversi
{
    // Static and final fields:
    private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    private final String welcomeMessage = 
        "Welcome to Reversi! Please enter your names and start playing.";

    // Fields:
    private JFrame frame;
    private JPanel playerPanel;
    private JLabel statusLabel;
    private Board reversiBoard;
    
    private boolean playing;
    private boolean newSession;
    private boolean newGame;
    private ArrayList<String> dataFromFile;
    
    private JButton play;
    private JButton startNewGame;

    private JTextField nameOne;
    private JTextField nameTwo;

    // Score details.
    private JLabel blackPlayerScore;
    private JLabel whitePlayerScore;
    private JLabel blackSessionScore;
    private JLabel whiteSessionScore;
    private int sessionScoreBlack;
    private int sessionScoreWhite;
    private String winner;

    /**
     * Create the "Reversi" application and show its components on the screen.
     * Initialise the necessary fields.
     */
    public Reversi()
    {
        playing = false;
        newSession = false;
        newGame = false;
        winner = "";
        dataFromFile = new ArrayList<>();
        makeFrame();
    }

    // **** Begin Swing stuff to build the frame and all its components and menus ****

    /**
     * Build a frame and show it on the screen.
     */
    private void makeFrame()
    {
        frame = new JFrame("Reversi");
        //Set the frame icon to an image loaded from a file.
        frame.setIconImage(new ImageIcon(Reversi.class.getResource("images/icon.png")).getImage());

        makeMenuBar(frame);
        
        // Create a panel and add components to it.
        JPanel contentPane = (JPanel)frame.getContentPane();
        // Create space around the contentPane
        contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Specify the layout manager.
        contentPane.setLayout(new BorderLayout(6, 6));

        reversiBoard = new Board();
        contentPane.add(reversiBoard.getBoardPanel(), BorderLayout.CENTER);
        
        // Add the Player panel.
        contentPane.add(makePlayerPanel(), BorderLayout.EAST);

        // Status label.
        statusLabel = new JLabel(welcomeMessage);
        contentPane.add(statusLabel, BorderLayout.SOUTH);

        // Exit the program when window closes.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Create a menu bar with menu options on the provided frame.
     * 
     * @param frame The frame that the menu bar should be added to.
     */
    private void makeMenuBar(JFrame frame)
    {
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        
        // Create a menubar for the frame.
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        // Create the Game menu.
        JMenu gameMenu = new JMenu("Game");
        menubar.add(gameMenu);

        // Create the new session menu item.
        JMenuItem newSessionItem = new JMenuItem("New Session");
        newSessionItem.addActionListener(e -> initiateNewSession());
        gameMenu.add(newSessionItem);

        // Create the open menu
        JMenuItem openItem = new JMenuItem("Open...");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
        openItem.addActionListener(e -> openFile());
        gameMenu.add(openItem);

        // Create the saveAs menu item.
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));

        saveAsItem.addActionListener(e -> saveAs());
        gameMenu.add(saveAsItem);

        gameMenu.addSeparator();

        // Create the change board size menu item.
        JMenuItem changeBoardSize = new JMenuItem("Change Board Size...");
        changeBoardSize.addActionListener(e -> newBoardSize());
        gameMenu.add(changeBoardSize);

        gameMenu.addSeparator();

        // Create the quit menu item.
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        quitItem.addActionListener(e -> quit());
        gameMenu.add(quitItem);
        
        // Create the Help menu.
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);
        
        // Create the help menu item.
        JMenuItem helpItem = new JMenuItem("About Reversi");
        helpItem.addActionListener(e -> aboutReversi());
        helpMenu.add(helpItem);
    }

    /**
     * Create a panel for player information and statistics.
     * 
     * @return The newly created player panel.
     */
    private JPanel makePlayerPanel()
    {
        playerPanel = new JPanel();

        // Create a panel for the overall player information and function buttons.
        JPanel playerInfo = new JPanel();
        playerInfo.setLayout(new GridLayout(0, 1));

        // Initialise the session scores for both players.
        sessionScoreBlack = 0;
        sessionScoreWhite = 0;

        // Set up panel for first player.
        // BEGIN
        JPanel blackPanel = new JPanel();
        blackPanel.setLayout(new GridLayout(0, 1));
        nameOne = new JTextField(10);
        blackPanel.add(nameOne);
        blackPlayerScore = new JLabel("Total: " + 2);
        blackPanel.add(blackPlayerScore);
        blackSessionScore = new JLabel("Session Score: " + 0);
        blackPanel.add(blackSessionScore);

        playerInfo.add(blackPanel);

        final TitledBorder blackPanelTitle;
        blackPanelTitle = BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Black");
        blackPanelTitle.setTitleJustification(TitledBorder.LEFT);
        blackPanel.setBorder(blackPanelTitle);
        // END

        // Set up panel for second player.
        // BEGIN
        JPanel whitePanel = new JPanel();
        whitePanel.setLayout(new GridLayout(0, 1));
        nameTwo = new JTextField(10);
        whitePanel.add(nameTwo);
        whitePlayerScore = new JLabel("Total: " + 2);
        whitePanel.add(whitePlayerScore);
        whiteSessionScore = new JLabel("Session Score: " + 0);
        whitePanel.add(whiteSessionScore);

        playerInfo.add(whitePanel);

        final TitledBorder whitePanelTitle;
        whitePanelTitle = BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "White");
        whitePanelTitle.setTitleJustification(TitledBorder.LEFT);
        whitePanel.setBorder(whitePanelTitle);
        // END

        // Set up panel for the function buttons.
        // BEGIN
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0, 1, 0, 4));

        // Create the play button.
        play = new JButton("Play");
        play.addActionListener(e -> startPlaying());
        buttons.add(play);

        // Create the start new game button.
        startNewGame = new JButton("Start New Game");
        startNewGame.addActionListener(e -> newGame());
        buttons.add(startNewGame);

        JPanel buttonsFlow = new JPanel();
        buttonsFlow.add(buttons);                
        playerInfo.add(buttonsFlow);        
        // END

        // Add player information into panel with flow layout for spacing.
        JPanel flow = new JPanel();
        flow.add(playerInfo);
        playerPanel.add(flow);
        startNewGame.setVisible(false);
        
        return playerPanel;
    }

    // **** End Swing stuff to build the frame and all its components and menus ****
    // -----------------------------------------------------------------------------
    // **** Begin implementation of menu functions ****
    
    /**
     * Show the 'About...' dialog.
     */
    private void aboutReversi()
    {
        JOptionPane.showMessageDialog(frame, about(), "About Reversi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Create the "Reversi" about message.
     * @return The "Reversi" about message.
     */
    private String about()
    {
        return "Reversi is a two-player game in which the \n" + 
                "players place identical pieces called disks \non  a rectangular board.\n" + 
                "Disks are light on one side and dark on the other.\nThe objective of the game " +
                "is to have \nthe majority of disks turned to display\nthe player's colour " + 
                "when the\nlast playable empty square is filled.";
    }
    
    /**
     * Set up the board and the player panel for a new game of "Reversi".
     */
    private void newGame()
    {
        // Set up the board for a new game.
        frame.getContentPane().remove(reversiBoard.getBoardPanel());
        reversiBoard = new Board();
        frame.getContentPane().add(reversiBoard.getBoardPanel(), BorderLayout.CENTER);

        // Set up the player panel  and status bar for the new game.
        getStatusLabel().setText(who() + ", it is your turn to play.");

        getBlackPlayerScore().setText("Total: " + 2);
        getWhitePlayerScore().setText("Total: " + 2);
        // Show available moves.
        reversiBoard.displayValidMoves(reversiBoard.getBlackPlayer());

        newGame = true;
        playing = true;
        winner = "";
        startNewGame.setVisible(false);
        play.setEnabled(true);
        // Pack the contents in order.
        frame.pack();
        frame.repaint();
    }

    /**
     * Initiate a new session for different players.
     */
    private void initiateNewSession()
    {
        newSession = true;
        // Reset the player panel.
        frame.getContentPane().remove(reversiBoard.getBoardPanel());
        reversiBoard = new Board();
        frame.getContentPane().add(reversiBoard.getBoardPanel(), BorderLayout.CENTER);
        frame.getContentPane().remove(playerPanel);
        frame.getContentPane().add(makePlayerPanel(), BorderLayout.EAST);
        frame.pack();
        frame.repaint();
    }

    /**
     * The players can now start playing the "Reversi" game in turns.
     * The black player starts placing disks on the board.
     */
    private void startPlaying()
    {
        // Disable the edit properties of the name fields.
        nameOne.setEditable(false);
        nameTwo.setEditable(false);
        // Let players begin to play. Show available moves.
        getReversiBoard().displayValidMoves(reversiBoard.getBlackPlayer());
        getStatusLabel().setText(who() + ", it is your turn to play.");
        playing = true;
        setNewSession(false);
    }

    /**
     * Change Board Size... function: Handle the process for changing the size of the current board.
     * If a game is in progress the application asks for confirmation before resetting the table.
     */
    private void newBoardSize()
    {        
        playing = true;
        int inputBoardSize;

        if(playing) {
            // Prompt the players if they are sure and are aware of the consequenses.
            int reset = JOptionPane.showConfirmDialog(frame, "Clicking yes will reset the game.", 
                    "Changing Board Size", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if(reset == JOptionPane.YES_OPTION) {
                String inputValue = JOptionPane.showInputDialog("Please input an even number greater than 5", "");

                if(inputValue == null) {
                    // clicked cancel
                    return;
                }

                inputBoardSize = Integer.parseInt(inputValue);
                // Handle input from the user.
                if((inputBoardSize%2 == 0) && (inputBoardSize > 5)) {
                    getReversiBoard().setChangingBoardSize(true);
                    // Reset the game.
                    frame.getContentPane().remove(getReversiBoard().getBoardPanel());
                    reversiBoard = new Board(inputBoardSize);
                    frame.getContentPane().add(reversiBoard.getBoardPanel(), BorderLayout.CENTER);
                    frame.pack();
                    frame.repaint();
                    // Display the current players' moves.
                    reversiBoard.displayValidMoves(reversiBoard.getCurrentPlayer());
                }
            }

        }
    }
    
    /**
     * Save As function: save the current game session to a file.
     */
    private void saveAs()
    {
        // Only save a game that was started
        if(getPlaying()) {
            int returnVal = fileChooser.showSaveDialog(frame);

            if(returnVal != JFileChooser.APPROVE_OPTION) {
                return;  // cancelled
            }
            File selectedFile = fileChooser.getSelectedFile();

            ReversiFileHandler.saveToFile(selectedFile, gameInformation());
        }
    }

    /**
     * Open function: open a file chooser to select a new reversi game file,
     * and then set up the chosen game.
     */
    private void openFile()
    {
        int returnVal = fileChooser.showOpenDialog(frame);

        if(returnVal != JFileChooser.APPROVE_OPTION) {
            return;  // cancelled
        }
        File selectedFile = fileChooser.getSelectedFile();

        // Add functionality to load the game.
        String data = ReversiFileHandler.loadFromFile(selectedFile);
        //System.out.println(data);
        String[] dataArray = data.split(",");  // split at new line

        for(String nextLine : dataArray) {
            dataFromFile.add(nextLine);
        }

        // Call the method to restore the player information 
        // that was saved to file.
        restoreGameInformation();
    }

    /**
     * Quit function: quit the application.
     */
    private void quit()
    {
        System.exit(0);
    }

    // **** End implementation of menu functions ****

    // *** Begin implementation of helper methods ****

    /**
     * @return The current board of the game application.
     */
    public Board getReversiBoard()
    {
        return reversiBoard;
    }

    /**
     * @return The current total number of black discs on the board.
     */
    public JLabel getBlackPlayerScore()
    {
        return blackPlayerScore;
    }

    /**
     * @return The current total number of white discs on the board.
     */
    public JLabel getWhitePlayerScore()
    {
        return whitePlayerScore;
    }

    /**
     * @return The current session score for the player using black discs.
     */
    public JLabel getBlackSessionScore()
    {
        return blackSessionScore;
    }

    /**
     * @return The current session score for the player using white discs.
     */
    public JLabel getWhiteSessionScore()
    {
        return whiteSessionScore;
    }

    /**
     * @return The current status of the game.
     */
    public JLabel getStatusLabel()
    {
        return statusLabel;
    }

    /**
     * @return The current player panel.
     */
    public JPanel getPlayerPanel()
    {
        return playerPanel;
    }

    /**
     * @return The name of the player playing currently.
     */
    public String who()
    {
        if(getReversiBoard().getCurrentPlayer().equals(getReversiBoard().getBlackPlayer())) {
            return getNameOne();
        }
        else {
            return getNameTwo();
        }        
    }

    /**
     * @return A String format of the current game information.
     */
    private String gameInformation()
    {
        String info = getReversiBoard().getCurrentPlayer() + "," + getNameOne() + "," + 
            getBlackPlayerScore().getText() + "," + 
            getBlackSessionScore().getText() + "," + 
            getNameTwo() + "," + getWhitePlayerScore().getText() + "," + 
            getWhiteSessionScore().getText() + "," + getReversiBoard().boardToString();
        return info;
    }

    /**
     * Restore a previously saved game application.
     */
    private void restoreGameInformation()
    {
        // Set the current player
        getReversiBoard().setCurrentPlayer(dataFromFile.get(0));

        // Player One details
        nameOne.setText(dataFromFile.get(1));
        getBlackPlayerScore().setText(dataFromFile.get(2));
        getBlackSessionScore().setText(dataFromFile.get(3));

        // Player Two details
        nameTwo.setText(dataFromFile.get(4));
        getWhitePlayerScore().setText(dataFromFile.get(5));
        getWhiteSessionScore().setText(dataFromFile.get(6));

        int EOF = 7;
        int index = 0;
        ArrayList<JButton> tempDiscArray = getReversiBoard().getDiscsArray();
        while(EOF < dataFromFile.size()) {
            tempDiscArray.get(index).setText(dataFromFile.get(EOF));
            EOF++;
            index++;
        }

        getReversiBoard().displayValidMoves(getReversiBoard().getCurrentPlayer());
        playing = true;
    }

    /**
     * @return The name of the player using black discs.
     */
    public String getNameOne()
    {
        return nameOne.getText();
    }

    /**
     * @return The name of the player using white discs.
     */
    public String getNameTwo()
    {
        return nameTwo.getText();
    }

    /**
     * @return The state of the current session.
     */
    public boolean getNewSession()
    {
        return newSession;
    }

    /**
     * Set the state of the current session.
     * 
     * @param The state of the current session.
     */
    public void setNewSession(boolean newSession)
    {
        this.newSession = newSession;
    }

    /**
     * @return The winner of the game.
     */
    public String getWinner()
    {
        return winner;
    }

    /**
     * Reset the winner of the game.
     */
    public void resetWinner()
    {
        winner = "";
    }

    /**
     * @return The state of the game play.
     */
    public boolean getPlaying()
    {
        return playing;
    }

    /**
     * @return A String object representing a welcome message..
     */
    public String getWelcomeMessage()
    {
        return welcomeMessage;
    }

    /**
     * Alert the current player that they cannot make any legal move.
     * So they should confirm that the turn moves to the other player.
     */
    private void pass()
    {
        String who = "";
        if(getReversiBoard().getCurrentPlayer().equals(getReversiBoard().getBlackPlayer())) {
            who = getNameOne();
        }
        else {
            who = getNameTwo();
        }
        JOptionPane.showMessageDialog(frame, who + ", you have no legal moves,\nClick on the 'OK' button to Pass!");
        getReversiBoard().resetSkipped();
        getReversiBoard().changeTurn();
    }

    /**
     * Determine who the winner of the current game is.
     * The player with the highest score is declared a winner.
     * And make visible a button on the right panel for players to initiate a new game.
     */
    public void winner()
    {
        if(getReversiBoard().getTotalBlack() > getReversiBoard().getTotalWhite()) {
            winner = getNameOne();
            sessionScoreBlack++;
            blackSessionScore.setText("Session Score: " + sessionScoreBlack);
        }
        else if(getReversiBoard().getTotalWhite() > getReversiBoard().getTotalBlack()) {
            winner = getNameTwo();
            sessionScoreWhite++;
            whiteSessionScore.setText("Session Score: " + sessionScoreWhite);
        }
        else {
            winner = "no one.\nIt is a draw game";
        }

        JOptionPane.showMessageDialog(frame, "The winner is " + winner + ".");
        // After winner declared set visible the button in an attempt to initiate a new game.
        startNewGame.setVisible(true);
        playing = false;
        // Disable the play button after the game finishes.
        play.setEnabled(false);
    }

    /**
     * Update player scores on the right pane.
     * And check if the player has no legal moves so they can be skipped
     * otherwise if the game is has finished.
     */
    public void gameUpdate()
    {
        getReversiBoard().score();
        getBlackPlayerScore().setText("Total: " + getReversiBoard().getTotalBlack());
        getWhitePlayerScore().setText("Total: " + getReversiBoard().getTotalWhite());

        if((getReversiBoard().fullBoard()) || getReversiBoard().getGameOver()) {
            winner();
        }
        else {
            if(getReversiBoard().skipped()) {
                pass();
            }
        }
    }
}