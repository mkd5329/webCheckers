package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class PositionTest {
    private final Position position = new Position(0, 0);

    @Test
    public void test_get_row() {
        assertEquals(0, position.getRow());
    }

    @Test
    public void test_get_cell() {
        assertEquals(0, position.getCell());
    }

    @Test
    public void test_equals()
    {
        Position p1 = new Position(1,1);
        Position p2 = new Position(1,1);
        assertTrue(p1.equals(p2));
    }

    @Test
    public void test_not_equals()
    {
        Position p1 = new Position(1,1);
        Position p2 = new Position(2,2);
        assertFalse(p1.equals(p2));
    }
}
