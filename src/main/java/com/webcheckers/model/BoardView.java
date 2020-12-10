package com.webcheckers.model;

import java.util.Iterator;

/**
 * BoardView class, which represents the board on which Checkers is played, and also what is viewed by the users.
 * Utilizes BoardIterator class to iterate through rows.
 *
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 */
public class BoardView implements Iterable {
    //single piece red jump to win
    public static final String REDWINS = "red";
    public static final String AILOSES = "AILoses";


    //single piece white jump to win
    public static final String WHITEWINS = "white";
    public static final String AIWINS = "AIWins";

    //white blocked
    public static final String BLOCKED = "blocked";

    //king multiple jump moves in both directions
    public static final String KINGMULTIPLE = "kingmult";

    //king move made by red player along with simple movements
    public static final String KINGSIMPLE = "kingsimp";

    //tests a single multiple jump move
    public static final String SINGLEMULTIPLE = "singlemult";

    // Array of all the rows
    private Row[] rows = new Row[8];

    /**
     * Constructs a new board view and initializes the rows inside
     */
    public BoardView(){
        for (int i = 0; i < 8; i++){
            this.rows[i] = new Row(i);
        }
    }

    /**
     * Creates an empty BoardView for testing purposes
     * @param empty - whether T or F, boolean signifies that empty testing board is to be made.
     */
    public BoardView(boolean empty){
        for(int i = 0; i < 8; i++){
            this.rows[i] = new Row(i, empty);
        }
    }

    /**
     * Places a single piece at the given row and column of the board
     * @param row - row number of the board where piece will be placed (must be 0-8)
     * @param cell - cell number of the row where piece will be placed (must be 0-8)
     * @param color - Color of piece to be placed
     */
    public void PlaceSinglePiece(int row, int cell, Piece.Color color)
    {
        rows[row].getSpace(cell).addPiece(new Piece(Piece.Type.SINGLE,color));
    }

    /**
     * Removes the piece at the given cell of the board
     * @param row - row number of piece
     * @param cell - cell # of piece
     */
    public void RemovePiece(int row, int cell)
    {
        rows[row].getSpace(cell).removePiece();
    }

    /**
     * Creates a copy of the current BoardView with the orientation reversed
     * @return the inverted copy of the current BoardView
     */
    public BoardView invertBoardView(){
        Row[] copy = new Row[8];
        for (int i = 0; i < rows.length; i++){
            copy[i] = rows[rows.length-i-1].invertSpaces();
        }
        BoardView inverted = new BoardView();
        inverted.rows = copy;
        return inverted;
    }

    /**
     * Gets the space at the given row and cell.
     * @param row - int row # of cell
     * @param cell - int cell # of space
     * @return space from the row
     */
    public Space getSpaceAtRow(int row, int cell)
    {
        return rows[row].getSpace(cell);

    }

    /**
     * Get a piece at a given row and cell
     * @param row the row of the piece
     * @param cell the cell of the piece
     * @return the piece at the row and cell
     */
    public Piece getPieceAtSpace(int row, int cell) {
        return getSpaceAtRow(row, cell).getPiece();
    }

    /**
     * Find a piece at the given row and cell and king it
     * @param row the row of the piece
     * @param cell the cell of the piece
     */
    public void kingPieceAtSpace(int row, int cell) {
        getPieceAtSpace(row, cell).kingMe();
    }

    /**
     * Access a row in this board by its index.
     * @param idx The index of the row being accessed
     * @return A Row object
     */
    public Row getRow(int idx){
        return rows[idx];
    }

    /**
     * Used to iterate through the rows to game.ftl can create the UI
     * @return a new BoardIterator
     */
    @Override
    public Iterator<Row> iterator() {
        return new BoardIterator(rows);
    }
}
