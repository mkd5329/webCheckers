package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class MoveTest {

    private final Move move = new Move(null, null);

    @Test
    public void test_get_start() {
        assertNull(move.getStart());
    }

    @Test
    public void test_get_end() {
        assertNull(move.getEnd());
    }
}
