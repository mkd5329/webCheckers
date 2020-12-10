package com.webcheckers.model;

/**
 * Class that represents a location on the game board
 */
public class Position {

    /**
     * The row of this position on the board
     */
    private int row;

    /**
     * The cell on the given row for this position
     */
    private int cell;

    /**
     * Construct a new Position
     * @param row The row of this position on the board
     * @param cell The cell on the given row for this position
     */
    public Position(int row, int cell){
        this.row = row;
        this.cell = cell;
    }

    /**
     * Get this position's row
     * @return this position's row
     */
    public int getRow() {
        return row;
    }

    /**
     * Get this position's cell
     * @return this position's cell
     */
    public int getCell() {
        return cell;
    }

    /**
     * Equals method to determine if this Position is equal to another object that might be
     * a Position with the same row and cell indices
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position){
            Position copy = (Position)obj;
            return copy.getRow() == this.row && copy.getCell() == this.cell;
        }else {
            return false;
        }
    }
}
