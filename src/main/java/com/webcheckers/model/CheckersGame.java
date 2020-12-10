package com.webcheckers.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Stack;

/**
 * This Checkers class is the model for the CheckersWebapp, and handles game logic.
 *
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 * @author <a href='mailto:zah1276@rit.edu'>Zeb Hollinger</a>
 */
public class CheckersGame {

    //the player that moves the red pieces
    private Player redPlayer;
    private boolean redPlayerIn;
    private int redPieces;

    //the player that moves the white pieces
    private Player whitePlayer;
    private boolean whitePlayerIn;
    private int whitePieces;

    //the color of the player who must take their turn currently
    private Piece.Color activeColor;

    //the board on which the game in played
    private BoardView board;

    //the moves that were taken this turn
    private Stack<Move> turnMoves;
    private HashSet<Piece> piecesJumped;

    //first piece moved in a turn used to validate king moves
    private Piece firstMovePiece;

    //game running state
    private boolean running;

    //AI game identifier
    private boolean AIgame = false;

    //game messages
    private String gameOverMessage;
    private String invalidTurnErrorMessage;
    public final static String RESIGN_MSG = "%s has resigned.";
    public final static String WIN_BY_DOM_MSG = "%s has captured all the pieces.";
    public final static String WIN_BY_NO_MOVES_MSG = "%s has no more possible moves. %s wins!";

    public final static String AI_NAME = "AI";
    public final static String ALT_AI_NAME = "Nice Try";

    /**
     * Constructor for the Checkers object, which represents a game of checkers
     * @param redPlayer - The player who moves first and controls the red pieces
     * @param whitePlayer - The player who moves second and controls the white pieces
     */
    public CheckersGame(Player redPlayer, Player whitePlayer){
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;

        this.invalidTurnErrorMessage = "No invalid turn has occured yet. (This message should never display";

        this.piecesJumped = new HashSet<>();

        //The red player must always go first
        this.activeColor = Piece.Color.RED;

        //creates a new board for the game to be played on
        //checks for 'cheat code' names that get you special board states for testing
        switch(redPlayer.getName()){
            case BoardView.REDWINS:
            case BoardView.AILOSES:
                board = new BoardView(true);
                board.PlaceSinglePiece(3, 2, Piece.Color.RED);
                board.PlaceSinglePiece(2, 1, Piece.Color.WHITE);
                redPieces = 1;
                whitePieces = 1;
                break;
            case BoardView.WHITEWINS:
            case BoardView.AIWINS:
                activeColor = Piece.Color.WHITE;
                board = new BoardView(true);
                board.PlaceSinglePiece(3, 2, Piece.Color.RED);
                board.PlaceSinglePiece(2, 1, Piece.Color.WHITE);
                redPieces = 1;
                whitePieces = 1;
                break;
            case BoardView.SINGLEMULTIPLE:
                board = new BoardView(true);
                board.PlaceSinglePiece(6, 7, Piece.Color.RED);
                board.PlaceSinglePiece(3, 6, Piece.Color.WHITE);
                board.PlaceSinglePiece(5, 6, Piece.Color.WHITE);
                board.PlaceSinglePiece(3, 4, Piece.Color.WHITE);
                board.PlaceSinglePiece(1, 2, Piece.Color.WHITE);
                redPieces = 1;
                whitePieces = 4;
                break;
            case BoardView.KINGMULTIPLE:
                board = new BoardView(true);
                board.PlaceSinglePiece(0, 7, Piece.Color.RED);
                board.kingPieceAtSpace(0,7);
                board.PlaceSinglePiece(1, 6, Piece.Color.WHITE);
                board.PlaceSinglePiece(3, 4, Piece.Color.WHITE);
                board.PlaceSinglePiece(5, 4, Piece.Color.WHITE);
                board.PlaceSinglePiece(5, 6, Piece.Color.WHITE);
                board.PlaceSinglePiece(3, 6, Piece.Color.WHITE);
                redPieces = 1;
                whitePieces = 5;
                break;
            case BoardView.KINGSIMPLE:
                board = new BoardView(true);
                board.PlaceSinglePiece(7, 0, Piece.Color.RED);
                board.PlaceSinglePiece(7, 2, Piece.Color.RED);
                board.PlaceSinglePiece(7, 4, Piece.Color.RED);
                board.PlaceSinglePiece(7, 6, Piece.Color.RED);
                board.PlaceSinglePiece(1, 0, Piece.Color.RED);
                board.PlaceSinglePiece(0, 5, Piece.Color.WHITE);
                board.PlaceSinglePiece(0, 7, Piece.Color.WHITE);
                board.PlaceSinglePiece(1, 4, Piece.Color.WHITE);
                board.PlaceSinglePiece(2, 5, Piece.Color.WHITE);
                redPieces = 5;
                whitePieces = 4;
                break;
            case BoardView.BLOCKED:
                board = new BoardView(true);
                board.PlaceSinglePiece(0, 7, Piece.Color.WHITE);
                board.PlaceSinglePiece(2, 7, Piece.Color.RED);
                board.PlaceSinglePiece(2, 5, Piece.Color.RED);
                whitePieces = 1;
                redPieces = 2;
                break;
            default:
                this.board = new BoardView();
                this.redPieces = 12;
                this.whitePieces = 12;
                break;
        }

        this.turnMoves = new Stack<>();
        this.firstMovePiece = null;
        running = true;
        this.redPlayerIn = true;
        this.whitePlayerIn = true;
    }

    /**
     * Constructor used for testing the end game scenarios
     */
    public CheckersGame(Player redPlayer, Player whitePlayer, int redPieces, int whitePieces){
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;

        //The red player must always go first
        this.activeColor = Piece.Color.RED;

        //creates a new board for the game to be played on
        this.board = new BoardView();

        this.turnMoves = new Stack<>();

        running = true;

        this.redPlayerIn = true;
        this.whitePlayerIn = true;

        this.redPieces = redPieces;
        this.whitePieces = whitePieces;
    }

    /**
     * Creates a game with an AI opponent
     * @param redPlayer - Human player
     */
    public CheckersGame(Player redPlayer){
        this(redPlayer, new Player((redPlayer.getName().equals(AI_NAME))?ALT_AI_NAME:AI_NAME));
        whitePlayerIn = false; //Makes it so that game will not stay open just for the AI player
        AIgame = true;
    }

    /**
     * gets the InvalidTurnErrorMessage
     * @return String of invalid turn error message
     */
    public String getInvalidTurnErrorMessage()
    {
        return invalidTurnErrorMessage;
    }


    /**
     * Sets the invalid turn message to be what is passed in
     * @param str the error string to set the variable to
     */
    public void setInvalidTurnErrorMessage(String str)
    {
        invalidTurnErrorMessage = str;
    }



    /**
     * Getter method for the red player
     * @return the red player
     */
    public Player getRedPlayer() {
        return this.redPlayer;
    }

    /**
     * Getter method for the white player
     * @return the white player
     */
    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    /**
     * Sets the game's active color to the color passed in
     * @param color - Piece.Color game's active color will be set to
     */
    public void setActiveColor(Piece.Color color)
    {
        this.activeColor = color;
    }


    /**
     * Getter method for who's turn it currently is
     * @return the color of the current turn's player
     */
    public Piece.Color getActiveColor() {
        return this.activeColor;
    }

    /**
     * Getter method for the board
     * @return the board
     */
    public BoardView getBoard(Player p) {
        if (p.equals(whitePlayer)){
            return board.invertBoardView();
        }
        return this.board;
    }

    /**
     * Getter method for the board
     * @return the board
     */
    public BoardView getBoard() {
        if (activeColor == Piece.Color.WHITE){
            return board.invertBoardView();
        }
        return this.board;
    }

    public Stack<Move> getTurnMoves() {
        return this.turnMoves;
    }

    public Piece getFirstMovePiece(){
        return this.firstMovePiece;
    }

    /**
     * Check to see if this game is being played by the given Player
     * @param player the Player for which we are checking if they are playing this game
     * @return true if the Player is playing this game, false otherwise
     */
    public boolean hasPlayer(Player player) {
        return player.equals(redPlayer) && redPlayerIn || player.equals(whitePlayer) && whitePlayerIn;
    }

    /**
     * Check to see if this game is being played by the given Player
     * @param player the Player for which we are checking if they are playing this game
     * @return true if the Player is playing this game, false otherwise
     */
    public boolean hasPlayer(String player) {
        return player.equals(redPlayer.getName()) && redPlayerIn ||
                player.equals(whitePlayer.getName()) && whitePlayerIn;
    }

    /**
     * When a user makes a valid move during their turn, add that move to the stack
     * @param move the valid move to be added to the stack
     */
    public void addMove(Move move) {
        turnMoves.push(move);
        if (this.firstMovePiece == null){
            Position start = move.getStart();
            this.firstMovePiece = getBoard().getSpaceAtRow(start.getRow(), start.getCell()).getPiece();
        }
    }

    /**
     * Gets the set of all pieces that have been jumped so far this turn
     * @return - Hashset of pieces jumped
     */
    public HashSet<Piece> getPiecesJumped(){
        return piecesJumped;
    }

    /**
     * When a user wants to back up their last move, pop it off the top of the stack
     * @return the last move the user made
     */
    public Move removeMove() {
        Move top = turnMoves.pop();

        if(getActiveColor() == Piece.Color.WHITE){
            top.switchMoveSide();
        }

        int[] diffs = TurnUtil.findDifferences(top.getStart(), top.getEnd());
        if(TurnUtil.isMoveJump(diffs[0], diffs[1])){
            Piece jumped = board.getPieceAtSpace(top.getStart().getRow() - diffs[0]/2,
                    top.getStart().getCell() - diffs[1]/2);
            piecesJumped.remove(jumped);
        }

        if (this.turnMoves.size() == 0){
            this.firstMovePiece =  null;
        }
        return top;
    }

    /**
     * When a user finishes their turn, clear the stack of moves so that it's empty
     * for the other player's turn
     */
    public void clearMoves() {
        while (!turnMoves.empty()) {
            turnMoves.pop();
        }
        this.firstMovePiece = null;
        piecesJumped = new HashSet<>();
    }


    /**
     * Check to see if this game is equal to another CheckersGame
     * @param other the other Object to check for equality
     * @return true if these two CheckersGames are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof CheckersGame) {
            CheckersGame that = (CheckersGame) other;
            return this.redPlayer.equals(that.redPlayer) && this.whitePlayer == that.whitePlayer;
        }

        return false;
    }

    /**
     * Hashcode method for a CheckersGame
     * @return an integer hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.redPlayer, this.whitePlayer);
    }

    /**
     * If the game is still running, ends the game and builds the correct resign message.
     * @return - T if game ended successfully, F if game is already over
     */
    public boolean resignGame(Player p){
        if(isOver()){
            return false;
        }else{
            running = false;
            gameOverMessage = String.format(RESIGN_MSG, p.getName());
            return true;
        }
    }


    /**
     * If the game is over, this updates the running boolean
     * to be false and outputs the correct gameOverMessage
     *
     * @param p player who won the game
     */
    public void endGame(Player p){
        running = false;
        if (redPieces == 0 || whitePieces == 0) {
            gameOverMessage = String.format(WIN_BY_DOM_MSG, p.getName());
        } else {
            Player otherPlayer;
            if (p == redPlayer) {
                otherPlayer = whitePlayer;
            } else {
                otherPlayer = redPlayer;
            }
            gameOverMessage = String.format(WIN_BY_NO_MOVES_MSG, p.getName(), otherPlayer.getName());
        }
    }

    /**
     * @return - T if game is over, F otherwise.
     */
    public boolean isOver(){
        return !running;
    }

    /**
     * Returns the message that tells the player why the game has ended.
     * @return gameOverMessage
     */
    public String getGameOverMessage(){
        return gameOverMessage;
    }

    /**
     * Notifies the game that the given player has left
     * @param p - player leaving
     * @return - T if player was in the game and removed, F otherwise.
     */
    public boolean removePlayer(Player p){
        if(isOver()) {
            if (p.equals(getWhitePlayer())) {
                whitePlayerIn = false;
            }else if(p.equals(getRedPlayer())){
                redPlayerIn = false;
            }else{
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * @return - T if both players have left the game.
     */
    public boolean hasNoPlayers(){
        return !(redPlayerIn || whitePlayerIn);
    }

    /**
     * This is a helper function to remove a piece from the piece count
     * for the designated player who had his piece jumped over by the
     * other player
     *
     * @param color color of player who made the jump move
     */
    private void subtractPieceHelper(Piece.Color color){
        if (color == Piece.Color.RED){
            this.whitePieces--;
        }
        else{
            this.redPieces--;
        }
    }

    /**
     * Ends the turn for this player by updating the board with the moves made,
     * changing the activeColor, and double checking that the Move stack is
     * empty.
     */
    public void submitTurn(){
        updateBoardMoves();
        if (this.activeColor == Piece.Color.RED){
            this.activeColor = Piece.Color.WHITE;
        }else {
            this.activeColor = Piece.Color.RED;
        }
        clearMoves();
    }

    /**
     * Update the board using the current moves in the stack. This is only called
     * after a turn has been verified in the submitTurn route.
     */
    private void updateBoardMoves(){
        Move lastMove = this.turnMoves.peek();
        Move currentMove;

        Position start = lastMove.getStart();
        Position end = lastMove.getEnd();

        Space lastSpace = this.getBoard().getRow(end.getRow()).getSpace(end.getCell());

        while (!this.turnMoves.empty()){
            currentMove = this.turnMoves.pop();
            start = currentMove.getStart();
            end = currentMove.getEnd();

            int[] diffs = TurnUtil.findDifferences(start, end);
            if (TurnUtil.isMoveJump(diffs[0], diffs[1])){
                Space jumpedSpace = this.getBoard().getRow(start.getRow()-(diffs[0]/2)).getSpace(start.getCell()-(diffs[1]/2));
                jumpedSpace.removePiece();
                subtractPieceHelper(this.activeColor);
            }
        }

        Space startSpace = this.getBoard().getRow(start.getRow()).getSpace(start.getCell());
        Piece pieceMoved = startSpace.removePiece();
        //king move will be the last move made or the first move off of the stack
        if (lastMove.getEnd().getRow() == 0) {
            pieceMoved.kingMe();
        }

        lastSpace.addPiece(pieceMoved);
        if (this.redPieces == 0){
            endGame(whitePlayer);
        }
        if (this.whitePieces == 0){
            endGame(redPlayer);
        }
    }

    /**
     * Identifies what game this is
     * @return T if this game is an AI game, F otherwise
     */
    public boolean isAIgame(){
        return AIgame;
    }
}
