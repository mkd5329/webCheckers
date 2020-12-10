package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class BoardIteratorTest {
    private final BoardView board = new BoardView();
    private Row[] rows = new Row[2];
    Row row1 = new Row(0);
    Row row2 = new Row(1);

    /**
     * Adds two simple rows to the row array
     */
    private void initialize_rows(){
        rows[0] = row1;
        rows[1] = row2;
    }

    /**
     * Tests the has_next method for the board iterator
     */
    @Test
    public void test_has_next() {
        initialize_rows();
        BoardIterator iterator = new BoardIterator(rows);

        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests the next method for the board iterator
     */
    @Test
    public void test_next() {
        initialize_rows();
        BoardIterator iterator = new BoardIterator(rows);
        assertEquals(row1, iterator.next());
        assertEquals(row2, iterator.next());
    }
}
