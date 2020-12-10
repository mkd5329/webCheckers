package com.webcheckers.model;

/**
 * This class represents a single space on the board that may or may not hold a Piece object.
 *
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 */
public class Space {
    // The current index of the cell and the piece it may hold
    private int cellIdx;
    private Piece piece;
    private boolean valid;

    /**
     * Creates a space and initializes the piece inside of it to null (MAY NEED TO CHANGE THAT)
     * @param cellIdx index of this cell in the row
     */
    public Space(int cellIdx, Piece piece, boolean valid){
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.valid = valid;
    }

    /**
     * Access the index of this space
     * @return cellIdx
     */
    public int getCellIdx() {
        return this.cellIdx;
    }

    /**
     * Checks if this Space is a valid cell to move a piece to (NEEDS CHANGE)
     * @return true if not occupied, otherwise false
     */
    public boolean isValid(){
        return valid;
    }

    /**
     * Access the piece that is currently on this Space in the board
     * @return piece
     */
    public Piece getPiece(){
        return this.piece;
    }

    public Piece removePiece(){
        Piece temp = getPiece();
        this.piece = null;
        this.valid = true;
        return temp;
    }

    public void addPiece(Piece newPiece){
        this.piece = newPiece;
        this.valid = false;
    }
}
