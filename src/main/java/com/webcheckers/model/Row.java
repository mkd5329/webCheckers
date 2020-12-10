package com.webcheckers.model;

import java.util.Iterator;

/**
 * Represents a row on the game board.
 *
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 */
public class Row implements Iterable<Space> {
    // the index of this row and the spaces it contains
    private int index;
    private Space[] spaces;



    /**
     * Constructs a new row and initializes the spaces inside it
     * @param index index of this row (0-7)
     */
    public Row(int index){
        spaces = new Space[8];

        this.index = index;
        for (int i = 0; i < 8; i++){
            if (this.index < 3){
                if (this.index % 2 == 0 && i % 2 == 1) {
                    this.spaces[i] = new Space(i, new Piece(Piece.Type.SINGLE,
                            Piece.Color.WHITE), false);
                }else if (this.index % 2 == 1 && i % 2 == 0){
                    this.spaces[i] = new Space(i, new Piece(Piece.Type.SINGLE,
                            Piece.Color.WHITE), false);
                }else {
                    this.spaces[i] = new Space(i,null, false);
                }
            }else if (this.index > 4){
                if (this.index % 2 == 0 && i % 2 == 1) {
                    this.spaces[i] = new Space(i, new Piece(Piece.Type.SINGLE,
                            Piece.Color.RED), false);
                }else if (this.index % 2 == 1 && i % 2 == 0){
                    this.spaces[i] = new Space(i, new Piece(Piece.Type.SINGLE,
                            Piece.Color.RED), false);
                }else {
                    this.spaces[i] = new Space(i,null, false);
                }
            }else {
                if (this.index % 2 == 1 && i % 2 == 1){
                    this.spaces[i] = new Space(i, null, false);
                }else if (this.index % 2 == 0 && i % 2 == 0){
                    this.spaces[i] = new Space(i, null, false);
                }else {
                    this.spaces[i] = new Space(i, null, true);
                }
            }
        }
    }

    /**
     * Creates an empty row (for testing).
     * @param index - index of row
     * @param empty - whether T or F, notifies that this should be an empty row
     */
    public Row(int index, boolean empty){
        spaces = new Space[8];
        this.index = index;
        for(int i = 0; i < 8; i++){
            spaces[i] = new Space(i, null, ((index + i)%2) == 1);
        }
    }

    /**
     * Access the index of this row
     * @return index
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * Access the space at a specific index of this row
     * @param idx The index of the space being accessed
     * @return a Space object
     */
    public Space getSpace(int idx) {
        return spaces[idx];
    }

    /**
     * Creates a copy of the current row with all spaces in reverse order
     * @return a copy of the inverted row
     */
    public Row invertSpaces(){
        Space[] copy = new Space[8];
        for (int i = 0; i < spaces.length; i++){
            copy[i] = spaces[spaces.length-i-1];
        }
        Row inverted = new Row(this.getIndex());
        inverted.spaces = copy;
        return inverted;
    }

    /**
     * Iterates through the row in order for the game.ftl to render each space
     * @return iterator for list of Spaces
     */
    @Override
    public Iterator<Space> iterator() {
        return new SpaceIterator(spaces);
    }
}

