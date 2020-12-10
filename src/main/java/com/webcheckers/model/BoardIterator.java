package com.webcheckers.model;

import java.util.Iterator;

/**
 * Iterator that allows the Board class to iterate through Rows
 *
 * @author <a href='mailto:daw1882@rit.edu'>Dade Wood</a>
 */
class BoardIterator implements Iterator<Row> {
    //the rows to iterate through
    private Row[] rows;

    private int index;


    /**
     * Constructor for the BoardIterator object
     * @param rows - the rows to iterate to
     */
    public BoardIterator(Row[] rows){
        this.rows = rows;
        this.index = -1;
    }

    /**
     * Returns boolean based on whether there is another Row in the array
     */
    @Override
    public boolean hasNext() {
        return this.index < (this.rows.length-1);
    }

    /**
     * Returns the next item in the array
     * @return
     */
    @Override
    public Row next() {
        this.index++;
        return this.rows[index];
    }
}
