package com.webcheckers.model;

import java.util.Iterator;

class SpaceIterator implements Iterator<Space> {

    /**
     * This class allows the Row class to iterate through the spaces in each Row object
     *
     * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
     */
    private Space[] spaces;
    private int index;

    /**
     * Constructor for the SpaceIterator object
     * @param spaces - the array to iterate through
     */
    public SpaceIterator(Space[] spaces){
        this.spaces = spaces;
        this.index = -1;
    }

    /**
     * Returns whether there is another element in the array to access or not
     */
    @Override
    public boolean hasNext() {
        return this.index < (this.spaces.length-1);
    }

    /**
     * Returns the next element in the array
     */
    @Override
    public Space next() {
        this.index++;
        return this.spaces[index];
    }
}
