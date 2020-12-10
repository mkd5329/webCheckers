package com.webcheckers.model;

/**
 * Class representing one move that a user makes on the board
 *
 * @author <a href='mailto:mjd6839@rit.edu'>Matthew DellaNeve</a>
 */
public class Move {

    /** The starting position of the piece that was moved */
    private Position start;

    /** The ending position of the piece that was moved */
    private Position end;

    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
    }

    /**
     * Flips the move coordinates to correspond with an inverted board for the white player
     */
    public void switchMoveSide(){
        int startRow = this.getStart().getRow() ;
        int startCell = this.getStart().getCell();

        int endRow = this.getEnd().getRow();
        int endCell = this.getEnd().getCell();
        //System.out.println("switchingSide... " + startRow + " " + startCell + " ---- " + endRow + " " + endCell);

        Position newStart = new Position(7-startRow, 7-startCell);
        Position newEnd = new Position(7-endRow, 7-endCell);

        this.start = newStart;
        this.end = newEnd;
    }

    /**
     * Get the starting position of the piece
     * @return the starting position of the piece
     */
    public Position getStart() {
        return this.start;
    }

    /**
     * Get the ending position of the piece
     * @return the ending position of the piece
     */
    public Position getEnd() {
        return this.end;
    }

    /**
     * Gets start row of this move
     * @return int
     */
    public int getStartRow(){
        return this.getStart().getRow();
    }

    /**
     * Gets start cell of this move
     * @return int
     */
    public int getStartCell(){
        return this.getStart().getCell();
    }

    /**
     * Gets end row of this move
     * @return int
     */
    public int getEndRow(){
        return this.getEnd().getRow();
    }

    /**
     * Gets end cell of this move
     * @return int
     */
    public int getEndCell(){
        return this.getEnd().getCell();
    }


}
